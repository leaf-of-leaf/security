package com.briup.security.config;

import com.briup.security.filter.SmsCodeFilter;
import com.briup.security.filter.ValidataCodeFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import javax.sql.DataSource;

/**
 * @author kj
 * @Date 2020/4/22 13:27
 * @Version 1.0
 */

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private ValidataCodeFilter validataCodeFilter;

    @Autowired
    private UserDetailService userDetailService;

    @Autowired
    private DataSource dataSource;

    @Autowired
    private SmsCodeFilter smsCodeFilter;

    @Autowired
    private SmsAuthenticationConfig smsAuthenticationConfig;

    @Autowired
    private MySessionExpiredStrategy sessionExpiredStrategy;


    @Override
    //实现登录方式以及登录时要走的过滤器
    protected void configure(HttpSecurity http) throws Exception {
//        http.formLogin() // 表单方式
//                .and()
//                .authorizeRequests() // 授权配置
//                .anyRequest()  // 所有请求
//                .authenticated(); // 都需要认证

                /*
                .loginPage("/login.html")
                指定了跳转到登录页面的请求URL，
                使用thymeleaf时，login.html放入thymeleaf/是无效的
                因为thymeleaf是要走controller然后使用视图解析器，才会走thymeleaf
                ,所以只能放在static或者其他资源文件下
                 */
//        http.formLogin() // 表单登录
//                // http.httpBasic() // HTTP Basic
//                .loginPage("/login.html")   //
//                .loginProcessingUrl("/login")   //  对应登录页面form表单的action="/login"
//                .and()
//                .authorizeRequests() // 授权配置
//                .antMatchers("/login.html").permitAll() //  表示跳转到登录页面的请求不被拦截，否则会进入无限循环。
//                .anyRequest()  // 所有请求
//                .authenticated() // 都需要认证
//                .and().csrf().disable();//关闭csrf不然登录密码正确也登陆不了

        http.addFilterBefore(validataCodeFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(smsCodeFilter,UsernamePasswordAuthenticationFilter.class)
                .formLogin() // 表单登录
                // http.httpBasic() // HTTP Basic
                // 登录跳转 URL,,,修改此处，实现返回Json格式
                .loginPage("/authentication/require")
                // 处理表单登录 URL
                .loginProcessingUrl("/login")
                .successHandler(new MyAuthenticationSuccessHandler())
                .failureHandler(new MyAuthenticationFailureHandler())
                .and()
                .rememberMe()
                .tokenRepository(persistentTokenRepository()) // 配置 token 持久化仓库
                .tokenValiditySeconds(3600) // remember 过期时间，单为秒
                .userDetailsService(userDetailService) // 处理自动登录逻辑
                .and()
                .authorizeRequests() // 授权配置
                // 登录跳转 URL 无需认证
                .antMatchers("/authentication/require", "/login.html","/code/image","/code/sms").permitAll()
                .anyRequest()  // 所有请求
                .authenticated() // 都需要认证
                .and()
                    .csrf().disable()
                .apply(smsAuthenticationConfig)
                .and()
                .sessionManagement() // 添加 Session管理器
                .invalidSessionUrl("/session/invalid") // Session失效后跳转到这个链接
                .maximumSessions(1)//Session最大并发数
                //并发失效后的策略
                .expiredSessionStrategy(sessionExpiredStrategy);
    }

    @Bean
    //将用户登录后的token信息使用JDBCTemplate进行持久化
    public PersistentTokenRepository persistentTokenRepository() {
        JdbcTokenRepositoryImpl jdbcTokenRepository = new JdbcTokenRepositoryImpl();
        //设置数据源
        jdbcTokenRepository.setDataSource(dataSource);
        //用于是否启动项目时创建保存token信息的数据表，这里设置为false，我们自己手动创建。
        jdbcTokenRepository.setCreateTableOnStartup(false);
        return jdbcTokenRepository;
    }

    @Bean
    public PasswordEncoder PasswordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
