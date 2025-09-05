package com.example.apiforge.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "entity_schemas")
public class EntitySchema {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name", nullable = false, length = 50)
    @NotBlank(message = "Schema name is required")
    @Size(min = 2, max = 50, message = "Schema name must be between 2 and 50 characters")
    private String name;

    @Column(name = "description", length = 500)
    private String description;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    @OneToMany(mappedBy = "entitySchema", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<SchemaField> fields = new ArrayList<>();

    @OneToMany(mappedBy = "responseSchema", fetch = FetchType.LAZY)
    private List<ApiEndpoint> responseEndpoints = new ArrayList<>();

    @OneToMany(mappedBy = "requestSchema", fetch = FetchType.LAZY)
    private List<ApiEndpoint> requestEndpoints = new ArrayList<>();

    public EntitySchema() {}

    public EntitySchema(String name, String description, Project project) {
        this.name = name;
        this.description = description;
        this.project = project;
    }
    @PrePersist
    public void onCreate(){
        this.createdAt = LocalDateTime.now();
    }
    
    public void addField(SchemaField field) {
        fields.add(field);
        field.setEntitySchema(this);
    }

    public void removeField(SchemaField field) {
        fields.remove(field);
        field.setEntitySchema(null);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public List<SchemaField> getFields() {
        return fields;
    }

    public void setFields(List<SchemaField> fields) {
        this.fields = fields;
    }

    public List<ApiEndpoint> getResponseEndpoints() {
        return responseEndpoints;
    }

    public void setResponseEndpoints(List<ApiEndpoint> responseEndpoints) {
        this.responseEndpoints = responseEndpoints;
    }

    public List<ApiEndpoint> getRequestEndpoints() {
        return requestEndpoints;
    }

    public void setRequestEndpoints(List<ApiEndpoint> requestEndpoints) {
        this.requestEndpoints = requestEndpoints;
    }

    @Override
    public String toString() {
        return "EntitySchema{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", createdAt=" + createdAt +
                ", project=" + project +
                ", fields=" + fields +
                ", responseEndpoints=" + responseEndpoints +
                ", requestEndpoints=" + requestEndpoints +
                '}';
    }
}
