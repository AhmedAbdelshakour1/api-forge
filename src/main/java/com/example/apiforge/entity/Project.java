package com.example.apiforge.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "projects")
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name", nullable = false, length = 100)
    @NotBlank(message = "Name is required")
    private String name;

    @Column(name = "description", length = 500)
    @NotBlank(message = "Description is required")
    private String description;

    @Column(name = "base_url")
    private String baseUrl;

    @Column(name = "is_public")
    private Boolean isPublic;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Original relationship
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // NEW RELATIONSHIPS - Add these to your existing Project entity
    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<EntitySchema> entitySchemas = new ArrayList<>();

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ApiEndpoint> apiEndpoints = new ArrayList<>();

    public Project() {}

    public Project(String name, String description, String baseUrl, Boolean isPublic, User user) {
        this.name = name;
        this.description = description;
        this.baseUrl = baseUrl;
        this.isPublic = isPublic;
        this.user = user;
    }

    // NEW HELPER METHODS - Add these to your existing Project entity
    public void addEntitySchema(EntitySchema entitySchema) {
        entitySchemas.add(entitySchema);
        entitySchema.setProject(this);
    }

    public void removeEntitySchema(EntitySchema entitySchema) {
        entitySchemas.remove(entitySchema);
        entitySchema.setProject(null);
    }

    public void addApiEndpoint(ApiEndpoint apiEndpoint) {
        apiEndpoints.add(apiEndpoint);
        apiEndpoint.setProject(this);
    }

    public void removeApiEndpoint(ApiEndpoint apiEndpoint) {
        apiEndpoints.remove(apiEndpoint);
        apiEndpoint.setProject(null);
    }

    /**
     * Get the full base URL for this project
     * If not set, generates one based on project ID
     */
    public String getEffectiveBaseUrl() {
        if (baseUrl != null && !baseUrl.trim().isEmpty()) {
            return baseUrl;
        }
        return "/mock/project/" + id;
    }

    // All your existing getters/setters remain the same...
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

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public Boolean getIsPublic() {
        return isPublic;
    }

    public void setIsPublic(Boolean isPublic) {
        this.isPublic = isPublic;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    // NEW GETTERS/SETTERS - Add these
    public List<EntitySchema> getEntitySchemas() {
        return entitySchemas;
    }

    public void setEntitySchemas(List<EntitySchema> entitySchemas) {
        this.entitySchemas = entitySchemas;
    }

    public List<ApiEndpoint> getApiEndpoints() {
        return apiEndpoints;
    }

    public void setApiEndpoints(List<ApiEndpoint> apiEndpoints) {
        this.apiEndpoints = apiEndpoints;
    }

    @PrePersist
    public void onCreate(){
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    public void onUpdate(){
        this.updatedAt = LocalDateTime.now();
    }

    @Override
    public String toString() {
        return "Project{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", baseUrl='" + baseUrl + '\'' +
                ", isPublic=" + isPublic +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", user=" + user +
                ", entitySchemas=" + entitySchemas +
                ", apiEndpoints=" + apiEndpoints +
                '}';
    }
}
