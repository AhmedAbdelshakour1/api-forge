package com.example.apiforge.service;

import com.example.apiforge.dto.projectDTO.ProjectRequestDto;
import com.example.apiforge.dto.projectDTO.ProjectResponseDto;
import com.example.apiforge.entity.Project;
import com.example.apiforge.entity.User;
import com.example.apiforge.repository.ProjectRepository;
import com.example.apiforge.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectService {
    private final ProjectRepository  projectRepository;
    private final UserRepository userRepository;

    public ProjectService(ProjectRepository projectRepository, UserRepository userRepository) {
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
    }

    public ProjectResponseDto createProject(ProjectRequestDto requestDto) {
        User user = userRepository.findById(requestDto.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("User Not Found"));

        boolean exists = projectRepository.existsByNameAndUserId(requestDto.getName(), user.getId());
        if(exists){
            throw new IllegalArgumentException("Project with the same name already exists for this user");
        }
        Project newProject = new Project();
        newProject.setName(requestDto.getName());
        newProject.setDescription(requestDto.getDescription());
        newProject.setBaseUrl(requestDto.getBaseUrl());
        newProject.setIsPublic(requestDto.getPublic() != null ? requestDto.getPublic() : false);
        newProject.setUser(user);
        Project savedProject = projectRepository.save(newProject);
        return convertToResponseDto(savedProject);
    }

    public ProjectResponseDto getProjectById(Long id){
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Project Not Found"));
        return convertToResponseDto(project);
    }

    public List<ProjectResponseDto> getProjectsByUserId(Long userId){
        List<Project> projects = projectRepository.findByUserId(userId);
        return projects.stream().map(this::convertToResponseDto).toList();
    }

    public ProjectResponseDto updateProject(Long id, ProjectRequestDto requestDto) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Project Not Found"));

        if(!project.getUser().getId().equals(requestDto.getUserId())){
            throw new IllegalArgumentException("Cannot change the owner of the project");
        }

        if(!project.getName().equals(requestDto.getName())){
            boolean exist = projectRepository.existsByNameAndUserId(requestDto.getName(), requestDto.getUserId());
            if(exist){
                throw new IllegalArgumentException("Project with the same name already exists for this user");
            }
            project.setName(requestDto.getName());
        }

        project.setDescription(requestDto.getDescription());
        project.setBaseUrl(requestDto.getBaseUrl());
        if(requestDto.getPublic() != null){
            project.setIsPublic(requestDto.getPublic());
        }
        Project updatedProject = projectRepository.save(project);
        return convertToResponseDto(updatedProject);
    }

    public void deleteProject(Long id) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Project Not Found"));
        projectRepository.delete(project);
    }

    private ProjectResponseDto convertToResponseDto(Project savedProject) {
        ProjectResponseDto responseDto = new ProjectResponseDto();
        responseDto.setId(savedProject.getId());
        responseDto.setName(savedProject.getName());
        responseDto.setDescription(savedProject.getDescription());
        responseDto.setBaseUrl(savedProject.getBaseUrl());
        responseDto.setPublic(savedProject.getIsPublic());
        responseDto.setUserId(savedProject.getUser().getId());
        responseDto.setCreatedAt(savedProject.getCreatedAt());
        responseDto.setUpdatedAt(savedProject.getUpdatedAt());
        return responseDto;
    }
}
