package com.example.apiforge.controller;

import com.example.apiforge.dto.entityfieldDTO.SchemaFieldRequestDto;
import com.example.apiforge.dto.entityfieldDTO.SchemaFieldResponseDto;
import com.example.apiforge.service.SchemaFieldService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/fields")
public class SchemaFieldController {
    private final SchemaFieldService schemaFieldService;
    public SchemaFieldController(SchemaFieldService schemaFieldService) {
        this.schemaFieldService = schemaFieldService;
    }

    @PostMapping
    public ResponseEntity<SchemaFieldResponseDto> createField(@RequestBody SchemaFieldRequestDto requestDto) {
        SchemaFieldResponseDto responseDto = schemaFieldService.createSchemaField(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    @GetMapping
    public ResponseEntity<List<SchemaFieldResponseDto>> getFieldsBySchemaId(@RequestParam("schemaId") Long schemaId) {
        List<SchemaFieldResponseDto> responseDto = schemaFieldService.getFieldsBySchemaId(schemaId);
        return ResponseEntity.ok(responseDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SchemaFieldResponseDto> getFieldById(@PathVariable Long id) {
        SchemaFieldResponseDto responseDto = schemaFieldService.getFieldById(id);
        return ResponseEntity.ok(responseDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SchemaFieldResponseDto> updateField(@PathVariable Long id, @RequestBody SchemaFieldRequestDto requestDto) {
        SchemaFieldResponseDto responseDto = schemaFieldService.updateField(id, requestDto);
        return ResponseEntity.ok(responseDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<SchemaFieldResponseDto> deleteField(@PathVariable Long id) {
        schemaFieldService.deleteField(id);
        return ResponseEntity.noContent().build();
    }
}
