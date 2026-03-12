package com.example.demo.auth.security;

import com.example.demo.auth.model.Usuario;
import com.example.demo.auth.repository.RepositorioUsuario;
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

    private final RepositorioUsuario repositorioUsuario;
    private final PasswordEncoder codificadorSenha;

    public ProvedorAutenticacaoUsuario(RepositorioUsuario repositorioUsuario, PasswordEncoder codificadorSenha) {
        this.repositorioUsuario = repositorioUsuario;
        this.codificadorSenha = codificadorSenha;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String login = authentication.getName() == null ? "" : authentication.getName().trim();
        String senhaInformada = authentication.getCredentials() == null ? "" : authentication.getCredentials().toString();

        Usuario usuario = buscarUsuarioPorLogin(login)
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

    private boolean senhaValida(Usuario usuario, String senhaInformada) {
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

        // Migra automaticamente senha legada em texto puro para hash bcrypt.
        usuario.setSenha(codificadorSenha.encode(senhaInformada));
        repositorioUsuario.save(usuario);
        return true;
    }

    private boolean isHashBcrypt(String valor) {
        return valor.startsWith("$2a$") || valor.startsWith("$2b$") || valor.startsWith("$2y$");
    }

    private java.util.Optional<Usuario> buscarUsuarioPorLogin(String login) {
        if (login == null || login.isBlank()) {
            return java.util.Optional.empty();
        }

        java.util.Optional<Usuario> usuarioPorEmail = repositorioUsuario.findByEmailIgnoreCase(login);
        if (usuarioPorEmail.isPresent()) {
            return usuarioPorEmail;
        }

        return repositorioUsuario.findByNomeIgnoreCase(login);
    }
}
