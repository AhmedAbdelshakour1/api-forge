package com.example.apiforge.controller;

import com.example.apiforge.entity.EntitySchema;
import com.example.apiforge.repository.EntitySchemaRepository;
import com.example.apiforge.service.DataGeneratorService;
import com.example.apiforge.service.EntitySchemaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/generate")
public class DataGenerationController {
    private final EntitySchemaRepository entitySchemaRepository;
    private final DataGeneratorService dataGeneratorService;

    public DataGenerationController(EntitySchemaRepository entitySchemaRepository, DataGeneratorService dataGeneratorService) {
        this.entitySchemaRepository = entitySchemaRepository;
        this.dataGeneratorService = dataGeneratorService;
    }

    @GetMapping("/{schemaId}")
    public ResponseEntity<?> generateData(@PathVariable Long schemaId, @RequestParam(defaultValue = "1") int count) {
        EntitySchema schema = entitySchemaRepository.findById(schemaId)
                .orElseThrow(() -> new IllegalArgumentException("Schema not found"));
        if(count == 1){
            return ResponseEntity.ok(dataGeneratorService.generateOne(schema));
        }else {
            return ResponseEntity.ok(dataGeneratorService.generateMany(schema, count));
        }
    }
}
