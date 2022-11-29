package com.example.reggie_take_out.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.reggie_take_out.entity.User;
import com.example.reggie_take_out.mapper.UserMapper;
import com.example.reggie_take_out.service.UserService;
import org.springframework.stereotype.Service;

/**
 * @Author "Airceach"
 * @Date 2022/11/27 15:12
 * @Version 1.0
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper , User> implements UserService {
}
