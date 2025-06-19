package com.medical.schoolMedical.security;


import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collection;

@Component
public class CustomSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

        // Lặp qua các vai trò (roles) của người dùng
        for (GrantedAuthority auth : authentication.getAuthorities()) {
            String role = auth.getAuthority();

            switch (role) {
                case "ROLE_ADMIN":
                    response.sendRedirect("/yte/admin/dashboard");
                    return;
                case "ROLE_PARENT":
                    response.sendRedirect("/yte/parent/parent-home");
                    return;
                case "ROLE_MANAGER":
                    response.sendRedirect("/yte/manager/manager-home");
                    return;
                case "ROLE_SCHOOL_NURSE":
                    response.sendRedirect("/yte/nurse/nurse-home");
                    return;
                default:
                    response.sendRedirect("/yte/access-denied");
                    return;
            }
        }
    }
}
