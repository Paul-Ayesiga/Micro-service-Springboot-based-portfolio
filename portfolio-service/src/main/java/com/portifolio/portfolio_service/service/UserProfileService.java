package com.portifolio.portfolio_service.service;

import com.portifolio.portfolio_service.dto.UserProfileDto;
import com.portifolio.portfolio_service.model.UserProfile;
import com.portifolio.portfolio_service.repository.UserProfileRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserProfileService {

    private final UserProfileRepository userProfileRepository;

    public UserProfileDto getUserProfileByUsername(String username) {
        UserProfile userProfile = userProfileRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("User profile not found with username: " + username));
        return mapToDto(userProfile);
    }

    public UserProfileDto getUserProfileById(Long id) {
        UserProfile userProfile = userProfileRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User profile not found with id: " + id));
        return mapToDto(userProfile);
    }

    @Transactional
    public UserProfileDto createUserProfile(UserProfileDto userProfileDto) {
        UserProfile userProfile = mapToEntity(userProfileDto);
        UserProfile savedUserProfile = userProfileRepository.save(userProfile);
        return mapToDto(savedUserProfile);
    }

    @Transactional
    public UserProfileDto updateUserProfile(Long id, UserProfileDto userProfileDto) {
        UserProfile existingUserProfile = userProfileRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User profile not found with id: " + id));
        
        updateEntityFromDto(existingUserProfile, userProfileDto);
        UserProfile updatedUserProfile = userProfileRepository.save(existingUserProfile);
        return mapToDto(updatedUserProfile);
    }

    private UserProfileDto mapToDto(UserProfile userProfile) {
        return UserProfileDto.builder()
                .id(userProfile.getId())
                .fullName(userProfile.getFullName())
                .username(userProfile.getUsername())
                .bio(userProfile.getBio())
                .title(userProfile.getTitle())
                .location(userProfile.getLocation())
                .email(userProfile.getEmail())
                .githubUrl(userProfile.getGithubUrl())
                .linkedinUrl(userProfile.getLinkedinUrl())
                .twitterUrl(userProfile.getTwitterUrl())
                .websiteUrl(userProfile.getWebsiteUrl())
                .resumeUrl(userProfile.getResumeUrl())
                .profileImageUrl(userProfile.getProfileImageUrl())
                .build();
    }

    private UserProfile mapToEntity(UserProfileDto userProfileDto) {
        return UserProfile.builder()
                .id(userProfileDto.getId())
                .fullName(userProfileDto.getFullName())
                .username(userProfileDto.getUsername())
                .bio(userProfileDto.getBio())
                .title(userProfileDto.getTitle())
                .location(userProfileDto.getLocation())
                .email(userProfileDto.getEmail())
                .githubUrl(userProfileDto.getGithubUrl())
                .linkedinUrl(userProfileDto.getLinkedinUrl())
                .twitterUrl(userProfileDto.getTwitterUrl())
                .websiteUrl(userProfileDto.getWebsiteUrl())
                .resumeUrl(userProfileDto.getResumeUrl())
                .profileImageUrl(userProfileDto.getProfileImageUrl())
                .build();
    }

    private void updateEntityFromDto(UserProfile userProfile, UserProfileDto userProfileDto) {
        userProfile.setFullName(userProfileDto.getFullName());
        userProfile.setBio(userProfileDto.getBio());
        userProfile.setTitle(userProfileDto.getTitle());
        userProfile.setLocation(userProfileDto.getLocation());
        userProfile.setEmail(userProfileDto.getEmail());
        userProfile.setGithubUrl(userProfileDto.getGithubUrl());
        userProfile.setLinkedinUrl(userProfileDto.getLinkedinUrl());
        userProfile.setTwitterUrl(userProfileDto.getTwitterUrl());
        userProfile.setWebsiteUrl(userProfileDto.getWebsiteUrl());
        userProfile.setResumeUrl(userProfileDto.getResumeUrl());
        userProfile.setProfileImageUrl(userProfileDto.getProfileImageUrl());
    }
}
