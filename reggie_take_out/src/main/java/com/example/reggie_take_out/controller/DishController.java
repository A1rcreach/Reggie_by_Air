package com.example.reggie_take_out.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.reggie_take_out.common.Result;
import com.example.reggie_take_out.dto.DishDto;
import com.example.reggie_take_out.entity.*;
import com.example.reggie_take_out.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author "Airceach"
 * @Date 2022/11/25 10:53
 * @Version 1.0
 */
@Slf4j
@RestController
@RequestMapping("/dish")
public class DishController {

    @Autowired
    private DishService dishService;

    @Autowired
    private DishFlavourService dishFlavourService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private SetmealService setmealService;
    @Autowired
    private SetmealDishService setmealDishService;

    @GetMapping("/page")
    public Result<Page> page(int page , int pageSize , String name){

        //构造分页构造器
        Page<Dish> pageInfo = new Page(page , pageSize);
        Page<DishDto> dtoPage = new Page<>(page , pageSize);
        //构造条件构造器
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        //过滤条件
        queryWrapper.like(name != null , Dish::getName , name);
        //排序条件
        queryWrapper.orderByAsc(Dish :: getSort);
        //执行查询
        dishService.page(pageInfo, queryWrapper);
        //对象拷贝
        BeanUtils.copyProperties(pageInfo , dtoPage , "records");       //将 pageInfo 拷贝到 dtoPage , 忽视 records 属性
        //拷贝 records ==> 将 records 进行处理(添加 categoryName ) ==> 复制给 dtoPage
        List<Dish> records = pageInfo.getRecords();
        List<DishDto> list = records.stream().map((item) -> {
            DishDto dishDto = new DishDto();
            BeanUtils.copyProperties(item , dishDto);
            Long categoryId = item.getCategoryId();
            Category category = categoryService.getById(categoryId);
            String categoryName = category.getName();
            dishDto.setCategoryName(categoryName);
            return dishDto;
        }).collect(Collectors.toList());

        dtoPage.setRecords(list);
        return Result.success(dtoPage);

    }

    @GetMapping("/{id}")
    public Result<DishDto> getById(@PathVariable Long id){
        DishDto dishDto = dishService.getByIdWithFlavor(id);
        if (dishDto == null){
            return Result.error("菜品信息加载失败");
        }
        return Result.success(dishDto);
    }

    @DeleteMapping
    public Result<String> delete(List<Long> ids){
        for (Long i : ids) {
            dishService.removeById(i);

        }
        return Result.success("删除成功");

    }

    /**
     * 添加菜品
     * 传输的 Json 包含 flavors , Dish 类内不包含该属性 , 因此创建 DTO ,接受前端数据.
     * DTO : 面向 UI 设计
     * @param dishDto
     * @return
     */
    @PostMapping
    public Result<String> save(@RequestBody DishDto dishDto){

        dishService.saveWithFlavor(dishDto);

        return Result.success("添加菜品成功");
    }

    @PutMapping
    public Result<String> update(@RequestBody DishDto dishDto){

        dishService.updateByIdWithFlavor(dishDto);
        return Result.success("修改成功");

    }

    @PostMapping("/status/{status}")
    public Result<String> status(@PathVariable Integer status ,@RequestParam List<Long> ids){
        for (Long id : ids) {
            Dish dish = dishService.getById(id);
            dish.setStatus(status == 0 ? 0 : 1);
            dishService.updateById(dish);
            if (status == 0){
                LambdaQueryWrapper<SetmealDish> queryWrapper = new LambdaQueryWrapper<>();
                queryWrapper.eq(SetmealDish::getDishId , id);
                List<SetmealDish> list = setmealDishService.list(queryWrapper);
                if (list != null){
                    for (SetmealDish setmealDish : list) {
                        LambdaQueryWrapper<Setmeal> lambdaQueryWrapper = new LambdaQueryWrapper<>();
                        lambdaQueryWrapper.eq(Setmeal::getId , setmealDish.getSetmealId());
                        List<Setmeal> setmeals = setmealService.list(lambdaQueryWrapper);
                        for (Setmeal setmeal : setmeals) {
                            Setmeal s = setmealService.getById(setmeal.getId());
                            s.setStatus(0);
                            setmealService.updateById(s);
                        }
                    }
                }
            }
        }
        return Result.success("修改成功");
    }

    @GetMapping("/list")
    public Result<List<DishDto>> list(Dish dish){

        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(dish.getCategoryId() != null , Dish::getCategoryId , dish.getCategoryId());
        queryWrapper.eq(Dish::getStatus , 1);
        queryWrapper.orderByAsc(Dish::getSort).orderByDesc(Dish::getUpdateTime);
        List<Dish> list = dishService.list(queryWrapper);
        List<DishDto> dtoList = new ArrayList<>();
        dtoList = list.stream().map((item) -> {
            DishDto dishDto= new DishDto();
            BeanUtils.copyProperties(item , dishDto);

            Category category = categoryService.getById(item.getCategoryId());
            if (category != null){
                dishDto.setCategoryName(category.getName());
            }
            LambdaQueryWrapper<DishFlavor> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(DishFlavor::getDishId , item.getId());
            List<DishFlavor> flavors = dishFlavourService.list(lambdaQueryWrapper);
            dishDto.setFlavors(flavors);
            return dishDto;
        }).collect(Collectors.toList());
        return Result.success(dtoList);
    }


}
