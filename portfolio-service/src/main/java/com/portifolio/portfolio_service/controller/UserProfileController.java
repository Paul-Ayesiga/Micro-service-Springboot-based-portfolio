package com.portifolio.portfolio_service.controller;

import com.portifolio.portfolio_service.dto.UserProfileDto;
import com.portifolio.portfolio_service.service.UserProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserProfileController {

    private final UserProfileService userProfileService;

    @GetMapping("/public/profiles/{username}")
    public ResponseEntity<UserProfileDto> getPublicUserProfile(@PathVariable String username) {
        return ResponseEntity.ok(userProfileService.getUserProfileByUsername(username));
    }

    @GetMapping("/admin/profiles/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserProfileDto> getUserProfileById(@PathVariable Long id) {
        return ResponseEntity.ok(userProfileService.getUserProfileById(id));
    }

    @PostMapping("/admin/profiles")
    @PreAuthorize("hasRole('ADMIN ')")
    public ResponseEntity<UserProfileDto> createUserProfile(@RequestBody UserProfileDto userProfileDto) {
        return new ResponseEntity<>(userProfileService.createUserProfile(userProfileDto), HttpStatus.CREATED);
    }

    @PutMapping("/admin/profiles/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserProfileDto> updateUserProfile(
            @PathVariable Long id,
            @RequestBody UserProfileDto userProfileDto) {
        return ResponseEntity.ok(userProfileService.updateUserProfile(id, userProfileDto));
    }
}
