package com.example.reggie_take_out.common;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author "Airceach"
 * @Date 2022/11/23 10:08
 * @Version 1.0
 */
@Data
public class Result<T> {

    private Integer code; //编码
    private String msg; //错误信息
    private T data; //数据
    private Map map = new HashMap(); //动态数据

    public static <T> Result<T> success(T object) {
        Result<T> res = new Result<T>();
        res.data = object;
        res.code = 10000;
        return res;
    }
    public static <T> Result<T> error(String msg) {
        Result<T> res = new Result<T>();
        res.msg = msg;
        res.code = 10001;
        return res;
    }
    public Result<T> add(String key , Object value) {
        this.map.put(key , value);
        return this;
    }

}
