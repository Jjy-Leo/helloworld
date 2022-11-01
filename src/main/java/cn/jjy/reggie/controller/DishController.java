package cn.jjy.reggie.controller;

import cn.jjy.reggie.entity.Dish;
import cn.jjy.reggie.entity.Employee;
import cn.jjy.reggie.service.DishService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}
