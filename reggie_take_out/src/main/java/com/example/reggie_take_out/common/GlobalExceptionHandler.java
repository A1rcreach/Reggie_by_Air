package com.example.reggie_take_out.common;

import com.example.reggie_take_out.exceptions.CustomException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLIntegrityConstraintViolationException;

/**
 * @Author "Airceach"
 * @Date 2022/11/23 17:08
 * @Version 1.0
 */
@Slf4j
@ControllerAdvice(annotations = {RestController.class , Controller.class})
@ResponseBody
public class GlobalExceptionHandler {

    /**
     * SQL 处理异常
     * @param exception
     * @return
     */
    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public Result<String> sqlExceptionHandler(SQLIntegrityConstraintViolationException exception){
        String exceptionMsg = exception.getMessage();
        log.error(exceptionMsg);
        //违反数据库唯一约束条件 ==> 唯一约束属性词条冲突
        if (exceptionMsg.contains("Duplicate entry")){
            //以 " " 分隔 , 获取 String 存入数组
            String[] split = exceptionMsg.split(" ");
            return Result.error("Id  " + split[2] + "  已存在");
        }
        return Result.error("未知错误");
    }

    /**
     * Custom 异常处理
     * @param exception
     * @return
     */
    @ExceptionHandler(CustomException.class)
    public Result<String> customExceptionHandler(CustomException exception){
        String exceptionMsg = exception.getMessage();
        log.error(exceptionMsg);
        return Result.error(exceptionMsg);
    }
}
