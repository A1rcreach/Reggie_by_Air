package com.example.reggie_take_out.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.reggie_take_out.entity.Category;
import com.example.reggie_take_out.entity.Dish;
import com.example.reggie_take_out.entity.Setmeal;
import com.example.reggie_take_out.exceptions.CustomException;
import com.example.reggie_take_out.mapper.CategoryMapper;
import com.example.reggie_take_out.service.CategoryService;
import com.example.reggie_take_out.service.DishService;
import com.example.reggie_take_out.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author "Airceach"
 * @Date 2022/11/25 9:05
 * @Version 1.0
 */
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {
    @Autowired
    DishService dishService;
    @Autowired
    SetmealService setmealService;



    @Override
    public void remove(Long id) {
        //查询当前分类是否关联菜品
        LambdaQueryWrapper<Dish> dishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        //添加查询条件 ==> 根据分类 id 查询
        dishLambdaQueryWrapper.eq(Dish::getCategoryId , id);
        //查询符合条件的 dish 数量
        long countDish = dishService.count(dishLambdaQueryWrapper);
        if (countDish > 0){
            throw new CustomException("当前分类包含菜品 == 无法删除");
        }
        //查询当前分类是否关联菜单
        LambdaQueryWrapper<Setmeal> setmealLambdaQueryWrapper = new LambdaQueryWrapper<>();
        //添加查询条件
        setmealLambdaQueryWrapper.eq(Setmeal::getCategoryId , id);
        long countSetmeal = setmealService.count(setmealLambdaQueryWrapper);
        if (countSetmeal > 0){
            throw new CustomException("当前分类包含菜单 == 无法删除");
        }
        super.removeById(id);
    }
}
