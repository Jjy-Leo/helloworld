package cn.jjy.reggie.controller;

import cn.jjy.reggie.common.R;
import cn.jjy.reggie.entity.Category;
import cn.jjy.reggie.service.CategoryService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    private DishController dishController;

    @Autowired
    private SetmealController setmealController;

    @PostMapping
    public R<String> save(@RequestBody Category category){
        categoryService.save(category);
        log.info("新增分类成功");
        return R.success("新增分类成功");
    }

    @GetMapping("/page")
    private R<Page> page(int page,int pageSize){

        Page pageInfo = new Page();
        LambdaQueryWrapper<Category> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        categoryService.page(pageInfo);
        return R.success(pageInfo);
    }

    @DeleteMapping
    public R<String> delete(Long id) {

    }
}
