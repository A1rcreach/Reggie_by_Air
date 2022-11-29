package com.example.reggie_take_out.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.reggie_take_out.entity.*;
import com.example.reggie_take_out.exceptions.CustomException;
import com.example.reggie_take_out.mapper.OrdersMapper;
import com.example.reggie_take_out.service.*;
import com.example.reggie_take_out.utils.ThreadLocalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * @Author "Airceach"
 * @Date 2022/11/28 19:41
 * @Version 1.0
 */
@Service
public class OrderServiceImpl extends ServiceImpl<OrdersMapper , Orders> implements OrdersService {


    @Autowired
    UserService userService;

    @Autowired
    AddressBookService addressBookService;
    @Autowired
    private ShoppingCartService shoppingCartService;

    @Autowired
    private OrderDetailService orderDetailService;

    @Override
    public void submit(Orders order) {
        Long id = ThreadLocalUtil.getCurrentId();
        order.setUserId(id);
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId , id);
        List<ShoppingCart> list = shoppingCartService.list(queryWrapper);

        if (list == null){
            throw new CustomException("购物车为空 , 无法下单");
        }
        User user = userService.getById(id);
        AddressBook addressBook = addressBookService.getById(order.getAddressBookId());
        if (addressBook == null){
            throw new CustomException("地址信息错误");
        }
        long orderId = IdWorker.getId();

        AtomicInteger amount = new AtomicInteger(0);
        List<OrderDetail> orderDetails = list.stream().map( (item) -> {
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setOrderId(orderId);
            orderDetail.setNumber(item.getNumber());
            orderDetail.setDishFlavor(item.getDishFlavor());
            orderDetail.setDishId(item.getDishId());
            orderDetail.setSetmealId(item.getSetmealId());
            orderDetail.setAmount(item.getAmount());
            amount.addAndGet(item.getAmount().multiply(new BigDecimal(item.getNumber())).intValue());
            return orderDetail;
        }).collect(Collectors.toList());

        order.setId(orderId);
        order.setNumber(String.valueOf(orderId));
        order.setStatus(2);
        order.setAmount(new BigDecimal(amount.get()));
        order.setUserName(user.getName());
        order.setConsignee(addressBook.getConsignee());
        order.setOrderTime(LocalDateTime.now());
        order.setCheckoutTime(LocalDateTime.now());
        order.setAddress((addressBook.getProvinceName() == null ? "" : addressBook.getProvinceName())
                + (addressBook.getCityName() == null ? "" : addressBook.getCityName())
                + (addressBook.getDistrictName() == null ? "" : addressBook.getDistrictName())
                + (addressBook.getDetail() == null ? "" : addressBook.getDetail()));

        this.save(order);
        orderDetailService.saveBatch(orderDetails);
        shoppingCartService.remove(queryWrapper);
    }
}
