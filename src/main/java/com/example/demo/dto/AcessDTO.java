package com.example.demo.dto;

public class AcessDTO {

    private String token;

    //TODO Implementar retornar o usuário e liberações (authorities)

    public AcessDTO(String token) {
        super();
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
