package com.example.apiforge.repository;

import com.example.apiforge.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {
    Optional<Project> findByName(String name);
    List<Project> findByUserId(Long userId);
    List<Project> findByIsPublic(Boolean isPublic);
    List<Project> findByUserIdAndIsPublicTrue(Long userId);
    boolean existsByNameAndUserId(String name, Long userId);
}
