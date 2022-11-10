package cn.jjy.reggie.service.impl;

import cn.jjy.reggie.dto.DishDto;
import cn.jjy.reggie.entity.Dish;
import cn.jjy.reggie.entity.DishFlavor;
import cn.jjy.reggie.mapper.DishMapper;
import cn.jjy.reggie.service.DishFlavorService;
import cn.jjy.reggie.service.DishService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @program: reggie_take_out
 * @description:
 * @author: jjy
 * @create: 2022-11-02 06:34
 **/
@Service
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {

    @Autowired
    private DishFlavorService dIshFlavorService;

    public void saveWithFlavor(DishDto dishDto) {
        this.save(dishDto);
        Long id = dishDto.getId();
        List<DishFlavor> flavors = dishDto.getFlavors();
        flavors.stream().map((item) -> {
            item.setDishId(id);
            return item;
        }).collect(Collectors.toList());
        dIshFlavorService.saveBatch(flavors);
    }
}
