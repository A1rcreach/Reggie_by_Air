package com.example.reggie_take_out.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.reggie_take_out.entity.Orders;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Author "Airceach"
 * @Date 2022/11/28 19:41
 * @Version 1.0
 */
@Transactional
public interface OrdersService extends IService<Orders> {

    public void submit(Orders order);
}
