package com.briup.security.config;

import com.briup.security.bean.MyUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * @author kj
 * @Date 2020/4/22 15:55
 * @Version 1.0
 * 自定义认证过程
 * Spring Security支持我们自定义认证的过程，如处理用户信息获取逻辑，使用我们自定义的登录页面替换
 * Spring Security默认的登录页及自定义登录成功或失败后的处理逻辑等。这里将在上一节的源码基础上进行改造。
 */

@Configuration
public class UserDetailService implements UserDetailsService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * loadUserByUsername方法返回一个UserDetail对象，该对象也是一个接口，包含一些用于描述用户信息的方法
     * @param username
     * @return
     * @throws UsernameNotFoundException
     */

    @Override
    /*
    这里我们使用了org.springframework.security.core.userdetails.User类包含7个参数的构造器，其还包含
    一个三个参数的构造器
    User(String username, String password,Collection<? extends GrantedAuthority> authorities)，
    由于权限参数不能为空，所以这里先使用AuthorityUtils.commaSeparatedStringToAuthorityList方法模拟一个
    admin的权限，该方法可以将逗号分隔的字符串转换为权限集合。
     */
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 模拟一个用户，替代数据库获取逻辑
        MyUser user = new MyUser();
        user.setUserName(username);
        user.setPassword(this.passwordEncoder.encode("123456"));
        // 输出加密后的密码
        System.out.println(user.getPassword());
        //实际中我们可以自定义UserDetails接口的实现类，也可以直接使用Spring Security提供的UserDetails接口
        // 实现类org.springframework.security.core.userdetails.User。
        //此处为使用自带的实现类
        return new User(username, user.getPassword(), user.isEnabled(),
                user.isAccountNonExpired(), user.isCredentialsNonExpired(),
                user.isAccountNonLocked(), AuthorityUtils.commaSeparatedStringToAuthorityList("admin"));
    }
}
