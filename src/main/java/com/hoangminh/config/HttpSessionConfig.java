//package com.hoangminh.config;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.context.annotation.SessionScope;
//import org.springframework.web.context.request.RequestContextHolder;
//import org.springframework.web.context.request.ServletRequestAttributes;
//
//import jakarta.servlet.http.HttpSession;
//
//
//@Configuration
//public class HttpSessionConfig {
// 
//    @Bean
//    @SessionScope
//    public HttpSession httpSession() {
//    	ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
//        return attr.getRequest().getSession();
//    }
//}