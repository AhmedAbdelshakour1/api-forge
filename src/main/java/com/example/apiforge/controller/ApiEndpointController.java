package com.example.apiforge.controller;

import com.example.apiforge.dto.apiendpointDTO.ApiEndpointRequestDto;
import com.example.apiforge.dto.apiendpointDTO.ApiEndpointResponseDto;
import com.example.apiforge.service.ApiEndpointService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/endpoints")
public class ApiEndpointController {
    // crud operations
    private final ApiEndpointService apiEndpointService;

    public ApiEndpointController(ApiEndpointService apiEndpointService) {
        this.apiEndpointService = apiEndpointService;
    }

    @PostMapping
    public ResponseEntity<ApiEndpointResponseDto> createApiEndpoint(@RequestBody ApiEndpointRequestDto apiEndpointRequestDto){
        ApiEndpointResponseDto responseDto = apiEndpointService.createApiEndpoint(apiEndpointRequestDto);
        return ResponseEntity.status(201).body(responseDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiEndpointResponseDto> getApiEndpointById(@PathVariable Long id){
        ApiEndpointResponseDto responseDto = apiEndpointService.getApiEndpointById(id);
        return ResponseEntity.ok(responseDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiEndpointResponseDto> updateApiEndpoint(@PathVariable Long id, @RequestBody ApiEndpointRequestDto apiEndpointRequestDto) {
        ApiEndpointResponseDto responseDto = apiEndpointService.updateApiEndpoint(id, apiEndpointRequestDto);
        return ResponseEntity.ok(responseDto);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteApiEndpoint(@PathVariable Long id) {
        apiEndpointService.deleteApiEndpoint(id);
        return ResponseEntity.noContent().build();
    }
}
