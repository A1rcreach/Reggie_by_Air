package com.example.reggie_take_out.dto;


import com.example.reggie_take_out.entity.OrderDetail;
import com.example.reggie_take_out.entity.Orders;
import lombok.Data;
import java.util.List;

@Data
public class OrdersDto extends Orders {


    private List<OrderDetail> orderDetails;
	
}
