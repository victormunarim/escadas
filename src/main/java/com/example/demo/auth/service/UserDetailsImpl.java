package com.example.demo.auth.service;

import com.example.demo.auth.model.Perfil;
import com.example.demo.auth.model.UsuarioEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class UserDetailsImpl implements UserDetails {

    private final Long id;

    private final String username;

    private final String email;

    private final String password;

    private final Perfil perfil;

    private final Collection<? extends GrantedAuthority> authorities;

    public UserDetailsImpl(
            Long id,
            String username,
            String password,
            String email,
            Perfil perfil,
            Collection<? extends GrantedAuthority> authorities
    ) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.perfil = perfil;
        this.authorities = authorities;
    }

    public static UserDetailsImpl build(UsuarioEntity usuario) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        Perfil perfil = usuario.getPerfil();
        if (perfil != null) {
            authorities.add(new SimpleGrantedAuthority("ROLE_" + perfil.name()));
            for (String perm : perfil.getPermissoes()) {
                authorities.add(new SimpleGrantedAuthority(perm));
            }
        }

        return new UserDetailsImpl(
                usuario.getId(),
                usuario.getLogin(),
                usuario.getSenha(),
                usuario.getEmail(),
                perfil,
                authorities
        );
    }

    public Perfil getPerfil() {
        return perfil;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}