package com.example.reggie_take_out.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.reggie_take_out.common.Result;
import com.example.reggie_take_out.dto.SetmealDto;
import com.example.reggie_take_out.entity.Category;
import com.example.reggie_take_out.entity.Dish;
import com.example.reggie_take_out.entity.Setmeal;
import com.example.reggie_take_out.entity.SetmealDish;
import com.example.reggie_take_out.service.CategoryService;
import com.example.reggie_take_out.service.DishService;
import com.example.reggie_take_out.service.SetmealDishService;
import com.example.reggie_take_out.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author "Airceach"
 * @Date 2022/11/27 9:18
 * @Version 1.0
 */
@Slf4j
@RestController
@RequestMapping("/setmeal")
public class SetmealController {

    @Autowired
    private SetmealService setmealService;

    @Autowired
    private SetmealDishService setmealDishService;

    @Autowired
    private DishService dishService;
    @Autowired
    private CategoryService categoryService;

    /**
     * 分页查询 套餐
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    public Result<Page> page(int page , int pageSize , String name){

        //构造分页构造器
        Page<Setmeal> pageInfo = new Page(page , pageSize);
        Page<SetmealDto> dtoPage = new Page<>(page , pageSize);
        //构造条件构造器
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        //过滤条件
        queryWrapper.like(name != null , Setmeal::getName , name);
        //排序条件
        queryWrapper.orderByAsc(Setmeal :: getUpdateTime);
        //执行查询
        setmealService.page(pageInfo, queryWrapper);

        BeanUtils.copyProperties(pageInfo , dtoPage , "records");
        List<Setmeal> records = pageInfo.getRecords();

        List<SetmealDto> list = records.stream().map((item) -> {
            SetmealDto setmealDto = new SetmealDto();
            BeanUtils.copyProperties(item, setmealDto);
            Category category = categoryService.getById(item.getCategoryId());
            if (category != null){
                String categoryName = category.getName();
                setmealDto.setCategoryName(categoryName);
            }
            return setmealDto;
        }).collect(Collectors.toList());
        dtoPage.setRecords(list);

        return Result.success(dtoPage);
    }

    /**
     * 集合传参 ==> @RequestParam
     * @param ids
     * @return
     */
    @DeleteMapping
    public Result<String> delete(@RequestParam List<Long> ids){

        setmealService.removeWithStatus(ids);
        return Result.success("套餐删除成功");
    }

    /**
     * 套餐添加
     * @param setmealDto
     * @return
     */
    @PostMapping
    public Result<String> save(@RequestBody SetmealDto setmealDto){

        setmealService.saveWithSetmealDish(setmealDto);
        return Result.success("套餐添加成功");
    }

    @GetMapping("{id}")
    public Result<SetmealDto> getById(@PathVariable Long id){
        SetmealDto setmealDto = setmealService.getByIdWithSetmealDish(id);
        return Result.success(setmealDto);
    }

    @PutMapping
    public Result<String> update(@RequestBody SetmealDto setmealDto){
        log.info("修改==============");
        setmealService.updateByIdWithSetmealDish(setmealDto);
        return Result.success("susses");
    }

    @GetMapping("/list")
    public Result<List<Setmeal>> list( Setmeal setmeal){

        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(setmeal.getCategoryId() != null , Setmeal::getCategoryId , setmeal.getCategoryId());
        queryWrapper.eq(setmeal.getStatus() != null , Setmeal::getStatus , setmeal.getStatus());
        queryWrapper.orderByDesc(Setmeal::getUpdateTime);
        List<Setmeal> list = setmealService.list(queryWrapper);
        if (list == null){
            return Result.error("无相关菜品");
        }
        return Result.success(list);
    }

    @PostMapping("/status/{status}")
    public Result<String> status(@PathVariable int status , @RequestParam List<Long> ids){
        for (Long id : ids) {
            Setmeal setmeal = setmealService.getById(id);
            setmeal.setStatus(status == 0 ? 0 : 1);
            if (status == 1){
                LambdaQueryWrapper<SetmealDish> queryWrapper = new LambdaQueryWrapper<>();
                queryWrapper.eq(SetmealDish::getSetmealId , id);
                List<SetmealDish> list = setmealDishService.list(queryWrapper);
                if (list != null){
                    for (SetmealDish setmealDish : list) {
                        Long dishId = setmealDish.getDishId();
                        LambdaQueryWrapper<Dish> lambdaQueryWrapper = new LambdaQueryWrapper<>();
                        lambdaQueryWrapper.eq(Dish::getId , dishId);
                        List<Dish> dishes = dishService.list(lambdaQueryWrapper);
                        for (Dish dish : dishes) {
                            if (dish.getStatus() == 0){
                                return Result.error("套餐内菜品已禁售");
                            }
                        }
                    }
                }
            }
            setmealService.updateById(setmeal);
        }
        return Result.success("修改成功");
    }


}
