package com.example.reggie_take_out.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.reggie_take_out.entity.AddressBook;
import com.example.reggie_take_out.mapper.AddressBookMapper;
import com.example.reggie_take_out.service.AddressBookService;
import org.springframework.stereotype.Service;

/**
 * @Author "Airceach"
 * @Date 2022/11/27 17:21
 * @Version 1.0
 */
@Service
public class AddressBookServiceImpl extends ServiceImpl<AddressBookMapper , AddressBook> implements AddressBookService {
}
