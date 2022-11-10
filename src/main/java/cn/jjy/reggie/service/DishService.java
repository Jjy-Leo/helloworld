package cn.jjy.reggie.service;


import cn.jjy.reggie.dto.DishDto;
import cn.jjy.reggie.entity.Dish;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.transaction.annotation.Transactional;

public interface DishService extends IService<Dish> {
    void saveWithFlavor(DishDto dishDto);
}
