package com.example.reggie_take_out.config;

import com.example.reggie_take_out.common.JacksonObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @Author "Airceach"
 * @Date 2022/11/23 9:48
 * @Version 1.0
 */
@Slf4j
@Configuration
public class WebMVCSupport extends WebMvcConfigurationSupport {
    @Override
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
        log.info("静态资源配置完成");
        registry.addResourceHandler("/admin/**").addResourceLocations("classpath:/front/");
        registry.addResourceHandler("/air/**").addResourceLocations("classpath:/backend/");
    }

    @Override
    protected void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        log.info("消息转换器启动");
        //创建消息转换器对象
        MappingJackson2HttpMessageConverter messageConverter = new MappingJackson2HttpMessageConverter();
        //设置对象转换器
        messageConverter.setObjectMapper(new JacksonObjectMapper());
        //将转化器对象加入 MVC 框架内部 转换器集合
        //添加到 0 索引 , 优先执行
        converters.add(0 , messageConverter);
    }

    //    @Override
//    protected void addInterceptors(InterceptorRegistry registry) {
//        registry.addInterceptor(new HandlerInterceptor() {
//            @Override
//            public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
//                log.info("拦截url : {}" , request.getRequestURI());
//                return true;
//            }
//
//            @Override
//            public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
//                HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
//            }
//
//            @Override
//            public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
//                HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
//            }
//        }).addPathPatterns("/air/index.html");
//    }
}
