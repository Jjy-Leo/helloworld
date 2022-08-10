package cn.jjy.reggie.service;

import cn.jjy.reggie.entity.Employee;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface EmployeeService extends IService<Employee> {
}
