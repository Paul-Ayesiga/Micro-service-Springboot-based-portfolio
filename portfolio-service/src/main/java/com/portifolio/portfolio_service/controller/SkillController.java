package com.portifolio.portfolio_service.controller;

import com.portifolio.portfolio_service.dto.SkillDto;
import com.portifolio.portfolio_service.service.SkillService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class SkillController {

    private final SkillService skillService;

    @GetMapping("/public/skills")
    public ResponseEntity<List<SkillDto>> getAllSkills() {
        return ResponseEntity.ok(skillService.getAllSkills());
    }

    @GetMapping("/public/skills/category/{category}")
    public ResponseEntity<List<SkillDto>> getSkillsByCategory(@PathVariable String category) {
        return ResponseEntity.ok(skillService.getSkillsByCategory(category));
    }

    @GetMapping("/public/skills/level/{level}")
    public ResponseEntity<List<SkillDto>> getSkillsByProficiencyLevel(@PathVariable Integer level) {
        return ResponseEntity.ok(skillService.getSkillsByProficiencyLevel(level));
    }

    @GetMapping("/public/skills/{id}")
    public ResponseEntity<SkillDto> getSkillById(@PathVariable Long id) {
        return ResponseEntity.ok(skillService.getSkillById(id));
    }

    @PostMapping("/admin/skills")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SkillDto> createSkill(@RequestBody SkillDto skillDto) {
        return new ResponseEntity<>(skillService.createSkill(skillDto), HttpStatus.CREATED);
    }

    @PutMapping("/admin/skills/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SkillDto> updateSkill(
            @PathVariable Long id,
            @RequestBody SkillDto skillDto) {
        return ResponseEntity.ok(skillService.updateSkill(id, skillDto));
    }

    @DeleteMapping("/admin/skills/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteSkill(@PathVariable Long id) {
        skillService.deleteSkill(id);
        return ResponseEntity.noContent().build();
    }
}
