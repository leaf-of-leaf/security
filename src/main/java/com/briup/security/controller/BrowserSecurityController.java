package com.briup.security.controller;

import org.springframework.http.HttpStatus;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author kj
 * @Date 2020/4/22 16:44
 * @Version 1.0
 * 作用：配合SecurityConfig中的.loginPage("/authentication/require")
 * 实现访问后缀为.html时，自动跳转到登录页面
 * 当访问其他资源时，提示 ‘访问的资源需要身份认证’
 */

@RestController
public class BrowserSecurityController {
    /**
     * HttpSessionRequestCache为Spring Security提供的用于缓存请求的对象，
     * 通过调用它的getRequest方法可以获取到本次请求的HTTP信息
     */
    private RequestCache requestCache = new HttpSessionRequestCache();
    /**
     * DefaultRedirectStrategy的sendRedirect为Spring Security提供的用于处理重定向的方法。
     */
    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    @GetMapping("/authentication/require")
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public String requireAuthentication(HttpServletRequest request, HttpServletResponse response) throws IOException {
        SavedRequest savedRequest = requestCache.getRequest(request, response);
        if (savedRequest != null) {
            String targetUrl = savedRequest.getRedirectUrl();
            if (StringUtils.endsWithIgnoreCase(targetUrl, ".html")) {
                redirectStrategy.sendRedirect(request, response, "/login.html");
            }
        }
        return "访问的资源需要身份认证！";
    }

}
