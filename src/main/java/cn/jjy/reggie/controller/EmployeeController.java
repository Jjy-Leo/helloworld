package cn.jjy.reggie.controller;

import cn.jjy.reggie.common.BaseUtils;
import cn.jjy.reggie.entity.Employee;
import cn.jjy.reggie.common.R;
import cn.jjy.reggie.service.EmployeeService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

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

    @PostMapping("/logout")
    private R<String> logout(HttpServletRequest request) {
        request.getSession().removeAttribute("employee");
        return R.success("退出成功");
    }

    /**
     * @description: 新增员工
     * @author: jiajunyu
     * @date: 2022/8/23 20:49
     * @param: [request, employee]
     * @return: cn.jjy.reggie.commen.R<java.lang.String>
     **/
    @PostMapping
    public R<String> addEmployee(HttpServletRequest request, @RequestBody Employee employee) {
        employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));

//        employee.setCreateTime(LocalDateTime.now());
//        employee.setUpdateTime(LocalDateTime.now());
        Long empId = (Long) request.getSession().getAttribute("employee");
//        employee.setCreateUser(empId);
//        employee.setUpdateUser(empId);
        employeeService.save(employee);
        return R.success("新增员工成功");
    }

    @GetMapping("/page")
    public R<Page<Employee>> page(int page, int pageSize, String name) {
        //创建分页对象
        Page pageInfo = new Page(page,pageSize);
        //创建条件对象
        LambdaQueryWrapper<Employee> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.like(!StringUtils.isEmpty(name), Employee::getName, name);
        //调用service
        Page pages = employeeService.page(pageInfo, lambdaQueryWrapper);
        return R.success(pageInfo);

    }

    @PutMapping
    public R<String> update(HttpServletRequest request, @RequestBody Employee employee) {
//        employee.setUpdateTime(LocalDateTime.now());
        Long empId = (Long) request.getSession().getAttribute("employee");
//        employee.setUpdateUser(empId);
        employeeService.updateById(employee);
        return R.success("员工信息修改改成功");
    }

    @GetMapping("/{empId}")
    public R<Employee> getEmployeeById(HttpServletRequest request,@PathVariable Long empId) {
        Employee employee = employeeService.getById(empId);
        return R.success(employee);
    }
}
