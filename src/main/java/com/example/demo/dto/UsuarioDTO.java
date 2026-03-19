package com.example.demo.dto;

import com.example.demo.entity.UsuarioEntity;
import org.springframework.beans.BeanUtils;

public class UsuarioDTO {

    private Long id;

    private String email;

    private String login;

    private String senha;

    public UsuarioDTO(UsuarioEntity usuario) {
        BeanUtils.copyProperties(usuario, this);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }
}
