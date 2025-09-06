package com.portifolio.portfolio_service.controller;

import com.portifolio.portfolio_service.dto.UserRegistrationDto;
import com.portifolio.portfolio_service.exception.KeycloakIntegrationException;
import com.portifolio.portfolio_service.service.UserRegistrationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/public/auth")
@RequiredArgsConstructor
@Slf4j
public class UserRegistrationController {

    private final UserRegistrationService userRegistrationService;

    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> registerUser(@Valid @RequestBody UserRegistrationDto registrationDto, BindingResult bindingResult) {
        // Check for validation errors
        if (bindingResult.hasErrors()) {
            Map<String, Object> errorResponse = new HashMap<>();
            Map<String, String> validationErrors = new HashMap<>();

            for (FieldError error : bindingResult.getFieldErrors()) {
                validationErrors.put(error.getField(), error.getDefaultMessage());
            }

            errorResponse.put("error", "Validation failed");
            errorResponse.put("validationErrors", validationErrors);

            log.warn("Validation failed for registration request: {}", validationErrors);
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }
        log.info("Received registration request for username: {}", registrationDto.getUsername());

        try {
            userRegistrationService.registerUser(registrationDto);

            Map<String, Object> response = new HashMap<>();
            response.put("message", "User registered successfully");
            response.put("username", registrationDto.getUsername());

            log.info("Registration successful for username: {}", registrationDto.getUsername());
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (Exception e) {
            log.error("Registration failed for username: {}", registrationDto.getUsername(), e);

            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Registration failed");
            errorResponse.put("message", e.getMessage());

            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @ExceptionHandler(KeycloakIntegrationException.class)
    public ResponseEntity<Map<String, Object>> handleKeycloakException(KeycloakIntegrationException e) {
        log.error("Keycloak integration error in registration controller", e);

        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("error", "Registration failed");
        errorResponse.put("message", e.getMessage());
        errorResponse.put("type", "keycloak_error");

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleException(Exception e) {
        log.error("Unhandled exception in registration controller", e);

        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("error", "Registration failed");
        errorResponse.put("message", e.getMessage());
        errorResponse.put("type", "server_error");

        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
