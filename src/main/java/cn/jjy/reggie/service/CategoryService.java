package cn.jjy.reggie.service;


import cn.jjy.reggie.entity.Category;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface CategoryService extends IService<Category> {
}
