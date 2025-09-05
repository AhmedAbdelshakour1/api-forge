package com.example.apiforge.repository;

import com.example.apiforge.entity.EntitySchema;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EntitySchemaRepository extends JpaRepository<EntitySchema, Long> {
    List<EntitySchema> findByProjectId(Long projectId);
    Optional<EntitySchema> findByNameAndProjectId(String name, Long projectId);
    boolean existsByProjectIdAndName(Long projectId, String name);

    @Query("SELECT s FROM EntitySchema s where s.project.id = :projectId")
    List<EntitySchema> findAllByProjectId(Long projectId);

    @Query("SELECT s FROM EntitySchema s JOIN s.project p where p.isPublic = true ORDER BY s.name")
    List<EntitySchema> findPublicSchemas();

    long countByProjectId(Long projectId);
}
