package com.example.apiforge.controller;

import com.example.apiforge.service.ApiEndpointService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/mock")
public class MockApiController {
    private final ApiEndpointService apiEndpointService;

    public MockApiController(ApiEndpointService apiEndpointService) {
        this.apiEndpointService = apiEndpointService;
    }

    @GetMapping("/{path}")
    public ResponseEntity<Object> handleGet(@PathVariable String path){
        try {
            Object response = apiEndpointService.handleGetRequest("/" + path);
            return ResponseEntity.ok(response);
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error processing request: " + ex.getMessage());
        }
    }

    @PostMapping("/{path}")
    public ResponseEntity<Object> handlePost(@PathVariable String path, @RequestBody String body){
        try {
            Object response = apiEndpointService.handlePostRequest("/" + path, body);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error processing request: " + ex.getMessage());
        }
    }
}
