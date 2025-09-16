package com.example.apiforge.controller;

import com.example.apiforge.service.ApiEndpointService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
