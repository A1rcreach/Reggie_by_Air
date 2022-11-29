package com.example.reggie_take_out.utils;

/**
 * @Author "Airceach"
 * @Date 2022/11/25 8:44
 * @Version 1.0
 */
public class ThreadLocalUtil {
    private static ThreadLocal<Long> threadLocal = new ThreadLocal<>();

    public static void setCurrentId(Long id){
        threadLocal.set(id);
    }

    public static Long getCurrentId(){
        return threadLocal.get();
    }

}
