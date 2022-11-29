package com.example.reggie_take_out.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.reggie_take_out.entity.OrderDetail;
import com.example.reggie_take_out.mapper.OrderDetailMapper;
import com.example.reggie_take_out.service.OrderDetailService;
import org.springframework.stereotype.Service;

/**
 * @Author "Airceach"
 * @Date 2022/11/28 19:55
 * @Version 1.0
 */
@Service
public class OrderDetailServiceImpl extends ServiceImpl<OrderDetailMapper , OrderDetail> implements OrderDetailService {
}
