package com.example.exam.interceptor;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.exam.entity.Admin;
import com.example.exam.entity.Student;
import com.example.exam.repository.AdminRepository;
import com.example.exam.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

public class StudentInterceptor implements HandlerInterceptor {
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private AdminRepository adminRepository;
    private final String jwtHmacKey = "ThisIsTestKey";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler){
        String token = request.getHeader("Authorization");

        if (token == null || token.equals("")){
            response.setStatus(401);
            return false;
        }

        try{
            String uids = JWT.require(Algorithm.HMAC256(jwtHmacKey))
                    .build()
                    .verify(token)
                    .getAudience()
                    .get(0);
            Long uid = Long.parseLong(uids);
            Optional<Student> stu = studentRepository.findById(uid);
            Optional<Admin> admin = adminRepository.findById(uid);
            return (stu.isPresent() || admin.isPresent());
        }
        catch (Exception e){
            response.setStatus(401);
            return false;
        }
    }
}
