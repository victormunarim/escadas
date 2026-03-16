package com.example.demo.dto;

public class CredenciaisDriveDTO {
    private final String clientId;
    private final String projectId;
    private final String parentFolder;

    public CredenciaisDriveDTO(String clientId, String projectId, String parentFolder) {
        this.clientId = clientId;
        this.projectId = projectId;
        this.parentFolder = parentFolder;
    }

    public String getClientId() {
        return clientId;
    }

    public String getProjectId() {
        return projectId;
    }

    public String getParentFolder() {
        return parentFolder;
    }
}
