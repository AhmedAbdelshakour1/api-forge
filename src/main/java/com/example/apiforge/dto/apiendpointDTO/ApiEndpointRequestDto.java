package com.example.apiforge.dto.apiendpointDTO;

public class ApiEndpointRequestDto {
    private Long projectId;
    private String path;
    private String method;
    private Long responseSchemaId;
    private Long requestSchemaId;
    private String responseType;
    private Integer defaultCount;
    private Integer statusCode;
    private Integer delayMs;
    private Double errorRate;
    private Boolean isActive;

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public Long getResponseSchemaId() {
        return responseSchemaId;
    }

    public void setResponseSchemaId(Long responseSchemaId) {
        this.responseSchemaId = responseSchemaId;
    }

    public Long getRequestSchemaId() {
        return requestSchemaId;
    }

    public void setRequestSchemaId(Long requestSchemaId) {
        this.requestSchemaId = requestSchemaId;
    }

    public String getResponseType() {
        return responseType;
    }

    public void setResponseType(String responseType) {
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

    public Double getErrorRate() {
        return errorRate;
    }

    public void setErrorRate(Double errorRate) {
        this.errorRate = errorRate;
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }
}
