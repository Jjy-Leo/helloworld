package cn.jjy.reggie.controller;

import cn.jjy.reggie.service.DishFlavorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @program: reggie_take_out
 * @description:
 * @author: jjy
 * @create: 2022-11-09 21:13
 **/
@RestController
@RequestMapping("/DishFlavor")
public class DishFlavorController {

    @Autowired
    private DishFlavorService dIshFlavorService;

}
