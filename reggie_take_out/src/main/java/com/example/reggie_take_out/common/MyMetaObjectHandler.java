package com.example.reggie_take_out.common;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.example.reggie_take_out.utils.ThreadLocalUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * 自定义元数据处理器
 * @Author "Airceach"
 * @Date 2022/11/25 7:59
 * @Version 1.0
 */

@Slf4j
@Component
public class MyMetaObjectHandler implements MetaObjectHandler {
    @Override
    public void insertFill(MetaObject metaObject) {
        log.info(metaObject.toString());
        metaObject.setValue("createTime" , LocalDateTime.now());

        metaObject.setValue("createUser" , ThreadLocalUtil.getCurrentId());

        metaObject.setValue("updateTime" , LocalDateTime.now());

        metaObject.setValue("updateUser" , ThreadLocalUtil.getCurrentId());


    }

    @Override
    public void updateFill(MetaObject metaObject) {
        log.info(metaObject.toString());
        metaObject.setValue("updateTime" , LocalDateTime.now());

        metaObject.setValue("updateUser" , ThreadLocalUtil.getCurrentId());

    }
}
