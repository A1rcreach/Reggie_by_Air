package com.example.reggie_take_out.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.reggie_take_out.entity.Orders;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Author "Airceach"
 * @Date 2022/11/28 19:40
 * @Version 1.0
 */
@Mapper
public interface OrdersMapper extends BaseMapper<Orders> {
}
