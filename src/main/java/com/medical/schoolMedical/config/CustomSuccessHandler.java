package com.medical.schoolMedical.config;


import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        // Lặp qua các vai trò (roles) của người dùng
        for (GrantedAuthority auth : authentication.getAuthorities()) {
            String role = auth.getAuthority();

            // Chuyển hướng theo role
            if (role.equals("ROLE_MANAGER")) {
                response.sendRedirect("/yte/admin/dashboard");
                return;
            } else if (role.equals("ROLE_SCHOOL_NURSE")) {
                response.sendRedirect("/yte/nurse/students");
                return;
            } else if (role.equals("ROLE_PARENT")) {
                response.sendRedirect("/yte/parent/report");
                return;
            }
        }
        // Nếu không khớp vai trò nào thì quay lại trang chủ
        response.sendRedirect("/yte/home");
    }
}
