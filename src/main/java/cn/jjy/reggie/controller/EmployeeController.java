package cn.jjy.reggie.controller;

import cn.jjy.reggie.entity.Employee;
import cn.jjy.reggie.mapper.EmployeeMapper;
import cn.jjy.reggie.result.R;
import cn.jjy.reggie.service.EmployeeService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.PostMapping;
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

    @PostMapping("/login")
    private R<Employee> login(HttpServletRequest request, @RequestBody Employee employee) {
        String password = DigestUtils.md5DigestAsHex(employee.getPassword().getBytes());
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Employee::getUsername, employee.getUsername());
        Employee emp = employeeService.getOne(queryWrapper);

        if (emp == null) {
            return R.error("登陆错误");
        }

        if (!emp.getPassword().equals(password)) {
            return R.error("登陆错误");
        }

        if (emp.getStatus() == 0) {
            return R.error("账号已禁用");
        }
        request.getSession().setAttribute("employee", emp.getId());
        return R.success(emp);
    }
}
