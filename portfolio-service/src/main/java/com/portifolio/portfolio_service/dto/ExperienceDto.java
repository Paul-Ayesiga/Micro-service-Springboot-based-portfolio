package com.portifolio.portfolio_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExperienceDto {
    private Long id;
    private String company;
    private String position;
    private String description;
    private String location;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private boolean current;
    private String companyLogoUrl;
    private Set<String> responsibilities = new HashSet<>();
    private Set<String> technologies = new HashSet<>();
}
