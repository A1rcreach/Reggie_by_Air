package com.example.reggie_take_out.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.reggie_take_out.common.Result;
import com.example.reggie_take_out.entity.User;
import com.example.reggie_take_out.service.UserService;
import com.example.reggie_take_out.utils.ValidateCodeUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 * @Author "Airceach"
 * @Date 2022/11/27 16:10
 * @Version 1.0
 */
@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/sendMsg")
    public Result<String> sendMsg(HttpSession session , @RequestBody User user){
        String phone = user.getPhone();
        if (StringUtils.isNotEmpty(phone)){
            log.info("调用 Aliyun SSM API");
            String code = ValidateCodeUtils.generateValidateCode(4).toString();
            log.info("code : {}" , code);
//            try {
//                SMSUtils.sendMessage(" " , " " , phone , code );
//            } catch (Exception e) {
//                throw new RuntimeException(e);
//            }
            session.setAttribute(phone , code);
            return Result.success("发送成功");
        }
        return Result.error("发送失败");
    }

    @PostMapping("/login")
    public Result<User> login(HttpSession session , @RequestBody Map map){
        String phone = map.get("phone").toString();
        String code = map.get("code").toString();
        Object attribute = session.getAttribute(phone);
        if (attribute != null && attribute.equals(code)){
            LambdaQueryWrapper<User> queryWrapper =new LambdaQueryWrapper<>();
            queryWrapper.eq(User::getPhone , phone);
            User user = userService.getOne(queryWrapper);
            if (user == null){
                user = new User();
                user.setPhone(phone);
                userService.save(user);
            }
            session.setAttribute("user" , user.getId());
            return Result.success(user);

        }
        return Result.error("登陆失败");

    }
}
