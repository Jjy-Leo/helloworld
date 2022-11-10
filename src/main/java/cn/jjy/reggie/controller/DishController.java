package cn.jjy.reggie.controller;

import cn.jjy.reggie.common.R;
import cn.jjy.reggie.dto.DishDto;
import cn.jjy.reggie.entity.*;
import cn.jjy.reggie.service.*;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @program: reggie_take_out
 * @description:
 * @author: jjy
 * @create: 2022-11-02 06:35
 **/
@RestController
@Slf4j
@RequestMapping("/dish")
public class DishController {

    @Autowired
    private DishService dishService;


    @Autowired
    private DishFlavorService dIshFlavorService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private SetmealDishService setmealDishService;

    @Autowired
    private SetmealService setmealService;

    @PostMapping
    public R<String> save(@RequestBody DishDto dishDto){
        dishService.saveWithFlavor(dishDto);
        return R.success("保存成功");
    }

    @GetMapping("/page")
    public R<Page<DishDto>> page(int page, int pageSize, String name) {
        //创建分页对象
        Page<Dish> dishPage = new Page<>(page, pageSize);
        //创建DishPage分页对象
        Page<DishDto> dishDtoPage = new Page<>(page, pageSize);
        //创建条件表达式
        LambdaQueryWrapper<Dish> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        //按照更新时间排序
        lambdaQueryWrapper.orderByDesc(Dish::getUpdateTime);
        lambdaQueryWrapper.eq(name != null, Dish::getName, name);
        //查询数据库得到分页对象
        dishService.page(dishPage, lambdaQueryWrapper);
        //得到dish对象的list
        List<Dish> dishes = dishPage.getRecords();
        //利用lambda表达式，处理这个list，得到有categoryname的list
        List<DishDto> dishDtos = dishes.stream().map(item -> {
            DishDto dishDto = new DishDto();
            BeanUtils.copyProperties(item, dishDto);
            Long categoryId = item.getCategoryId();
            Category category = categoryService.getById(categoryId);
            String categoryName = category.getName();

            dishDto.setCategoryName(categoryName);
            return dishDto;
        }).collect(Collectors.toList());
        BeanUtils.copyProperties(dishPage,dishDtoPage);
        dishDtoPage.setRecords(dishDtos);
        return R.success(dishDtoPage);
    }

    @GetMapping("{id}")
    public R<DishDto> getDish(@PathVariable String id ){
        Dish dish = dishService.getById(id);
        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DishFlavor::getDishId, dish.getId());
        queryWrapper.orderByDesc(DishFlavor::getUpdateTime);
        List<DishFlavor> list = dIshFlavorService.list(queryWrapper);
        DishDto dishDto = new DishDto();
        BeanUtils.copyProperties(dish,dishDto);
        dishDto.setFlavors(list);
        return R.success(dishDto);
    }

    @PutMapping
    public R<String> put(@RequestBody DishDto dishDto) {
        List<DishFlavor> flavors = dishDto.getFlavors();
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDto,dish);

        //对于口味表，先删除，后添加，因为修改是按照id修改的，可能造成错误
        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.eq(DishFlavor::getDishId, dish.getId());
        dIshFlavorService.remove(queryWrapper);

        flavors.stream().map(itme -> {
            itme.setDishId(dish.getId());
            dIshFlavorService.save(itme);
            return itme;
        }).collect(Collectors.toList());

        dishService.updateById(dish);
        return R.success("修改成功");
    }

    @GetMapping("/list")
    public R<List> list(String categoryId) {
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.eq(categoryId != null, Dish::getCategoryId, categoryId);
        List<Dish> list = dishService.list(queryWrapper);
        return R.success(list);
    }


    @PostMapping("/status/{status}")
    @Transactional
    public R<String> status(@PathVariable Integer status ,@RequestParam(name = "ids") List<String> ids) {
        for (int i = 0; i < ids.size(); i++) {
            Dish dish = dishService.getById(ids.get(i));
            dish.setStatus(status);
            dishService.updateById(dish);
            //菜品禁售，对应的套餐也禁售
            //通过dish的id获得setmealid
            LambdaQueryWrapper<SetmealDish> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(SetmealDish::getDishId, dish.getId());
            SetmealDish setmealDish = setmealDishService.getOne(queryWrapper);
            if (setmealDish != null) {
                Long setmealId = setmealDish.getSetmealId();
                //通过setmealid得到setmeal对象
                Setmeal setmeal = setmealService.getById(setmealId);
                LambdaQueryWrapper<Setmeal> setmealLambdaQueryWrapper = new LambdaQueryWrapper<>();
                setmealLambdaQueryWrapper.eq(Setmeal::getId, setmealId);
                setmeal.setStatus(status);
                setmealService.update(setmeal, setmealLambdaQueryWrapper);
            }
        }
        return R.success("操作成功");
    }

    @DeleteMapping
    @Transactional
    public R<String> delete(@RequestParam(name = "ids") List<String> dishId) {
        for (int i = 0; i < dishId.size(); i++) {

            //套餐中含有这个菜品时，不可以删除
            LambdaQueryWrapper<SetmealDish> setmealLambdaQueryWrapper = new LambdaQueryWrapper<>();
            setmealLambdaQueryWrapper.eq(SetmealDish::getDishId, dishId.get(i));
            //获得所有含此菜品的套餐信息
            List<SetmealDish> list = setmealDishService.list(setmealLambdaQueryWrapper);
            //如果这个list的size大于0，则可以删除，否则不行
            if (list.size() > 0) {
                return R.error("不可删除!" + setmealService.getById(list.get(0).getSetmealId()).getName() + "套餐中含有"+
                        dishService.getById(dishId.get(i)).getName()+"菜品！");
            } else {
                dishService.removeById(dishId.get(i));
            }
        }
        return R.success("删除成功");
    }
}
