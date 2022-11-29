package com.example.reggie_take_out.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.reggie_take_out.common.Result;
import com.example.reggie_take_out.dto.OrdersDto;
import com.example.reggie_take_out.entity.OrderDetail;
import com.example.reggie_take_out.entity.Orders;
import com.example.reggie_take_out.entity.User;
import com.example.reggie_take_out.service.AddressBookService;
import com.example.reggie_take_out.service.OrderDetailService;
import com.example.reggie_take_out.service.OrdersService;
import com.example.reggie_take_out.service.UserService;
import com.example.reggie_take_out.utils.ThreadLocalUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author "Airceach"
 * @Date 2022/11/28 19:42
 * @Version 1.0
 */
@RestController
@RequestMapping("/order")
public class OrdersController {

    @Autowired
    private OrdersService ordersService;
    @Autowired
    private UserService userService;
    @Autowired
    private OrderDetailService orderDetailService;

    @PostMapping("/submit")
    public Result<String> submit(@RequestBody Orders order){

        ordersService.submit(order);
        return Result.success("订单提交成功");
    }

    @GetMapping("/userPage")
    public Result<Page<OrdersDto>> page(int page , int pageSize){
        Page<Orders> ordersPage = new Page<>();
        LambdaQueryWrapper<Orders> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Orders::getUserId , ThreadLocalUtil.getCurrentId());
        ordersService.page(ordersPage , queryWrapper);
        Page<OrdersDto> ordersDtoPage = new Page<>();
        BeanUtils.copyProperties(ordersPage , ordersDtoPage , "records");
        List<Orders> records = ordersPage.getRecords();

        List<OrdersDto> ordersDtos = records.stream().map( (item) ->{
            OrdersDto ordersDto = new OrdersDto();
            BeanUtils.copyProperties(item , ordersDto);
            User user = userService.getById(ThreadLocalUtil.getCurrentId());
            LambdaQueryWrapper<OrderDetail> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(OrderDetail::getOrderId , item.getId());
            List<OrderDetail> list = orderDetailService.list(lambdaQueryWrapper);
            ordersDto.setUserName(user.getName());
            ordersDto.setPhone(user.getPhone());
            ordersDto.setOrderDetails(list);
            return ordersDto;
        }).collect(Collectors.toList());

        ordersDtoPage.setRecords(ordersDtos);
        return Result.success(ordersDtoPage);

    }

}
