package com.taita.springboot.taxibookingcustomerapi.config;

import com.taita.springboot.taxibookingcustomerapi.dto.RequestMetaDTO;
import com.taita.springboot.taxibookingcustomerapi.util.JwtUtil;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@Component
public class JwtInterceptor implements HandlerInterceptor {

    @Autowired
    private JwtUtil jwtUtil;

    private final RequestMetaDTO requestMetaDTO;

    public JwtInterceptor(RequestMetaDTO requestMetaDTO){
        this.requestMetaDTO = requestMetaDTO;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String auth = request.getHeader("authorization");

        if(!(request.getRequestURI().contains("auth/"))){
            Claims claims = jwtUtil.verify(auth);
            requestMetaDTO.setCustomerId(Integer.parseInt(claims.getIssuer()));
            requestMetaDTO.setUsername(claims.get("username").toString());
            requestMetaDTO.setMobile(claims.get("mobile").toString());
        }

        return HandlerInterceptor.super.preHandle(request, response, handler);
    }
}
