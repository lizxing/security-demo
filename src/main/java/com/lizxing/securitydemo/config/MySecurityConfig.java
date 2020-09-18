package com.lizxing.securitydemo.config;

import com.lizxing.securitydemo.handle.MyAccessDeniedHandler;
import com.lizxing.securitydemo.handle.MyAuthenticationFailureHandler;
import com.lizxing.securitydemo.handle.MyAuthenticationSucessHandler;
import com.lizxing.securitydemo.handle.MySessionInformationExpiredStrategy;
import com.lizxing.securitydemo.service.UserDetailServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import javax.sql.DataSource;

/**
 * @author Administrator
 */
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class MySecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private MyAuthenticationSucessHandler myAuthenticationSucessHandler;

    @Autowired
    private MyAuthenticationFailureHandler myAuthenticationFailureHandler;

    @Autowired
    private DataSource dataSource;

    @Autowired
    private UserDetailServiceImpl userDetailService;

    @Autowired
    private MySessionInformationExpiredStrategy mySessionInformationExpiredStrategy;

    @Autowired
    private MyAccessDeniedHandler myAccessDeniedHandler;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.exceptionHandling()
                .accessDeniedHandler(myAccessDeniedHandler)
                .and()
                .formLogin() // 表单方式
//        http.httpBasic() // HTTP Basic方式
                .loginPage("/login.html")
                .loginProcessingUrl("/login")
                .successHandler(myAuthenticationSucessHandler) // 处理登录成功
                .failureHandler(myAuthenticationFailureHandler) // 处理登录失败
                .and()
                .rememberMe()
                .tokenRepository(persistentTokenRepository()) // 配置 token 持久化仓库
                .tokenValiditySeconds(3600) // remember 过期时间，单为秒
                .userDetailsService(userDetailService) // 处理自动登录逻辑
                .and()
                .authorizeRequests() // 授权配置
                .antMatchers("/login.html", "/test/sessionInvalid").permitAll() //允许通过
                .anyRequest()  // 所有请求
                .authenticated() // 都需要认证
                .and()
                .sessionManagement()
//                .sessionCreationPolicy(SessionCreationPolicy.STATELESS) //不使用session
                .invalidSessionUrl("/test/sessionInvalid") // Session失效后跳转
                .maximumSessions(1)
                .maxSessionsPreventsLogin(true)
                .expiredSessionStrategy(mySessionInformationExpiredStrategy)
                .and()
                .and().csrf().disable(); //关闭scrf
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public PersistentTokenRepository persistentTokenRepository() {
        JdbcTokenRepositoryImpl jdbcTokenRepository = new JdbcTokenRepositoryImpl();
        jdbcTokenRepository.setDataSource(dataSource);
        jdbcTokenRepository.setCreateTableOnStartup(false);
        return jdbcTokenRepository;
    }
}
