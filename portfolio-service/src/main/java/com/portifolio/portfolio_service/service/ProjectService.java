package com.portifolio.portfolio_service.service;

import com.portifolio.portfolio_service.dto.ProjectDto;
import com.portifolio.portfolio_service.model.Project;
import com.portifolio.portfolio_service.repository.ProjectRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;

    @Cacheable("projects")
    public List<ProjectDto> getAllProjects() {
        return projectRepository.findAllWithCollections().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Cacheable("featuredProjects")
    public List<ProjectDto> getFeaturedProjects() {
        return projectRepository.findByFeaturedTrue().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public List<ProjectDto> getProjectsByCategory(String category) {
        return projectRepository.findByCategory(category).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public List<ProjectDto> getProjectsByTechnology(String technology) {
        return projectRepository.findByTechnology(technology).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Cacheable(value = "project", key = "#id")
    public ProjectDto getProjectById(Long id) {
        Project project = projectRepository.findByIdWithCollections(id);
        if (project == null) {
            throw new EntityNotFoundException("Project not found with id: " + id);
        }
        return mapToDto(project);
    }

    @Transactional
    @CacheEvict(value = {"projects", "featuredProjects"}, allEntries = true)
    public ProjectDto createProject(ProjectDto projectDto) {
        Project project = mapToEntity(projectDto);
        Project savedProject = projectRepository.save(project);
        return mapToDto(savedProject);
    }

    @Transactional
    @CacheEvict(value = {"projects", "featuredProjects", "project"}, allEntries = true)
    public ProjectDto updateProject(Long id, ProjectDto projectDto) {
        Project existingProject = projectRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Project not found with id: " + id));

        updateEntityFromDto(existingProject, projectDto);
        Project updatedProject = projectRepository.save(existingProject);
        return mapToDto(updatedProject);
    }

    @Transactional
    @CacheEvict(value = {"projects", "featuredProjects", "project"}, allEntries = true)
    public void deleteProject(Long id) {
        if (!projectRepository.existsById(id)) {
            throw new EntityNotFoundException("Project not found with id: " + id);
        }
        projectRepository.deleteById(id);
    }

    private ProjectDto mapToDto(Project project) {
        return ProjectDto.builder()
                .id(project.getId())
                .title(project.getTitle())
                .description(project.getDescription())
                .summary(project.getSummary())
                .githubUrl(project.getGithubUrl())
                .liveUrl(project.getLiveUrl())
                .imageUrl(project.getImageUrl())
                .startDate(project.getStartDate())
                .endDate(project.getEndDate())
                .featured(project.isFeatured())
                .technologies(project.getTechnologies())
                .categories(project.getCategories())
                .createdAt(project.getCreatedAt())
                .updatedAt(project.getUpdatedAt())
                .build();
    }

    private Project mapToEntity(ProjectDto projectDto) {
        return Project.builder()
                .id(projectDto.getId())
                .title(projectDto.getTitle())
                .description(projectDto.getDescription())
                .summary(projectDto.getSummary())
                .githubUrl(projectDto.getGithubUrl())
                .liveUrl(projectDto.getLiveUrl())
                .imageUrl(projectDto.getImageUrl())
                .startDate(projectDto.getStartDate())
                .endDate(projectDto.getEndDate())
                .featured(projectDto.isFeatured())
                .technologies(projectDto.getTechnologies())
                .categories(projectDto.getCategories())
                .build();
    }

    private void updateEntityFromDto(Project project, ProjectDto projectDto) {
        project.setTitle(projectDto.getTitle());
        project.setDescription(projectDto.getDescription());
        project.setSummary(projectDto.getSummary());
        project.setGithubUrl(projectDto.getGithubUrl());
        project.setLiveUrl(projectDto.getLiveUrl());
        project.setImageUrl(projectDto.getImageUrl());
        project.setStartDate(projectDto.getStartDate());
        project.setEndDate(projectDto.getEndDate());
        project.setFeatured(projectDto.isFeatured());
        project.setTechnologies(projectDto.getTechnologies());
        project.setCategories(projectDto.getCategories());
    }
}
