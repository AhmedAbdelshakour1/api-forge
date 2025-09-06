package com.example.apiforge.dto.entityschemaDTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class EntitySchemaRequestDto {

    @NotBlank(message = "Schema name is required")
    @Size(min = 2, max = 50, message = "Schema name must be between 2 and 50 characters")
    private String name;
    private String description;
    private Long projectId;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }
}
