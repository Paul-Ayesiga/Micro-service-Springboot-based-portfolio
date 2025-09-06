package com.portifolio.portfolio_service.service;

import com.portifolio.portfolio_service.dto.ExperienceDto;
import com.portifolio.portfolio_service.model.Experience;
import com.portifolio.portfolio_service.repository.ExperienceRepository;
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
public class ExperienceService {

    private final ExperienceRepository experienceRepository;

    @Cacheable("experiences")
    public List<ExperienceDto> getAllExperiences() {
        return experienceRepository.findAllOrderByStartDateDesc().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Cacheable("currentExperiences")
    public List<ExperienceDto> getCurrentExperiences() {
        return experienceRepository.findByCurrentTrue().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Cacheable(value = "experience", key = "#id")
    public ExperienceDto getExperienceById(Long id) {
        Experience experience = experienceRepository.findByIdWithCollections(id);
        if (experience == null) {
            throw new EntityNotFoundException("Experience not found with id: " + id);
        }
        return mapToDto(experience);
    }

    @Transactional
    @CacheEvict(value = {"experiences", "currentExperiences"}, allEntries = true)
    public ExperienceDto createExperience(ExperienceDto experienceDto) {
        Experience experience = mapToEntity(experienceDto);
        Experience savedExperience = experienceRepository.save(experience);
        return mapToDto(savedExperience);
    }

    @Transactional
    @CacheEvict(value = {"experiences", "currentExperiences", "experience"}, allEntries = true)
    public ExperienceDto updateExperience(Long id, ExperienceDto experienceDto) {
        Experience existingExperience = experienceRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Experience not found with id: " + id));

        updateEntityFromDto(existingExperience, experienceDto);
        Experience updatedExperience = experienceRepository.save(existingExperience);
        return mapToDto(updatedExperience);
    }

    @Transactional
    @CacheEvict(value = {"experiences", "currentExperiences", "experience"}, allEntries = true)
    public void deleteExperience(Long id) {
        if (!experienceRepository.existsById(id)) {
            throw new EntityNotFoundException("Experience not found with id: " + id);
        }
        experienceRepository.deleteById(id);
    }

    private ExperienceDto mapToDto(Experience experience) {
        return ExperienceDto.builder()
                .id(experience.getId())
                .company(experience.getCompany())
                .position(experience.getPosition())
                .description(experience.getDescription())
                .location(experience.getLocation())
                .startDate(experience.getStartDate())
                .endDate(experience.getEndDate())
                .current(experience.isCurrent())
                .companyLogoUrl(experience.getCompanyLogoUrl())
                .responsibilities(experience.getResponsibilities())
                .technologies(experience.getTechnologies())
                .build();
    }

    private Experience mapToEntity(ExperienceDto experienceDto) {
        return Experience.builder()
                .id(experienceDto.getId())
                .company(experienceDto.getCompany())
                .position(experienceDto.getPosition())
                .description(experienceDto.getDescription())
                .location(experienceDto.getLocation())
                .startDate(experienceDto.getStartDate())
                .endDate(experienceDto.getEndDate())
                .current(experienceDto.isCurrent())
                .companyLogoUrl(experienceDto.getCompanyLogoUrl())
                .responsibilities(experienceDto.getResponsibilities())
                .technologies(experienceDto.getTechnologies())
                .build();
    }

    private void updateEntityFromDto(Experience experience, ExperienceDto experienceDto) {
        experience.setCompany(experienceDto.getCompany());
        experience.setPosition(experienceDto.getPosition());
        experience.setDescription(experienceDto.getDescription());
        experience.setLocation(experienceDto.getLocation());
        experience.setStartDate(experienceDto.getStartDate());
        experience.setEndDate(experienceDto.getEndDate());
        experience.setCurrent(experienceDto.isCurrent());
        experience.setCompanyLogoUrl(experienceDto.getCompanyLogoUrl());
        experience.setResponsibilities(experienceDto.getResponsibilities());
        experience.setTechnologies(experienceDto.getTechnologies());
    }
}
