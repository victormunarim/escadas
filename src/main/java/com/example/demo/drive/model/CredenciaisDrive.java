package com.example.demo.drive.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "google_drive_credentials")
public class CredenciaisDrive {

    @Id
    @Column(name = "nome", nullable = false)
    private String nome;

    @Column(name = "client_id", nullable = false)
    private String clientId;

    @Column(name = "project_id", nullable = false)
    private String projectId;

    @Column(name = "client_secret", nullable = false)
    private String clientSecret;

    @Column(name = "token", columnDefinition = "TEXT")
    private String token;

    @Column(name = "parent_folder")
    private String parentFolder;

    protected CredenciaisDrive() {
    }

    public CredenciaisDrive(String nome, String clientId, String projectId, String clientSecret) {
        this.nome = nome;
        this.clientId = clientId;
        this.projectId = projectId;
        this.clientSecret = clientSecret;
    }

    public String getNome() {
        return nome;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getParentFolder() {
        return parentFolder;
    }

    public void setParentFolder(String parentFolder) {
        this.parentFolder = parentFolder;
    }
}
