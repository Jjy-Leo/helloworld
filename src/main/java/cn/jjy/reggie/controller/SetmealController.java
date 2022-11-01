package cn.jjy.reggie.controller;

import cn.jjy.reggie.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @program: reggie_take_out
 * @description:
 * @author: jjy
 * @create: 2022-11-02 06:44
 **/
@RestController
@RequestMapping("/Setmeal")
public class SetmealController {

    @Autowired
    private SetmealService setmealService;
}
