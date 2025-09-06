package com.portifolio.portfolio_service.controller;

import com.portifolio.portfolio_service.dto.ProjectDto;
import com.portifolio.portfolio_service.service.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;

    @GetMapping("/public/projects")
    public ResponseEntity<List<ProjectDto>> getAllProjects() {
        return ResponseEntity.ok(projectService.getAllProjects());
    }

    @GetMapping("/public/projects/featured")
    public ResponseEntity<List<ProjectDto>> getFeaturedProjects() {
        return ResponseEntity.ok(projectService.getFeaturedProjects());
    }

    @GetMapping("/public/projects/category/{category}")
    public ResponseEntity<List<ProjectDto>> getProjectsByCategory(@PathVariable String category) {
        return ResponseEntity.ok(projectService.getProjectsByCategory(category));
    }

    @GetMapping("/public/projects/technology/{technology}")
    public ResponseEntity<List<ProjectDto>> getProjectsByTechnology(@PathVariable String technology) {
        return ResponseEntity.ok(projectService.getProjectsByTechnology(technology));
    }

    @GetMapping("/public/projects/{id}")
    public ResponseEntity<ProjectDto> getProjectById(@PathVariable Long id) {
        return ResponseEntity.ok(projectService.getProjectById(id));
    }

    @PostMapping("/admin/projects")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProjectDto> createProject(@RequestBody ProjectDto projectDto) {
        return new ResponseEntity<>(projectService.createProject(projectDto), HttpStatus.CREATED);
    }

    @PutMapping("/admin/projects/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProjectDto> updateProject(
            @PathVariable Long id,
            @RequestBody ProjectDto projectDto) {
        return ResponseEntity.ok(projectService.updateProject(id, projectDto));
    }

    @DeleteMapping("/admin/projects/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteProject(@PathVariable Long id) {
        projectService.deleteProject(id);
        return ResponseEntity.noContent().build();
    }
}
