package com.example.reggie_take_out.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.reggie_take_out.entity.ShoppingCart;
import com.example.reggie_take_out.mapper.ShoppingCartMapper;
import com.example.reggie_take_out.service.ShoppingCartService;
import org.springframework.stereotype.Service;

/**
 * @Author "Airceach"
 * @Date 2022/11/28 14:55
 * @Version 1.0
 */
@Service
public class ShoppingCartServiceImpl extends ServiceImpl<ShoppingCartMapper , ShoppingCart> implements ShoppingCartService {
}
