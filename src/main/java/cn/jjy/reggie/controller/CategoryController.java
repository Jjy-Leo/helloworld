package cn.jjy.reggie.controller;

import cn.jjy.reggie.common.R;
import cn.jjy.reggie.entity.Category;
import cn.jjy.reggie.entity.Dish;
import cn.jjy.reggie.entity.Setmeal;
import cn.jjy.reggie.service.CategoryService;
import cn.jjy.reggie.service.DishService;
import cn.jjy.reggie.service.SetmealService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @program: reggie_take_out
 * @description:
 * @author: jjy
 * @create: 2022-11-02 05:50
 **/
@RestController
@RequestMapping("/category")
@Slf4j
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private DishService dishService;

    @Autowired
    private SetmealService setmealService;

    @PostMapping
    public R<String> save(@RequestBody Category category){
        categoryService.save(category);
        log.info("新增分类成功");
        return R.success("新增分类成功");
    }

    @GetMapping("/page")
    private R<Page> page(int page,int pageSize){

        Page pageInfo = new Page(page,pageSize);
        LambdaQueryWrapper<Category> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.orderByAsc(Category::getSort);
        categoryService.page(pageInfo);
        return R.success(pageInfo);
    }

    @DeleteMapping
    public R<String> delete(Long id) {
        LambdaQueryWrapper<Dish> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Dish::getCategoryId, id);
        Map<String, Object> dish = dishService.getMap(lambdaQueryWrapper);

        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Setmeal::getCategoryId, id);
        Map<String, Object> setmeal = setmealService.getMap(queryWrapper);
        if (dish != null || setmeal != null) {
            return R.error("不能删除");
        } else {
            LambdaQueryWrapper<Category> categoryLambdaQueryWrapper = new LambdaQueryWrapper<>();
            categoryLambdaQueryWrapper.eq(Category::getId, id);
            categoryService.remove(categoryLambdaQueryWrapper);
        }
        return R.success("删除成功");
    }

    @PutMapping
    public R<String> putCategory(@RequestBody Category category) {
        LambdaQueryWrapper<Category> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Category::getId, category.getId());
        categoryService.update(category, lambdaQueryWrapper);
        return R.success("修改成功");
    }

    @GetMapping("/list")
    public R<List<Category>> list(Category category) {
        LambdaQueryWrapper<Category> lambdaQueryWrapper = new LambdaQueryWrapper();
        lambdaQueryWrapper.eq(category != null, Category::getType, category.getType());
        lambdaQueryWrapper.orderByAsc(Category::getSort).orderByDesc(Category::getUpdateTime);
        List<Category> list = categoryService.list(lambdaQueryWrapper);
        return R.success(list);
    }
}
