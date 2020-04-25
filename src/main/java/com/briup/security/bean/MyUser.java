package com.briup.security.bean;

import java.io.Serializable;

/**
 * @author kj
 * @Date 2020/4/22 15:52
 * @Version 1.0
 * 对应UserDetails
 */
public class MyUser implements Serializable {
    private static final long serialVersionUID = 3497935890426858541L;

    private String userName;

    private String password;
    /**
     * 方法返回boolean类型，用于判断账户是否未过期，未过期返回true反之返回false；
     */
    private boolean accountNonExpired = true;
    /**
     * 用于判断账户是否未锁定
     */
    private boolean accountNonLocked= true;
    /**
     * 用于判断用户凭证是否没过期，即密码是否未过期；
     */
    private boolean credentialsNonExpired= true;

    /**
     * 方法用于判断用户是否可用。
     */
    private boolean enabled= true;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isAccountNonExpired() {
        return accountNonExpired;
    }

    public void setAccountNonExpired(boolean accountNonExpired) {
        this.accountNonExpired = accountNonExpired;
    }

    public boolean isAccountNonLocked() {
        return accountNonLocked;
    }

    public void setAccountNonLocked(boolean accountNonLocked) {
        this.accountNonLocked = accountNonLocked;
    }

    public boolean isCredentialsNonExpired() {
        return credentialsNonExpired;
    }

    public void setCredentialsNonExpired(boolean credentialsNonExpired) {
        this.credentialsNonExpired = credentialsNonExpired;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public String toString() {
        return "MyUser{" +
                "userName='" + userName + '\'' +
                ", password='" + password + '\'' +
                ", accountNonExpired=" + accountNonExpired +
                ", accountNonLocked=" + accountNonLocked +
                ", credentialsNonExpired=" + credentialsNonExpired +
                ", enabled=" + enabled +
                '}';
    }
}
