package com.briup.security.config;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author kj
 * @Date 2020/4/22 17:30
 * @Version 1.0
 * 自定义登录成功逻辑
 */
@Component
public class MyAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private RequestCache requestCache = new HttpSessionRequestCache();
    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication)
            throws IOException, ServletException {
//        response.setContentType("application/json;charset=utf-8");
//        ObjectMapper mapper = new ObjectMapper();
//        response.getWriter().write(mapper.writeValueAsString(authentication));
        SavedRequest savedRequest = requestCache.getRequest(request, response);

        //如果saveRequest为null则说明之前已经成功登录过了，所以对应的之前的缓存已经被清除了
        if(savedRequest == null){
            redirectStrategy.sendRedirect(request, response, "/hello");
            return;
        }

        //跳转回登录前访问的页面
        redirectStrategy.sendRedirect(request, response, savedRequest.getRedirectUrl());
    }
}
