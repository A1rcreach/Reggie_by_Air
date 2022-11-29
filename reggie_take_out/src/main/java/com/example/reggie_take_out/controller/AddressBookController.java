package com.example.reggie_take_out.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.example.reggie_take_out.common.Result;
import com.example.reggie_take_out.entity.AddressBook;
import com.example.reggie_take_out.service.AddressBookService;
import com.example.reggie_take_out.utils.ThreadLocalUtil;
import com.sun.prism.impl.BaseContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author "Airceach"
 * @Date 2022/11/27 17:22
 * @Version 1.0
 */
@RestController
@RequestMapping("/addressBook")
public class AddressBookController {

    @Autowired
    private AddressBookService addressBookService;

    @PostMapping
    public Result<AddressBook> save(@RequestBody AddressBook addressBook){
        addressBook.setUserId(ThreadLocalUtil.getCurrentId());
        addressBookService.save(addressBook);
        return Result.success(addressBook);
    }

    @GetMapping("{id}")
    public Result<AddressBook> getById(@PathVariable Long id){

        LambdaQueryWrapper<AddressBook> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(AddressBook::getId , id);
        AddressBook addressBook = addressBookService.getOne(queryWrapper);
        return Result.success(addressBook);
    }

    @PutMapping
    public Result<String> update(@RequestBody AddressBook addressBook){

        addressBookService.updateById(addressBook);
        return Result.success("地址修改成功");
    }

    @DeleteMapping
    public Result<String> delete(@RequestParam List<Long> ids){
        for (Long id : ids) {
            addressBookService.removeById(id);
        }
        return Result.success("地址删除成功");
    }

    @PutMapping("/default")
    public Result<AddressBook> setDefault(@RequestBody AddressBook addressBook){

        //该用户下所有地址默认值 为 0
        LambdaUpdateWrapper<AddressBook> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(AddressBook::getUserId , ThreadLocalUtil.getCurrentId());
        updateWrapper.set(AddressBook::getIsDefault , 0);
        addressBookService.update(updateWrapper);
        addressBook.setIsDefault(1);
        //当前地址默认值 为 1
        addressBookService.updateById(addressBook);
        return Result.success(addressBook);
    }

    @GetMapping("/default")
    public Result<AddressBook> getDefault(){
        LambdaQueryWrapper<AddressBook> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(AddressBook::getUserId , ThreadLocalUtil.getCurrentId());
        queryWrapper.eq(AddressBook::getIsDefault , 1);
        AddressBook addressBook = addressBookService.getOne(queryWrapper);
        if (addressBook == null){
            return Result.error("未设置默认地址");
        }
        return Result.success(addressBook);
    }

    @GetMapping("/list")
    public Result<List<AddressBook>> list(AddressBook addressBook){
        addressBook.setUserId(ThreadLocalUtil.getCurrentId());
        LambdaQueryWrapper<AddressBook> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(addressBook.getUserId() != null , AddressBook::getUserId , addressBook.getUserId());

        queryWrapper.orderByDesc(AddressBook::getIsDefault) .orderByDesc(AddressBook::getUpdateTime);
        List<AddressBook> list = addressBookService.list(queryWrapper);
        return Result.success(list);
    }
}
