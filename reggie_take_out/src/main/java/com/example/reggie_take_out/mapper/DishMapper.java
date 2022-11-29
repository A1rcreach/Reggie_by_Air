package com.example.reggie_take_out.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.reggie_take_out.entity.Dish;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Author "Airceach"
 * @Date 2022/11/25 9:58
 * @Version 1.0
 */
@Mapper
public interface DishMapper extends BaseMapper<Dish> {
}
