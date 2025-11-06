package com.sedocefosse.backend.configs.security;


import com.sedocefosse.backend.configs.exceptions.LoginException;
import com.sedocefosse.backend.model.admin.AdminEntity;
import com.sedocefosse.backend.repository.admin.AdminRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Component
@RequiredArgsConstructor
public class SecurityFilter extends OncePerRequestFilter {
    private static final Logger logger = LoggerFactory.getLogger(SecurityFilter.class);
    @Autowired
    TokenService tokenService;
    @Autowired
    AdminRepository adminRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        var token = this.recoverToken(request);
        if (token == null) {
            logger.debug("No Authorization header present for request {} {}", request.getMethod(), request.getRequestURI());
        } else {
            logger.debug("Authorization header present (token length={}) for request {} {}", token.length(), request.getMethod(), request.getRequestURI());
        }

        var login = tokenService.validateToken(token);
        if(login != null){
            logger.debug("Token validated, subject={}", login);
            AdminEntity user = adminRepository.findByEmail(login).orElseThrow(() -> new LoginException("User Not Found"));
            var authority = new SimpleGrantedAuthority(user.getRole().name());
            var authorities = Collections.singletonList(authority);
            var authentication = new UsernamePasswordAuthenticationToken(user, null, authorities);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            logger.info("Authenticated user={} with role={} for request {} {}", 
                user.getEmail(), user.getRole(), request.getMethod(), request.getRequestURI());
        } else {
            if (token != null) logger.info("Token present but invalid for request {} {}", request.getMethod(), request.getRequestURI());
        }
        filterChain.doFilter(request, response);
    }

    private String recoverToken(HttpServletRequest request){
        var authHeader = request.getHeader("Authorization");
        if(authHeader == null) return null;
        return authHeader.replace("Bearer ", "");
    }
}
