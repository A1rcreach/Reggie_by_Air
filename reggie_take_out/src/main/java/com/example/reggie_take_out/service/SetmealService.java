package com.example.reggie_take_out.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.reggie_take_out.dto.DishDto;
import com.example.reggie_take_out.dto.SetmealDto;
import com.example.reggie_take_out.entity.Setmeal;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Author "Airceach"
 * @Date 2022/11/25 10:03
 * @Version 1.0
 */
@Transactional
public interface SetmealService extends IService<Setmeal> {

    public void saveWithSetmealDish(SetmealDto setmealDto);

    public void removeWithStatus(List<Long> id);
    public SetmealDto getByIdWithSetmealDish(Long id);

    public void updateByIdWithSetmealDish(SetmealDto setmealDto);
}
