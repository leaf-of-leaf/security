package com.briup.security.controller;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author kj
 * @Date 2020/4/25 10:41
 * @Version 1.0
 * Session管理,
 */
@Controller
public class SessionController {
    /**
     * 超时
     * @return
     */
    @GetMapping("session/invalid")
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public String sessionInvalid(){
        return "session已失效，请重新认证";
    }

}
