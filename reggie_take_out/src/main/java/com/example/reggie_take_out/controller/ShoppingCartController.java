package com.example.reggie_take_out.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.reggie_take_out.common.Result;
import com.example.reggie_take_out.entity.Dish;
import com.example.reggie_take_out.entity.Setmeal;
import com.example.reggie_take_out.entity.ShoppingCart;
import com.example.reggie_take_out.service.ShoppingCartService;
import com.example.reggie_take_out.utils.ThreadLocalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author "Airceach"
 * @Date 2022/11/28 14:56
 * @Version 1.0
 */
@RestController
@RequestMapping("/shoppingCart")
public class ShoppingCartController {

    @Autowired
    private ShoppingCartService shoppingCartService;

    @PostMapping("/add")
    public Result<ShoppingCart> add(@RequestBody ShoppingCart shoppingCart){

        Long id = ThreadLocalUtil.getCurrentId();
        shoppingCart.setUserId(id);
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId , id);
        if (shoppingCart.getDishId() != null) {
            queryWrapper.eq(ShoppingCart::getDishId , shoppingCart.getDishId());

        } else {
            queryWrapper.eq(ShoppingCart::getSetmealId , shoppingCart.getSetmealId());
        }
        queryWrapper.eq(shoppingCart.getDishFlavor() != null , ShoppingCart::getDishFlavor , shoppingCart.getDishFlavor());
        ShoppingCart cart = shoppingCartService.getOne(queryWrapper);

        if (cart == null){
            shoppingCart.setNumber(1);
            shoppingCartService.save(shoppingCart);
            cart = shoppingCart;
        } else {
            Integer number = cart.getNumber();
            cart.setNumber(number + 1);
            shoppingCartService.updateById(cart);
        }

        return Result.success(cart);
    }

    @GetMapping("/list")
    public Result<List<ShoppingCart>> list(){
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId , ThreadLocalUtil.getCurrentId());
        queryWrapper.orderByDesc(ShoppingCart::getCreateTime);
        List<ShoppingCart> list = shoppingCartService.list(queryWrapper);
        return Result.success(list);
    }

    @PostMapping("/sub")
    public Result<ShoppingCart> sub(@RequestBody ShoppingCart shoppingCart){
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId , ThreadLocalUtil.getCurrentId());
        if (shoppingCart.getDishId() != null) {
            queryWrapper.eq(ShoppingCart::getDishId , shoppingCart.getDishId());

        } else {
            queryWrapper.eq(ShoppingCart::getSetmealId , shoppingCart.getSetmealId());
        }

        queryWrapper.orderByDesc(ShoppingCart::getCreateTime);
        List<ShoppingCart> list = shoppingCartService.list(queryWrapper);
        ShoppingCart cart = list.get(0);
        if (cart.getNumber() > 1){
            cart.setNumber(cart.getNumber() - 1);
            shoppingCartService.updateById(cart);
        } else {
            cart.setNumber(0);
            shoppingCartService.removeById(cart.getId());
        }

        return Result.success(cart);
    }

    @DeleteMapping("/clean")
    public Result<String> clean(){
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId , ThreadLocalUtil.getCurrentId());
        shoppingCartService.remove(queryWrapper);
        return Result.success("购物车清空成功");
    }
}
