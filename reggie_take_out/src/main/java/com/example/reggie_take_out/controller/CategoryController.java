package com.example.reggie_take_out.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.reggie_take_out.common.Result;
import com.example.reggie_take_out.entity.Category;
import com.example.reggie_take_out.entity.Employee;
import com.example.reggie_take_out.service.CategoryService;
import com.example.reggie_take_out.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;
import sun.plugin2.main.server.ResultID;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @Author "Airceach"
 * @Date 2022/11/23 10:07
 * @Version 1.0
 */
@Slf4j
@RestController
@RequestMapping("/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    /**
     * 添加菜品分类
     * @param category
     * @return
     */
    @PostMapping
    public Result<String> save(HttpServletRequest request , @RequestBody Category category){

        categoryService.save(category);
        return Result.success("新增分类成功");
    }

    /**
     * 菜品信息分页查询
     * @param page
     * @param pageSize
     * @return
     */
    @GetMapping("/page")
    public Result<Page> page(int page , int pageSize){

        //构造分页构造器
        Page<Category> pageInfo = new Page(page , pageSize);
        //构造条件构造器
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        //排序条件
        queryWrapper.orderByAsc(Category::getSort);
        //执行查询
        categoryService.page(pageInfo , queryWrapper);
        return Result.success(pageInfo);
    }

    @DeleteMapping
    public Result<String> delete(Long id){
        categoryService.remove(id);
        return Result.success("删除成功");
    }

    @PutMapping
    public Result<String> update(@RequestBody Category category){
        categoryService.updateById(category);
        return Result.success("修改分类信息成功");
    }

    @GetMapping("/list")
    public Result<List<Category>> list(Category category){
        // 条件构造器
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(category.getType() != null , Category::getType , category.getType());
        queryWrapper.orderByAsc(Category::getSort).orderByDesc(Category::getUpdateTime);

        List<Category> list = categoryService.list(queryWrapper);
        return Result.success(list);
    }


}
