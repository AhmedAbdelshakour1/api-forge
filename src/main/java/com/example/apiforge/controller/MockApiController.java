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
        return ResponseEntity.ok(apiEndpointService.handleGetRequest("/" + path));
    }

    @PostMapping("/{path}")
    public ResponseEntity<Object> handlePost(@PathVariable String path, @RequestBody String body){
        return ResponseEntity.status(HttpStatus.CREATED).body(apiEndpointService.handlePostRequest("/" + path, body));
    }
}
