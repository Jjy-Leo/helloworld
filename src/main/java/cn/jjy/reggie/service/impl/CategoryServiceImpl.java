package cn.jjy.reggie.service.impl;

import cn.jjy.reggie.entity.Category;
import cn.jjy.reggie.mapper.CategoryMapper;
import cn.jjy.reggie.service.CategoryService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * @program: reggie_take_out
 * @description:
 * @author: jjy
 * @create: 2022-11-02 05:52
 **/
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {
}
