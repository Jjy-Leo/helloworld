package cn.jjy.reggie.controller;

import cn.jjy.reggie.common.R;
import cn.jjy.reggie.dto.SetmealDto;
import cn.jjy.reggie.entity.Setmeal;
import cn.jjy.reggie.entity.SetmealDish;
import cn.jjy.reggie.service.SetmealDishService;
import cn.jjy.reggie.service.SetmealService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @program: reggie_take_out
 * @description:
 * @author: jjy
 * @create: 2022-11-10 00:42
 **/
@RestController
@RequestMapping("setmeal")
public class SetmealController {

    @Autowired
    private SetmealDishService setmealDishService;

    @Autowired
    private SetmealService setmealService;

    @GetMapping("/page")
    public R<Page> page(int page,int pageSize){
        Page<Setmeal> setmealPage = new Page<>(page, pageSize);
        Page<SetmealDto> setmealDtoPage = new Page<>(page, pageSize);
        setmealService.page(setmealPage);
        BeanUtils.copyProperties(setmealPage, setmealDtoPage, "records");
        List<Setmeal> setmealList = setmealPage.getRecords();
        List<SetmealDto> collect = setmealList.stream().map(item -> {
            SetmealDto setmealDto = new SetmealDto();
            BeanUtils.copyProperties(item, setmealDto);
            LambdaQueryWrapper<SetmealDish> queryWrapper = new LambdaQueryWrapper();
            queryWrapper.eq(SetmealDish::getSetmealId, item.getId());
            List<SetmealDish> list = setmealDishService.list(queryWrapper);
            setmealDto.setSetmealDishes(list);
            return setmealDto;
        }).collect(Collectors.toList());
        setmealDtoPage.setRecords(collect);
        return R.success(setmealDtoPage);
    }

    @PostMapping
    @Transactional
    public R<String> save(@RequestBody SetmealDto setmealDto) {
        setmealDto.toString();
        System.out.println(setmealDto);
        //先存套餐，再存套餐菜品关系，不然没办法获得套餐id
        //获得套餐
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealDto,setmeal);
        //存套餐
        setmealService.save(setmeal);
        //获得套餐id
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.eq(Setmeal::getName, setmealDto.getName());
        Setmeal serviceOne = setmealService.getOne(queryWrapper);
        Long setmealId = serviceOne.getId();
        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();
        List<Object> collect = setmealDishes.stream().map(item -> {
            //将菜品对象的套餐id附上去，然后存到数据库中
            item.setSetmealId(setmealId);
            setmealDishService.save(item);
            return item;
        }).collect(Collectors.toList());
        return R.success("新增成功");
    }

    @GetMapping("{id}")
    public R<SetmealDto> getSetmeal(@PathVariable String id) {
        SetmealDto setmealDto = new SetmealDto();
        Setmeal setmeal = setmealService.getById(id);
        LambdaQueryWrapper<SetmealDish> lambdaQueryWrapper = new LambdaQueryWrapper();
        lambdaQueryWrapper.eq(SetmealDish::getSetmealId, id);
        List<SetmealDish> list = setmealDishService.list(lambdaQueryWrapper);
        BeanUtils.copyProperties(setmeal, setmealDto);
        setmealDto.setSetmealDishes(list);
        return R.success(setmealDto);
    }

    @PutMapping
    @Transactional
    public R<String> update(@RequestBody SetmealDto setmealDto){
        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealDto, setmeal);
        //套餐信息修改
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Setmeal::getId, setmeal.getId());
        setmealService.update(setmeal,queryWrapper);
        //对于套餐菜品关系表，要先删除在新增
        LambdaQueryWrapper<SetmealDish> setmealDishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        setmealDishLambdaQueryWrapper.eq(SetmealDish::getSetmealId, setmealDto.getId());
        setmealDishService.remove(setmealDishLambdaQueryWrapper);

        setmealDto.getSetmealDishes().stream().map(item -> {
            item.setSetmealId(setmealDto.getId());
            setmealDishService.save(item);
            return item;
        }).collect(Collectors.toList());


        return R.success("修改成功");
    }

    @PostMapping("/status/{status}")
    public R<String> status(@PathVariable Integer status, @RequestParam(name = "ids") List<String> setmealId) {
        for (int i = 0; i < setmealId.size(); i++) {
            Setmeal setmeal = setmealService.getById(setmealId.get(i));
            setmeal.setStatus(status);
            LambdaQueryWrapper<Setmeal> setmealLambdaQueryWrapper = new LambdaQueryWrapper<>();
            setmealLambdaQueryWrapper.eq(Setmeal::getId, setmeal.getId());
            setmealService.update(setmeal, setmealLambdaQueryWrapper);
        }
        return R.success("修改成功");
    }

    @DeleteMapping
    public R<String> deleteWithSetmealDish(@RequestParam(name = "ids") List<Long> setmealIdList) {
        return setmealService.deleteWithSetmealDish(setmealIdList);
    }
}
