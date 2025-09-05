package com.example.apiforge.repository;

import com.example.apiforge.entity.SchemaField;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SchemaFieldRepository extends JpaRepository<SchemaField, Long> {
    List<SchemaField> findByEntitySchemaId(Long schemaId);
    boolean existsByEntitySchemaIdAndFieldName(Long schemaId, String fieldName);
}
