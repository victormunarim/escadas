package com.example.demo.security;

import com.example.demo.entity.UsuarioEntity;
import com.example.demo.service.UsuarioService;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProvedorAutenticacaoUsuario implements AuthenticationProvider {

    private final UsuarioService usuarioService;
    private final PasswordEncoder codificadorSenha;

    public ProvedorAutenticacaoUsuario(UsuarioService usuarioService, PasswordEncoder codificadorSenha) {
        this.usuarioService = usuarioService;
        this.codificadorSenha = codificadorSenha;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String login = authentication.getName() == null ? "" : authentication.getName().trim();
        String senhaInformada = authentication.getCredentials() == null ? "" : authentication.getCredentials().toString();

        UsuarioEntity usuario = usuarioService.buscarPorLogin(login)
                .orElseThrow(() -> new BadCredentialsException("Usuário ou senha inválidos"));

        if (!senhaValida(usuario, senhaInformada)) {
            throw new BadCredentialsException("Usuário ou senha inválidos");
        }

        UsuarioAutenticado principal = new UsuarioAutenticado(usuario.getId(), usuario.getNome(), usuario.getEmail());
        return new UsernamePasswordAuthenticationToken(
                principal,
                null,
                List.of(new SimpleGrantedAuthority("ROLE_USER"))
        );
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }

    private boolean senhaValida(UsuarioEntity usuario, String senhaInformada) {
        String senhaBanco = usuario.getSenha();

        if (senhaBanco == null || senhaBanco.isBlank()) {
            return false;
        }

        if (isHashBcrypt(senhaBanco)) {
            return codificadorSenha.matches(senhaInformada, senhaBanco);
        }

        if (!senhaBanco.equals(senhaInformada)) {
            return false;
        }

        usuario.setSenha(codificadorSenha.encode(senhaInformada));
        usuarioService.salvar(usuario);
        return true;
    }

    private boolean isHashBcrypt(String valor) {
        return valor.startsWith("$2a$") || valor.startsWith("$2b$") || valor.startsWith("$2y$");
    }
}
