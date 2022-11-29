package com.example.reggie_take_out.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.reggie_take_out.entity.Employee;
import com.example.reggie_take_out.mapper.EmployeeMapper;
import com.example.reggie_take_out.service.EmployeeService;
import org.springframework.stereotype.Service;

/**
 * @Author "Airceach"
 * @Date 2022/11/23 10:05
 * @Version 1.0
 */
@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements EmployeeService {
}
