package com.lizxing.securitydemo.service;

import com.lizxing.securitydemo.pojo.MyUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class UserDetailServiceImpl implements UserDetailsService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 根据username通过数据库查询出用户信息
        // MyUser user = xxxDao.selectByName(username)
        // 这里直接new一个来模拟数据库操作
        MyUser user = new MyUser();
        user.setUserName(username);
        user.setPassword("123456");

        // 模拟权限
        if ("aaa".equals(username)){
            user.setIdentity("admin");
        } else {
            user.setIdentity("user");
        }
        List<GrantedAuthority> authorities = new ArrayList<>();
        // 记录这个用户的权限
        authorities = AuthorityUtils.commaSeparatedStringToAuthorityList(user.getIdentity());

        // 密码加密（可选，在配置类中配置加密方法）
        user.setPassword(this.passwordEncoder.encode(user.getPassword()));

        return new User(username, user.getPassword(), true, true, true, true, authorities);
    }
}
