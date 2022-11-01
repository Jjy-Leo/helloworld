package cn.jjy.reggie.service.impl;

import cn.jjy.reggie.entity.Employee;
import cn.jjy.reggie.mapper.EmployeeMapper;
import cn.jjy.reggie.service.EmployeeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @program: reggie_take_out
 * @description:
 * @author: jjy
 * @create: 2022-08-10 07:21
 **/
@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements EmployeeService {

}
