package com.example.reggie_take_out.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.reggie_take_out.dto.DishDto;
import com.example.reggie_take_out.entity.Dish;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Author "Airceach"
 * @Date 2022/11/25 9:59
 * @Version 1.0
 */
@Transactional
public interface DishService extends IService<Dish> {

    public void saveWithFlavor(DishDto dishDto);

    public DishDto getByIdWithFlavor(Long id);

    public void updateByIdWithFlavor(DishDto dishDto);
}
