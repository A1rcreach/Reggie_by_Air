package com.example.reggie_take_out.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.reggie_take_out.entity.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Author "Airceach"
 * @Date 2022/11/27 15:11
 * @Version 1.0
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
}
