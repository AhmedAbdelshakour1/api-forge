package com.example.apiforge.controller;

import com.example.apiforge.dto.entityschemaDTO.EntitySchemaRequestDto;
import com.example.apiforge.dto.entityschemaDTO.EntitySchemaResponseDto;
import com.example.apiforge.service.EntitySchemaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/schemas")
public class EntitySchemaController {
    private final EntitySchemaService entitySchemaService;

    public EntitySchemaController(EntitySchemaService entitySchemaService) {
        this.entitySchemaService = entitySchemaService;
    }

    @PostMapping
    public ResponseEntity<EntitySchemaResponseDto> createSchema(@RequestBody EntitySchemaRequestDto requestDto) {
        EntitySchemaResponseDto responseDto = entitySchemaService.createEntitySchema(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntitySchemaResponseDto> getSchema(@PathVariable Long id) {
        EntitySchemaResponseDto responseDto = entitySchemaService.getSchemaById(id);
        return ResponseEntity.ok(responseDto);
    }
    // get all schemas by project id (get ?projectId=)
    @GetMapping
    public ResponseEntity<List<EntitySchemaResponseDto>> getAllSchemasByProjectId(@RequestParam Long projectId) {
        List<EntitySchemaResponseDto> responseDtos = entitySchemaService.getSchemasByProjectId(projectId);
        return ResponseEntity.ok(responseDtos);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EntitySchemaResponseDto> updateSchema(@PathVariable Long id, @RequestBody EntitySchemaRequestDto requestDto) {
        EntitySchemaResponseDto responseDto = entitySchemaService.updateSchema(id, requestDto);
        return ResponseEntity.ok(responseDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSchema(@PathVariable Long id) {
        entitySchemaService.deleteSchema(id);
        return ResponseEntity.noContent().build();
    }
}
