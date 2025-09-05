package com.example.apiforge.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "api_endpoints")
public class ApiEndpoint {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "path", nullable = false, length = 255)
    @NotBlank(message = "Endpoint path is required")
    @Size(max = 255, message = "Path cannot exceed 255 characters")
    private String path;

    @Enumerated(EnumType.STRING)
    @Column(name = "http_method", nullable = false)
    private HttpMethod httpMethod;

    @Enumerated(EnumType.STRING)
    @Column(name = "response_type")
    private ResponseType responseType = ResponseType.SINGLE;

    @Column(name = "default_count")
    private Integer defaultCount = 10;

    @Column(name = "status_code")
    private Integer statusCode = 200;

    @Column(name = "delay_ms")
    private Integer delayMs = 0;

    @Column(name = "error_rate", precision = 3, scale = 2)
    private BigDecimal errorRate = BigDecimal.ZERO;

    @Column(name = "is_active")
    private Boolean isActive = true;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "response_schema_id")
    private EntitySchema responseSchema;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "request_schema_id")
    private EntitySchema requestSchema;

    @OneToMany(mappedBy = "apiEndpoint", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ApiCallsLog> apiCalls = new ArrayList<>();

    public enum HttpMethod {
        GET, POST, PUT, DELETE, PATCH
    }

    public enum ResponseType {
        SINGLE("single"),
        ARRAY("array"),
        PAGINATED("paginated");

        private final String value;
        ResponseType(String value) {
            this.value = value;
        }
        public String getValue() {
            return value;
        }
    }

    public ApiEndpoint() {}

    public ApiEndpoint(String path, HttpMethod httpMethod, Project project) {
        this.path = path;
        this.httpMethod = httpMethod;
        this.project = project;
    }

    @PrePersist
    public void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    public void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public void addApiCall(ApiCallsLog apiCall) {
        apiCalls.add(apiCall);
        apiCall.setApiEndpoint(this);
    }

    public String getFullUrl() {
        String baseUrl = project.getBaseUrl();
        if (baseUrl == null || baseUrl.isEmpty()) {
            baseUrl = "/api";
        }

        if (!baseUrl.endsWith("/") && !path.startsWith("/")) {
            return baseUrl + "/" + path;
        } else if (baseUrl.endsWith("/") && path.startsWith("/")) {
            return baseUrl + path.substring(1);
        }

        return baseUrl + path;
    }

    public boolean shouldReturnError() {
        if (errorRate == null || errorRate.compareTo(BigDecimal.ZERO) == 0) {
            return false;
        }

        double random = Math.random();
        return random < errorRate.doubleValue();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public HttpMethod getHttpMethod() {
        return httpMethod;
    }

    public void setHttpMethod(HttpMethod httpMethod) {
        this.httpMethod = httpMethod;
    }

    public ResponseType getResponseType() {
        return responseType;
    }

    public void setResponseType(ResponseType responseType) {
        this.responseType = responseType;
    }

    public Integer getDefaultCount() {
        return defaultCount;
    }

    public void setDefaultCount(Integer defaultCount) {
        this.defaultCount = defaultCount;
    }

    public Integer getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }

    public Integer getDelayMs() {
        return delayMs;
    }

    public void setDelayMs(Integer delayMs) {
        this.delayMs = delayMs;
    }

    public BigDecimal getErrorRate() {
        return errorRate;
    }

    public void setErrorRate(BigDecimal errorRate) {
        this.errorRate = errorRate;
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
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

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public EntitySchema getResponseSchema() {
        return responseSchema;
    }

    public void setResponseSchema(EntitySchema responseSchema) {
        this.responseSchema = responseSchema;
    }

    public EntitySchema getRequestSchema() {
        return requestSchema;
    }

    public void setRequestSchema(EntitySchema requestSchema) {
        this.requestSchema = requestSchema;
    }

    public List<ApiCallsLog> getApiCalls() {
        return apiCalls;
    }

    public void setApiCalls(List<ApiCallsLog> apiCalls) {
        this.apiCalls = apiCalls;
    }

    @Override
    public String toString() {
        return "ApiEndpoint{" +
                "id=" + id +
                ", path='" + path + '\'' +
                ", httpMethod=" + httpMethod +
                ", responseType=" + responseType +
                ", defaultCount=" + defaultCount +
                ", statusCode=" + statusCode +
                ", delayMs=" + delayMs +
                ", errorRate=" + errorRate +
                ", isActive=" + isActive +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", project=" + project +
                ", responseSchema=" + responseSchema +
                ", requestSchema=" + requestSchema +
                ", apiCalls=" + apiCalls +
                '}';
    }
}
