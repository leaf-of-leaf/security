package com.briup.security.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author kj
 * @Date 2020/4/22 13:17
 * @Version 1.0
 */
@Controller
public class SecurityController {

    @GetMapping("/hello")
    @ResponseBody
    public String Hello(){
        return "Hello Spring Security";
    }

}
