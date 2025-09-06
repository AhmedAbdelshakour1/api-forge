package com.example.apiforge.service;

import com.example.apiforge.dto.entityschemaDTO.EntitySchemaRequestDto;
import com.example.apiforge.dto.entityschemaDTO.EntitySchemaResponseDto;
import com.example.apiforge.entity.EntitySchema;
import com.example.apiforge.entity.Project;
import com.example.apiforge.repository.EntitySchemaRepository;
import com.example.apiforge.repository.ProjectRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EntitySchemaService {
    private final EntitySchemaRepository entitySchemaRepository;
    private final ProjectRepository projectRepository;

    public EntitySchemaService(EntitySchemaRepository entitySchemaRepository, ProjectRepository projectRepository) {
        this.entitySchemaRepository = entitySchemaRepository;
        this.projectRepository = projectRepository;
    }

    public EntitySchemaResponseDto createEntitySchema(EntitySchemaRequestDto requestDto) {
        boolean exists = entitySchemaRepository.existsByProjectIdAndName(requestDto.getProjectId(), requestDto.getName());
        if(exists){
            throw new IllegalArgumentException("Schema with the same name already exists in the project");
        }
        Project project = projectRepository.findById(requestDto.getProjectId())
                .orElseThrow(() -> new IllegalArgumentException("Project not found"));

        EntitySchema entity = convertToEntity(requestDto, project);
        EntitySchema savedEntity = entitySchemaRepository.save(entity);
        return convertToResponseDto(savedEntity);
    }

    public EntitySchemaResponseDto getSchemaById(Long id){
        EntitySchema entity = entitySchemaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Schema not found"));
        return convertToResponseDto(entity);
    }

    public List<EntitySchemaResponseDto> getSchemasByProjectId(Long projectId){
        List<EntitySchema> entities = entitySchemaRepository.findByProjectId(projectId);
        return entities.stream().map(this::convertToResponseDto).toList();
    }

    public EntitySchemaResponseDto updateSchema(Long id, EntitySchemaRequestDto requestDto) {
        EntitySchema entity = entitySchemaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Schema not found"));

        if(!entity.getProject().getId().equals(requestDto.getProjectId())){
            throw new IllegalArgumentException("Cannot change the project of the schema");
        }
        if(!entity.getName().equals(requestDto.getName())){
            boolean exist = entitySchemaRepository.existsByProjectIdAndName(requestDto.getProjectId(), requestDto.getName());
            if(exist){
                throw new IllegalArgumentException("Schema with the same name already exists in the project");
            }
            entity.setName(requestDto.getName());
        }
        entity.setDescription(requestDto.getDescription());
        entitySchemaRepository.save(entity);
        return convertToResponseDto(entity);
    }

    public void deleteSchema(Long id) {
        EntitySchema entity =  entitySchemaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Schema not found"));
        entitySchemaRepository.delete(entity);
    }

    public EntitySchema convertToEntity(EntitySchemaRequestDto dto, Project project) {
        EntitySchema entity = new EntitySchema();
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        entity.setProject(project);
        return entity;
    }

    public EntitySchemaResponseDto convertToResponseDto(EntitySchema entity) {
        EntitySchemaResponseDto responseDto = new EntitySchemaResponseDto();
        responseDto.setId(entity.getId());
        responseDto.setName(entity.getName());
        responseDto.setDescription(entity.getDescription());
        responseDto.setProjectId(entity.getProject().getId());
        responseDto.setCreatedAt(entity.getCreatedAt());
        return responseDto;
    }
}
