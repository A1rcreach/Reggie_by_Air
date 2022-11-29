package com.example.reggie_take_out.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.reggie_take_out.entity.Category;
import com.example.reggie_take_out.entity.Employee;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Author "Airceach"
 * @Date 2022/11/23 10:02
 * @Version 1.0
 */
@Mapper
public interface CategoryMapper extends BaseMapper<Category> {
}
