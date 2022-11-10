package cn.jjy.reggie.service.impl;

import cn.jjy.reggie.common.R;
import cn.jjy.reggie.entity.Setmeal;
import cn.jjy.reggie.entity.SetmealDish;
import cn.jjy.reggie.mapper.SetmealMapper;
import cn.jjy.reggie.service.SetmealDishService;
import cn.jjy.reggie.service.SetmealService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @program: reggie_take_out
 * @description:
 * @author: jjy
 * @create: 2022-11-02 06:43
 **/
@Service
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal> implements SetmealService {


    @Autowired
    private SetmealDishService setmealDishService;


    @Override
    @Transactional
    public R<String> deleteWithSetmealDish(List<Long> setmealIdlist) {
        //没有停售的套餐不能删除
        LambdaQueryWrapper<Setmeal> setmealLambdaQueryWrapper = new LambdaQueryWrapper<>();
        setmealLambdaQueryWrapper.in(Setmeal::getId);
        setmealLambdaQueryWrapper.eq(Setmeal::getStatus, 1);
        int count = this.count(setmealLambdaQueryWrapper);

        if (count>0 ) {
            return R.error("删除套餐失败，该套餐没有停售，不能删除！");
        }
        //删除套餐的同时要删除套餐菜品关系表
        this.removeByIds(setmealIdlist);
        LambdaQueryWrapper<SetmealDish> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.in(SetmealDish::getSetmealId, setmealIdlist);
        setmealDishService.remove(lambdaQueryWrapper);
        return R.success("删除成功");
    }
}
