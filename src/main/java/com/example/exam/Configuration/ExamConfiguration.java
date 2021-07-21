package com.example.exam.Configuration;

import com.example.exam.interceptor.AdminInterceptor;
import com.example.exam.interceptor.StudentInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class ExamConfiguration implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry){
        registry.addInterceptor(studentInterceptor())
                .addPathPatterns("/api/exams/**");

        registry.addInterceptor(adminInterceptor())
                .addPathPatterns("/api/students")
                .addPathPatterns("/api/admins")
                .addPathPatterns("/api/questions")
                .addPathPatterns("/api/questions/**");
    }

    @Bean
    public StudentInterceptor studentInterceptor() {
        return new StudentInterceptor();
    }

    @Bean
    public AdminInterceptor adminInterceptor() {
        return new AdminInterceptor();
    }
}
