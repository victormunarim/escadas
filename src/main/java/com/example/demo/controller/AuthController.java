package com.example.demo.controller;

import com.example.demo.dto.AuthenticationDTO;
import com.example.demo.service.AuthService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@CrossOrigin
public class AuthController {

    private AuthService authService;
    private int jwtExpirationMs;

    public AuthController(AuthService authService, @Value("${projeto.jwtExpirationMs}") int jwtExpirationMs) {
        this.authService = authService;
        this.jwtExpirationMs = jwtExpirationMs;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthenticationDTO authenticationDTO) {
        var acess = authService.login(authenticationDTO);
        ResponseCookie cookie = ResponseCookie.from("AUTH_TOKEN", acess.getToken())
                .httpOnly(true)
                .sameSite("Lax")
                .path("/")
                .maxAge(jwtExpirationMs / 1000)
                .build();
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(acess);
    }
}
