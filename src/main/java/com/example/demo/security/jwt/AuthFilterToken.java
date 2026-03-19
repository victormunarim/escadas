package com.example.demo.security.jwt;

import com.example.demo.service.UserDetailServiceImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Cookie;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class AuthFilterToken extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(AuthFilterToken.class);

    private JwtUtils jwtUtils;

    private UserDetailServiceImpl userDetailService;

    public AuthFilterToken(JwtUtils jwtUtils, UserDetailServiceImpl userDetailService) {
        this.jwtUtils = jwtUtils;
        this.userDetailService = userDetailService;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        try {
            String jwt = getToken(request);

            if (jwt != null && jwtUtils.validateJwtToken(jwt)) {
                String username = jwtUtils.getUsernameToken(jwt);

                UserDetails userDetails = userDetailService.loadUserByUsername(username);
                UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                        userDetails, null,
                        userDetails.getAuthorities()
                );
                auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(auth);
                logger.debug("Usuario autenticado via JWT: {}", username);
            } else if (jwt != null) {
                logger.warn("JWT presente, mas inválido.");
            }
        } catch (Exception e) {
            logger.warn("Erro ao processar o token JWT", e);
        }

        filterChain.doFilter(request, response);
    }

    private String getToken(HttpServletRequest request) {
        String headerToken = request.getHeader("Authorization");

        if (StringUtils.hasText(headerToken) && headerToken.startsWith("Bearer")) {
            return headerToken.replace("Bearer ", "");
        }

        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("AUTH_TOKEN".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }

        return null;
    }
}
