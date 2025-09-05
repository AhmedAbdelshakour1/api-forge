package com.example.apiforge.repository;

import com.example.apiforge.entity.ApiEndpoint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ApiEndpointRepository extends JpaRepository<ApiEndpoint, Long> {
    List<ApiEndpoint> findByProjectId(Long projectId);
    Optional<ApiEndpoint> findByProjectIdAndPathAndHttpMethod(Long projectId, String path, String httpMethod);

    List<ApiEndpoint> findByResponseSchemaId(Long schemaId);
    List<ApiEndpoint> findByRequestSchemaId(Long schemaId);
}
