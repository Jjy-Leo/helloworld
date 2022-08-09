package cn.jjy.reggie.controller;

import cn.jjy.reggie.entity.Employee;
import cn.jjy.reggie.mapper.EmployeeMapper;
import cn.jjy.reggie.result.R;
import cn.jjy.reggie.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @program: reggie_take_out
 * @description:
 * @author: jjy
 * @create: 2022-08-10 07:15
 **/
@RestController
@RequestMapping("/employee")
public class EmployeeController {
    @Autowired
    private EmployeeService employeeService;

    @RequestMapping("/login")
    private R<Employee> login(HttpServletRequest request, @RequestBody Employee employee) {
        String password = DigestUtils.md5DigestAsHex(employee.getPassword().getBytes());
        return null;
    }
}
