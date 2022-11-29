package com.example.reggie_take_out.exceptions;

/**
 * @Author "Airceach"
 * @Date 2022/11/25 10:22
 * @Version 1.0
 */
public class CustomException extends RuntimeException{

    public CustomException(String msg){
        super(msg);
    }
}
