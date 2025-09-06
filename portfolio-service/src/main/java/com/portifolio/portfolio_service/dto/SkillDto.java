package com.portifolio.portfolio_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SkillDto {
    private Long id;
    private String name;
    private String category;
    private Integer proficiencyLevel;
    private String iconUrl;
    private Integer yearsOfExperience;
}
