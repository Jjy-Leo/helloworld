package cn.jjy.reggie.service;

import cn.jjy.reggie.entity.Setmeal;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.transaction.annotation.Transactional;

/**
 * @program: reggie_take_out
 * @description:
 * @author: jjy
 * @create: 2022-11-02 06:42
 **/
@Transactional
public interface SetmealService extends IService<Setmeal> {
}
