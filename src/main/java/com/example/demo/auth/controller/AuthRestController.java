package com.example.demo.auth.controller;

import com.example.demo.auth.service.UserDetailsImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@RestController
public class AuthRestController {

    @GetMapping("/api/auth/me")
    public ResponseEntity<?> getMe(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated() || "anonymousUser".equals(authentication.getPrincipal())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        Map<String, Object> user = new HashMap<>();
        user.put("username", authentication.getName());
        if (authentication.getPrincipal() instanceof UserDetailsImpl userDetails) {
            user.put("perfil", userDetails.getPerfil() != null ? userDetails.getPerfil().name() : null);
            user.put("permissoes", userDetails.getPerfil() != null ? userDetails.getPerfil().getPermissoes() : Collections.emptySet());
        }
        return ResponseEntity.ok(user);
    }
}
