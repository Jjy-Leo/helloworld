package cn.jjy.reggie.mapper;

import cn.jjy.reggie.entity.Employee;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * @program: reggie_take_out
 * @description:
 * @author: jjy
 * @create: 2022-08-10 07:21
 **/
@Mapper
public interface EmployeeMapper extends BaseMapper<Employee> {
}
