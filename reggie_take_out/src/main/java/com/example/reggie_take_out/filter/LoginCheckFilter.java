package com.example.reggie_take_out.filter;

import com.alibaba.fastjson2.JSON;
import com.example.reggie_take_out.common.Result;
import com.example.reggie_take_out.utils.ThreadLocalUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Author "Airceach"
 * @Date 2022/11/23 15:11
 * @Version 1.0
 */
@Slf4j
@WebFilter(filterName = "loginCheckFilter" , urlPatterns = "/*")
public class LoginCheckFilter implements Filter{

    //路径匹配器 (通配符匹配路径)
    public static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest)servletRequest;
        HttpServletResponse response = (HttpServletResponse)servletResponse;
        //获取uri
        String requestURI = request.getRequestURI();
        //过滤路径
        String[] urls = new String[]{ "/employee/login" ,
                "/employee/logout" ,
                "/air/**" ,
                "/admin/**" ,
                "/common/**" ,
                "/user/sendMsg" ,
                "/user/login" ,
        };
        if (urlCheck(requestURI , urls)){
            filterChain.doFilter(request , response);
            return;
        }
        //检测登陆状态
        if (request.getSession().getAttribute("employee") != null){
            long threadId = Thread.currentThread().getId();
            log.info("当前线程id : {}" , threadId);
            Long empId = (Long) request.getSession().getAttribute("employee");
            ThreadLocalUtil.setCurrentId(empId);
            filterChain.doFilter(request , response);
            return;
        }

        if (request.getSession().getAttribute("user") != null){
            long threadId = Thread.currentThread().getId();
            log.info("当前线程id : {}" , threadId);
            Long userId = (Long) request.getSession().getAttribute("user");
            ThreadLocalUtil.setCurrentId(userId);
            filterChain.doFilter(request , response);
            return;
        }
        response.getWriter().write(JSON.toJSONString(Result.error("NOTLOGIN")));
        return;
    }

    public boolean urlCheck(String urlPath , String[] urls){
        for (String url : urls) {
            boolean b = PATH_MATCHER.match(url, urlPath);
            if (b){
                return true;
            }
        }
        return false;
    }
}
