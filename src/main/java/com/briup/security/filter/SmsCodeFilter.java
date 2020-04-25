package com.briup.security.filter;

import com.briup.security.bean.SmsCode;
import com.briup.security.controller.ValidataController;
import com.briup.security.exception.ValidateCodeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.social.connect.web.HttpSessionSessionStrategy;
import org.springframework.social.connect.web.SessionStrategy;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author kj
 * @Date 2020/4/24 15:49
 * @Version 1.0
 * 进行手机验证码的校验
 */

@Component
public class SmsCodeFilter extends OncePerRequestFilter {
    @Autowired
    private AuthenticationFailureHandler authenticationFailureHandler;

    private SessionStrategy sessionStrategy = new HttpSessionSessionStrategy();

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String loginUrl = "/login/mobile";
        String loginMethod = "POST";

        String requestURI = request.getRequestURI();
        String method = request.getMethod();


        if (loginUrl.equals(requestURI)
                && loginMethod.equals(method)) {
            try {
                ValidataCode(new ServletWebRequest(request));
            } catch (ValidateCodeException e) {
                authenticationFailureHandler.onAuthenticationFailure(request, response, e);
                return;
            }
        }
        filterChain.doFilter(request, response);
    }

    private void ValidataCode(ServletWebRequest servletWebRequest) throws ValidateCodeException {
        String smsCodeInRequest = servletWebRequest.getRequest().getParameter("smsCode");
        String mobileInRequest = servletWebRequest.getRequest().getParameter("mobile");
        SmsCode smsCode = (SmsCode) sessionStrategy.getAttribute(servletWebRequest, ValidataController.SESSION_KEY_SMS_CODE + mobileInRequest);
        if (StringUtils.isEmpty(smsCodeInRequest)) {
            throw new ValidateCodeException("验证码不能为空！");
        }
        if (smsCode == null) {
            throw new ValidateCodeException("验证码不存在！");
        }
        if (smsCode.isExpire()) {
            sessionStrategy.removeAttribute(servletWebRequest, ValidataController.SESSION_KEY_SMS_CODE + mobileInRequest);
            throw new ValidateCodeException("验证码已过期！");
        }
        if (!smsCode.getCode().equals(smsCodeInRequest)) {
            throw new ValidateCodeException("验证码不正确！");
        }
        sessionStrategy.removeAttribute(servletWebRequest, ValidataController.SESSION_KEY_SMS_CODE + mobileInRequest);
    }
}