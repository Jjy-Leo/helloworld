package cn.jjy.reggie.service.impl;

import cn.jjy.reggie.entity.Dish;
import cn.jjy.reggie.mapper.DishMapper;
import cn.jjy.reggie.service.DishService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * @program: reggie_take_out
 * @description:
 * @author: jjy
 * @create: 2022-11-02 06:34
 **/
@Service
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {
}
