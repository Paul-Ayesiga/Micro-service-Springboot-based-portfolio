package com.portifolio.portfolio_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileDto {
    private Long id;
    private String fullName;
    private String username;
    private String bio;
    private String title;
    private String location;
    private String email;
    private String githubUrl;
    private String linkedinUrl;
    private String twitterUrl;
    private String websiteUrl;
    private String resumeUrl;
    private String profileImageUrl;
}
