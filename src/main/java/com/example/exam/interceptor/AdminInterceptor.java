package com.example.exam.interceptor;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.exam.entity.Admin;
import com.example.exam.repository.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

public class AdminInterceptor implements HandlerInterceptor {
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
            Optional<Admin> admin = adminRepository.findById(uid);

            return admin.isPresent();
        }
        catch (Exception e){
            response.setStatus(401);
            return false;
        }
    }
}
