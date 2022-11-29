package com.example.reggie_take_out.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.reggie_take_out.dto.DishDto;
import com.example.reggie_take_out.entity.Dish;
import com.example.reggie_take_out.entity.DishFlavor;
import com.example.reggie_take_out.mapper.DishMapper;
import com.example.reggie_take_out.service.DishFlavourService;
import com.example.reggie_take_out.service.DishService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @Author "Airceach"
 * @Date 2022/11/25 10:00
 * @Version 1.0
 */
@Service
public class DishServiceImpl extends ServiceImpl<DishMapper , Dish> implements DishService {

    @Autowired
    private DishFlavourService dishFlavourService;

    /**
     *  service 层处理相关操作
     * @param dishDto
     */
    @Override
    public void saveWithFlavor(DishDto dishDto){

        this.save(dishDto);

        List<DishFlavor> flavors = dishDto.getFlavors();

        //java 新特性 stream
//        flavors = flavors.stream().map((dishFlavor) -> {
//                dishFlavor.setDishId(dishDto.getId());
//                return dishFlavor;
//        }).collect(Collectors.toList());

        for (DishFlavor flavor : flavors) {
            flavor.setDishId(dishDto.getId());
            dishFlavourService.save(flavor);
        }
    }

    @Override
    public DishDto getByIdWithFlavor(Long id) {
        //查询当前菜品基本信息
        Dish dish = this.getById(id);
        DishDto dishDto = new DishDto();
        BeanUtils.copyProperties(dish , dishDto);
        //查询当前菜品口味信息
        LambdaQueryWrapper<DishFlavor> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(DishFlavor::getDishId , dish.getId());
        List<DishFlavor> list = dishFlavourService.list(lambdaQueryWrapper);
        dishDto.setFlavors(list);
        return dishDto;
    }

    @Override
    public void updateByIdWithFlavor(DishDto dishDto) {

        Dish dish = dishDto;
        this.updateById(dish);
        LambdaQueryWrapper<DishFlavor> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(DishFlavor::getDishId , dish.getId());
        dishFlavourService.remove(lambdaQueryWrapper);

        List<DishFlavor> flavors = dishDto.getFlavors();

        flavors = flavors.stream().map((dishFlavor) -> {
                dishFlavor.setDishId(dishDto.getId());
                return dishFlavor;
        }).collect(Collectors.toList());

        dishFlavourService.saveBatch(flavors);
    }
}
