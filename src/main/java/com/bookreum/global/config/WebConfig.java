//package com.bookreum.global.config;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.servlet.config.annotation.CorsRegistry;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//
//@Configuration
//public class WebConfig implements WebMvcConfigurer {
//    @Override
//    public void addCorsMappings(CorsRegistry registry) {
//        registry.addMapping("/**")
//        		.allowedOrigins("http://localhost:5173", "http://10.50.216.86:5173") // 클라이언트 도메인
//                .allowedMethods("GET", "POST", "PUT", "DELETE","OPTIONS")
//                .allowedHeaders("*")
//                .allowCredentials(true);
//    }
//}
//
//
////y 10.50.216.86
////내 ip 10.50.234.11