package com.portifolio.portfolio_service.controller;

import com.portifolio.portfolio_service.dto.ExperienceDto;
import com.portifolio.portfolio_service.service.ExperienceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ExperienceController {

    private final ExperienceService experienceService;

    @GetMapping("/public/experiences")
    public ResponseEntity<List<ExperienceDto>> getAllExperiences() {
        return ResponseEntity.ok(experienceService.getAllExperiences());
    }

    @GetMapping("/public/experiences/current")
    public ResponseEntity<List<ExperienceDto>> getCurrentExperiences() {
        return ResponseEntity.ok(experienceService.getCurrentExperiences());
    }

    @GetMapping("/public/experiences/{id}")
    public ResponseEntity<ExperienceDto> getExperienceById(@PathVariable Long id) {
        return ResponseEntity.ok(experienceService.getExperienceById(id));
    }

    @PostMapping("/admin/experiences")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ExperienceDto> createExperience(@RequestBody ExperienceDto experienceDto) {
        return new ResponseEntity<>(experienceService.createExperience(experienceDto), HttpStatus.CREATED);
    }

    @PutMapping("/admin/experiences/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ExperienceDto> updateExperience(
            @PathVariable Long id,
            @RequestBody ExperienceDto experienceDto) {
        return ResponseEntity.ok(experienceService.updateExperience(id, experienceDto));
    }

    @DeleteMapping("/admin/experiences/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteExperience(@PathVariable Long id) {
        experienceService.deleteExperience(id);
        return ResponseEntity.noContent().build();
    }
}
