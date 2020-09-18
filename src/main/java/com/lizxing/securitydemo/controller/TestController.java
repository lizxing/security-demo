package com.lizxing.securitydemo.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestController {

    @RequestMapping("/trySecurity")
    public String trySecurity(){
        return "trySecurity";
    }

    @RequestMapping("/sessionInvalid")
    public String sessionInvalid(){
        return "session失效，需重新登录";
    }

    @RequestMapping("/tryAuthentication")
    @PreAuthorize("hasAuthority('admin')")
    public String tryAuthentication() {
        return "您拥有admin权限，可以查看";
    }
}
