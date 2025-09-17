package com.example.apiforge.service;

import com.example.apiforge.dto.apiendpointDTO.ApiEndpointRequestDto;
import com.example.apiforge.dto.apiendpointDTO.ApiEndpointResponseDto;
import com.example.apiforge.entity.ApiEndpoint;
import com.example.apiforge.entity.EntitySchema;
import com.example.apiforge.entity.Project;
import com.example.apiforge.entity.SchemaField;
import com.example.apiforge.repository.ApiEndpointRepository;
import com.example.apiforge.repository.EntitySchemaRepository;
import com.example.apiforge.repository.ProjectRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Map;

@Service
public class ApiEndpointService {
    private final ApiEndpointRepository apiEndpointRepository;
    private final ProjectRepository projectRepository;
    private final EntitySchemaRepository entitySchemaRepository;
    private final DataGeneratorService dataGeneratorService;

    public ApiEndpointService(ApiEndpointRepository apiEndpointRepository, DataGeneratorService dataGeneratorService, ProjectRepository projectRepository, EntitySchemaRepository entitySchemaRepository) {
        this.apiEndpointRepository = apiEndpointRepository;
        this.dataGeneratorService = dataGeneratorService;
        this.projectRepository = projectRepository;
        this.entitySchemaRepository = entitySchemaRepository;
    }

    public Object handleGetRequest(String path){
        ApiEndpoint endpoint = apiEndpointRepository.findByPathAndHttpMethod(path, ApiEndpoint.HttpMethod.GET)
                .orElseThrow(() -> new IllegalArgumentException("Endpoint not found"));

        simulateDelayAndError(endpoint);

        EntitySchema entitySchema = endpoint.getResponseSchema();

        if(endpoint.getResponseType() == ApiEndpoint.ResponseType.SINGLE){
            return dataGeneratorService.generateOne(entitySchema);
        } else {
            return dataGeneratorService.generateMany(entitySchema, endpoint.getDefaultCount());
        }
    }

    public Object handlePostRequest(String path, String body) {
        ApiEndpoint endpoint = apiEndpointRepository.findByPathAndHttpMethod(path, ApiEndpoint.HttpMethod.POST)
                .orElseThrow(() -> new IllegalArgumentException("Endpoint not found"));

        simulateDelayAndError(endpoint);

        if(endpoint.getRequestSchema() != null){
            validateRequestBody(endpoint.getRequestSchema(), body);
        }

        EntitySchema entitySchema = endpoint.getResponseSchema();

        if(endpoint.getResponseType() == ApiEndpoint.ResponseType.SINGLE){
            return dataGeneratorService.generateOne(entitySchema);
        } else {
            return dataGeneratorService.generateMany(entitySchema, endpoint.getDefaultCount());
        }
    }

    private void simulateDelayAndError(ApiEndpoint endpoint){
        if(endpoint.getDelayMs() != null && endpoint.getDelayMs() > 0){
            try {
                Thread.sleep(endpoint.getDelayMs());
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        if(endpoint.getErrorRate() != null && endpoint.getErrorRate().compareTo(BigDecimal.ZERO) > 0){
            double randomValue = Math.random();
            if(randomValue < endpoint.getErrorRate().doubleValue()){
                throw new RuntimeException("Simulated error for testing purposes");
            }
        }
    }

    private void validateRequestBody(EntitySchema requestSchema, String body) {
        try{
            ObjectMapper mapper = new ObjectMapper();
            Map<String, Object> bodyMap = mapper.readValue(body, new TypeReference<>() {});
            for(SchemaField field : requestSchema.getFields()){
                if(field.getRequired() && !bodyMap.containsKey(field.getFieldName())){
                    throw new IllegalArgumentException("Missing required field: " + field.getFieldName());
                }
            }
        }catch (Exception e){
            throw new RuntimeException("Invalid request body", e);
        }
    }


    public ApiEndpointResponseDto createApiEndpoint(ApiEndpointRequestDto apiEndpointRequestDto){
        boolean exists = apiEndpointRepository.existsByProjectIdAndPathAndHttpMethod(apiEndpointRequestDto.getProjectId(), apiEndpointRequestDto.getPath(), ApiEndpoint.HttpMethod.valueOf(apiEndpointRequestDto.getMethod()));
        if(exists){
            throw new IllegalArgumentException("Endpoint with the same path and method already exists in the project");
        }
        Project project = projectRepository.findById(apiEndpointRequestDto.getProjectId())
                .orElseThrow(() -> new IllegalArgumentException("Project not found"));


        EntitySchema responseSchema = null;
        if(apiEndpointRequestDto.getResponseSchemaId() != null){
            responseSchema = entitySchemaRepository.findById(apiEndpointRequestDto.getResponseSchemaId())
                    .orElseThrow(() -> new IllegalArgumentException("Response Schema not found"));
        }

        EntitySchema requestSchema = null;
        if(apiEndpointRequestDto.getRequestSchemaId() != null){
            requestSchema = entitySchemaRepository.findById(apiEndpointRequestDto.getRequestSchemaId())
                    .orElseThrow(() -> new IllegalArgumentException("Request Schema not found"));
        }

        ApiEndpoint endpoint = convertToEntity(apiEndpointRequestDto, project, requestSchema, responseSchema);
        ApiEndpoint savedEndpoint = apiEndpointRepository.save(endpoint);
        return convertToResponseDto(savedEndpoint);
    }

    public ApiEndpointResponseDto getApiEndpointById(Long id){
        ApiEndpoint endpoint = apiEndpointRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Endpoint not found"));
        return convertToResponseDto(endpoint);
    }

    public ApiEndpointResponseDto updateApiEndpoint(Long id, ApiEndpointRequestDto apiEndpointRequestDto){
        ApiEndpoint endpoint = apiEndpointRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Endpoint not found"));

        if(!endpoint.getProject().getId().equals(apiEndpointRequestDto.getProjectId())){
            throw new IllegalArgumentException("Cannot change the project of the endpoint");
        }
        if(!endpoint.getPath().equals(apiEndpointRequestDto.getPath()) || !endpoint.getHttpMethod().name().equals(apiEndpointRequestDto.getMethod())){
            boolean exist = apiEndpointRepository.existsByProjectIdAndPathAndHttpMethod(apiEndpointRequestDto.getProjectId(), apiEndpointRequestDto.getPath(), ApiEndpoint.HttpMethod.valueOf(apiEndpointRequestDto.getMethod()));
            if(exist){
                throw new IllegalArgumentException("Endpoint with the same path and method already exists in the project");
            }
            endpoint.setPath(apiEndpointRequestDto.getPath());
            endpoint.setHttpMethod(ApiEndpoint.HttpMethod.valueOf(apiEndpointRequestDto.getMethod()));
        }

        EntitySchema responseSchema = null;
        if(apiEndpointRequestDto.getResponseSchemaId() != null){
            responseSchema = entitySchemaRepository.findById(apiEndpointRequestDto.getResponseSchemaId())
                    .orElseThrow(() -> new IllegalArgumentException("Response Schema not found"));
        }

        EntitySchema requestSchema = null;
        if(apiEndpointRequestDto.getRequestSchemaId() != null){
            requestSchema = entitySchemaRepository.findById(apiEndpointRequestDto.getRequestSchemaId())
                    .orElseThrow(() -> new IllegalArgumentException("Request Schema not found"));
        }
        endpoint.setRequestSchema(requestSchema);
        endpoint.setResponseSchema(responseSchema);
        endpoint.setResponseType(ApiEndpoint.ResponseType.valueOf(apiEndpointRequestDto.getResponseType()));
        endpoint.setDefaultCount(apiEndpointRequestDto.getDefaultCount() != null ? apiEndpointRequestDto.getDefaultCount() : endpoint.getDefaultCount());
        endpoint.setStatusCode(apiEndpointRequestDto.getStatusCode() != null ? apiEndpointRequestDto.getStatusCode() : endpoint.getStatusCode());
        endpoint.setDelayMs(apiEndpointRequestDto.getDelayMs() != null ? apiEndpointRequestDto.getDelayMs() : endpoint.getDelayMs());
        endpoint.setErrorRate(BigDecimal.valueOf(apiEndpointRequestDto.getErrorRate() != null ? apiEndpointRequestDto.getErrorRate() : endpoint.getErrorRate().doubleValue()));
        endpoint.setActive(apiEndpointRequestDto.getActive() != null ? apiEndpointRequestDto.getActive() : endpoint.getActive());
        apiEndpointRepository.save(endpoint);
        return convertToResponseDto(endpoint);
    }

    public void deleteApiEndpoint(Long id){
        ApiEndpoint endpoint = apiEndpointRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Endpoint not found"));
        apiEndpointRepository.delete(endpoint);
    }

    private ApiEndpoint convertToEntity(ApiEndpointRequestDto apiEndpointRequestDto, Project project, EntitySchema requestSchema, EntitySchema responseSchema) {
        ApiEndpoint apiEndpoint = new ApiEndpoint();
        apiEndpoint.setProject(project);
        apiEndpoint.setPath(apiEndpointRequestDto.getPath());
        apiEndpoint.setHttpMethod(ApiEndpoint.HttpMethod.valueOf(apiEndpointRequestDto.getMethod()));
        apiEndpoint.setRequestSchema(requestSchema);
        apiEndpoint.setResponseSchema(responseSchema);
        apiEndpoint.setDefaultCount(apiEndpointRequestDto.getDefaultCount() != null ? apiEndpointRequestDto.getDefaultCount() : 10);
        apiEndpoint.setStatusCode(apiEndpointRequestDto.getStatusCode() != null ? apiEndpointRequestDto.getStatusCode() : 200);
        apiEndpoint.setDelayMs(apiEndpointRequestDto.getDelayMs() != null ? apiEndpointRequestDto.getDelayMs() : 0);
        apiEndpoint.setResponseType(apiEndpointRequestDto.getResponseType() != null ? ApiEndpoint.ResponseType.valueOf(apiEndpointRequestDto.getResponseType()) : ApiEndpoint.ResponseType.SINGLE);
        apiEndpoint.setErrorRate(BigDecimal.valueOf(apiEndpointRequestDto.getErrorRate() != null ? apiEndpointRequestDto.getErrorRate() : 0.00));
        apiEndpoint.setActive(apiEndpointRequestDto.getActive() != null ? apiEndpointRequestDto.getActive() : true);
        return apiEndpoint;
    }

    private ApiEndpointResponseDto convertToResponseDto(ApiEndpoint apiEndpoint) {
        ApiEndpointResponseDto dto = new ApiEndpointResponseDto();
        dto.setId(apiEndpoint.getId());
        dto.setPath(apiEndpoint.getPath());
        dto.setMethod(apiEndpoint.getHttpMethod().name());
        dto.setCreatedAt(apiEndpoint.getCreatedAt());
        dto.setUpdatedAt(apiEndpoint.getUpdatedAt());
        return dto;
    }


}
