package com.portifolio.portfolio_service.service;

import com.portifolio.portfolio_service.dto.SkillDto;
import com.portifolio.portfolio_service.model.Skill;
import com.portifolio.portfolio_service.repository.SkillRepository;
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
public class SkillService {

    private final SkillRepository skillRepository;

    @Cacheable("skills")
    public List<SkillDto> getAllSkills() {
        return skillRepository.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public List<SkillDto> getSkillsByCategory(String category) {
        return skillRepository.findByCategory(category).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public List<SkillDto> getSkillsByProficiencyLevel(Integer level) {
        return skillRepository.findByProficiencyLevelGreaterThanEqual(level).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Cacheable(value = "skill", key = "#id")
    public SkillDto getSkillById(Long id) {
        Skill skill = skillRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Skill not found with id: " + id));
        return mapToDto(skill);
    }

    @Transactional
    @CacheEvict(value = {"skills"}, allEntries = true)
    public SkillDto createSkill(SkillDto skillDto) {
        Skill skill = mapToEntity(skillDto);
        Skill savedSkill = skillRepository.save(skill);
        return mapToDto(savedSkill);
    }

    @Transactional
    @CacheEvict(value = {"skills", "skill"}, allEntries = true)
    public SkillDto updateSkill(Long id, SkillDto skillDto) {
        Skill existingSkill = skillRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Skill not found with id: " + id));

        updateEntityFromDto(existingSkill, skillDto);
        Skill updatedSkill = skillRepository.save(existingSkill);
        return mapToDto(updatedSkill);
    }

    @Transactional
    @CacheEvict(value = {"skills", "skill"}, allEntries = true)
    public void deleteSkill(Long id) {
        if (!skillRepository.existsById(id)) {
            throw new EntityNotFoundException("Skill not found with id: " + id);
        }
        skillRepository.deleteById(id);
    }

    private SkillDto mapToDto(Skill skill) {
        return SkillDto.builder()
                .id(skill.getId())
                .name(skill.getName())
                .category(skill.getCategory())
                .proficiencyLevel(skill.getProficiencyLevel())
                .iconUrl(skill.getIconUrl())
                .yearsOfExperience(skill.getYearsOfExperience())
                .build();
    }

    private Skill mapToEntity(SkillDto skillDto) {
        return Skill.builder()
                .id(skillDto.getId())
                .name(skillDto.getName())
                .category(skillDto.getCategory())
                .proficiencyLevel(skillDto.getProficiencyLevel())
                .iconUrl(skillDto.getIconUrl())
                .yearsOfExperience(skillDto.getYearsOfExperience())
                .build();
    }

    private void updateEntityFromDto(Skill skill, SkillDto skillDto) {
        skill.setName(skillDto.getName());
        skill.setCategory(skillDto.getCategory());
        skill.setProficiencyLevel(skillDto.getProficiencyLevel());
        skill.setIconUrl(skillDto.getIconUrl());
        skill.setYearsOfExperience(skillDto.getYearsOfExperience());
    }
}
