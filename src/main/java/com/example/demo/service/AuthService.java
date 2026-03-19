package com.example.demo.service;

import com.example.demo.dto.AcessDTO;
import com.example.demo.dto.AuthenticationDTO;
import com.example.demo.security.jwt.JwtUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class AuthService {

    private final JwtUtils jwtUtils;
    private AuthenticationManager authenticationManager;

    public AuthService(AuthenticationManager authenticationManager, JwtUtils jwtUtils) {
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
    }

    public AcessDTO login(AuthenticationDTO authenticationDTO) {

        try {
            //Cria mecanismo de credencial para o spring
            UsernamePasswordAuthenticationToken userAuth = new UsernamePasswordAuthenticationToken(
                    authenticationDTO.getUsername(),
                    authenticationDTO.getPassword()
            );

            //Prepara mecanismo para autenticação
            Authentication authentication = authenticationManager.authenticate(userAuth);

            //Busca usuário logado
            UserDetailsImpl userAuthenticate = (UserDetailsImpl) authentication.getPrincipal();

            String token = jwtUtils.generateTokenFromUserDetailsImpl(userAuthenticate);

            return new AcessDTO(token);
        } catch(BadCredentialsException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuário ou senha inválidos", e);
        }
    }
}
