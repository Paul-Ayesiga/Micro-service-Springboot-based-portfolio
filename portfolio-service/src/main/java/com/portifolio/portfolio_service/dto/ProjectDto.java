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
public class ProjectDto {
    private Long id;
    private String title;
    private String description;
    private String summary;
    private String githubUrl;
    private String liveUrl;
    private String imageUrl;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private boolean featured;
    private Set<String> technologies = new HashSet<>();
    private Set<String> categories = new HashSet<>();
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
