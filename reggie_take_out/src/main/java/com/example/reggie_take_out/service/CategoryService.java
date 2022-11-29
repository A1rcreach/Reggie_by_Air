package com.example.reggie_take_out.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.reggie_take_out.entity.Category;

/**
 * @Author "Airceach"
 * @Date 2022/11/25 9:04
 * @Version 1.0
 */
public interface CategoryService extends IService<Category> {

    public void remove(Long id);
}
