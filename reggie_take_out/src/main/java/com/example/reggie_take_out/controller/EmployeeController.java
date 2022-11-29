package com.example.reggie_take_out.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.injector.methods.Insert;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.reggie_take_out.common.Result;
import com.example.reggie_take_out.entity.Employee;
import com.example.reggie_take_out.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;

import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;

/**
 * @Author "Airceach"
 * @Date 2022/11/23 10:07
 * @Version 1.0
 */
@Slf4j
@RestController
@RequestMapping("/employee")
public class EmployeeController {
    @Autowired
    private EmployeeService employeeService;

    /**
     * 员工登录
     * @param request
     * @param employee
     * @return
     */
    @PostMapping("/login")
    public Result<Employee> login(HttpServletRequest request , @RequestBody Employee employee){
        // MD5 加密
        String password = employee.getPassword();
        password = DigestUtils.md5DigestAsHex(password.getBytes());    // DigestUtils 工具类 进行MD5加密
        // 查询数据库
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Employee::getUsername,employee.getUsername());      // eq ==> 等值查询 equals
        Employee emp = employeeService.getOne(queryWrapper);
        // 登陆判定
        if (emp == null){
            return Result.error("账号错误");
        }
        if (!emp.getPassword().equals(password)){
            return Result.error("密码错误");
        }
        if (emp.getStatus() == 0){
            return  Result.error("账号已禁用");
        }
        //获取 session
        request.getSession().setAttribute("employee",emp.getId());
        return Result.success(emp);
    }

    /**
     * 员工退出
     * @param request
     * @return
     */
    @PostMapping("/logout")
    public Result<String> logout(HttpServletRequest request){
        //清除用户 session
        request.getSession().removeAttribute("employee");
        return Result.success("退出成功");
    }

    /**
     * 添加员工
     * @param employee
     * @return
     */
    @PostMapping
    public Result<String> save(HttpServletRequest request , @RequestBody Employee employee){
        //设置默认密码(MD5加密处理)
        employee.setPassword(DigestUtils.md5DigestAsHex("000000".getBytes()));
        //设置创建时间 更新时间 操作用户
//        employee.setCreateTime(LocalDateTime.now());
//        employee.setUpdateTime(LocalDateTime.now());
//        employee.setCreateUser((Long)request.getSession().getAttribute("employee"));
//        employee.setUpdateUser((Long)request.getSession().getAttribute("employee"));
        //添加员工
        employeeService.save(employee);
        return Result.success("添加员工成功");
    }

    /**
     * 员工信息分页查询
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    public Result<Page> page(int page , int pageSize , String name){
        log.info("page = {}  pageSize = {}  name = {}" , page , pageSize , name);

        //构造分页构造器
        Page<Employee> pageInfo = new Page(page , pageSize);
        //构造条件构造器
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        //过滤条件
        queryWrapper.like(StringUtils.isNotEmpty(name) , Employee::getName , name);
        //排序条件
        queryWrapper.orderByAsc(Employee::getUsername);
        //执行查询
        employeeService.page(pageInfo , queryWrapper);

        return Result.success(pageInfo);
    }

    /**
     * update
     * @param employee 更改
     * @return
     */
    @PutMapping
    public Result<String> update(HttpServletRequest request , @RequestBody Employee employee){
        log.info(employee.toString());
        employee.setUpdateUser((Long) request.getSession().getAttribute("employee"));
        employee.setUpdateTime(LocalDateTime.now());
        employeeService.updateById(employee);
        return Result.success("员工信息修改成功");
    }

    /**
     * 获取单位员工详细信息
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public Result<Employee> getById(@PathVariable Long id){
        Employee emp = employeeService.getById(id);
        if (emp == null){
            return Result.error("员工信息加载失败");
        }
        return Result.success(emp);
    }

}
