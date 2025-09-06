package com.portifolio.portfolio_service.service;

import com.portifolio.portfolio_service.dto.UserRegistrationDto;
import com.portifolio.portfolio_service.exception.KeycloakIntegrationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.*;

/**
 * Service for user registration with Keycloak integration.
 * Handles user creation and role assignment in Keycloak.
 */

@Service
@RequiredArgsConstructor
@Slf4j
public class UserRegistrationService {

    private final RestTemplate restTemplate;

    @Value("${keycloak.auth-server-url}")
    private String keycloakUrl;

    @Value("${keycloak.realm}")
    private String realm;

    @Value("${keycloak.resource}")
    private String clientId;

    @Value("${keycloak.credentials.secret}")
    private String clientSecret;

    @Value("${keycloak.admin-username}")
    private String adminUsername;

    @Value("${keycloak.admin-password}")
    private String adminPassword;

    /**
     * Register a new user in Keycloak.
     * This is a transactional operation - if any step fails, the user will be deleted.
     *
     * @param registrationDto User registration data
     * @throws KeycloakIntegrationException if registration fails
     */
    public void registerUser(UserRegistrationDto registrationDto) {
        log.info("Starting user registration for username: {}", registrationDto.getUsername());

        try {
            // Get admin token
            String adminToken = getAdminToken();
            log.info("Successfully obtained admin token");

            // Create user and assign role
            createUserWithRole(registrationDto, adminToken);

            log.info("User registration completed successfully for username: {}", registrationDto.getUsername());
        } catch (Exception e) {
            log.error("Error during user registration: {}", e.getMessage(), e);
            throw new KeycloakIntegrationException("User registration failed: " + e.getMessage(), e);
        }
    }

    /**
     * Get an admin token from Keycloak.
     *
     * @return Admin access token
     * @throws KeycloakIntegrationException if token retrieval fails
     */
    private String getAdminToken() {
        String tokenUrl = keycloakUrl + "/realms/master/protocol/openid-connect/token";
        log.info("Getting admin token from URL: {}", tokenUrl);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("grant_type", "password");
        map.add("client_id", "admin-cli");
        map.add("username", adminUsername);
        map.add("password", adminPassword);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);

        try {
            ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                    tokenUrl,
                    HttpMethod.POST,
                    request,
                    new ParameterizedTypeReference<Map<String, Object>>() {}
            );

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                Object accessToken = response.getBody().get("access_token");
                if (accessToken != null) {
                    return accessToken.toString();
                }
                throw new KeycloakIntegrationException("Access token not found in response");
            } else {
                throw new KeycloakIntegrationException("Failed to get admin token. Status code: " + response.getStatusCode());
            }
        } catch (HttpClientErrorException e) {
            throw new KeycloakIntegrationException("Client error when getting admin token: " + e.getStatusCode() + " - " + e.getResponseBodyAsString(), e);
        } catch (HttpServerErrorException e) {
            throw new KeycloakIntegrationException("Server error when getting admin token: " + e.getStatusCode() + " - " + e.getResponseBodyAsString(), e);
        } catch (RestClientException e) {
            throw new KeycloakIntegrationException("REST client error when getting admin token: " + e.getMessage(), e);
        }
    }

    /**
     * Create a user in Keycloak and assign the manager role.
     *
     * @param registrationDto User registration data
     * @param adminToken Admin access token
     * @throws KeycloakIntegrationException if user creation or role assignment fails
     */
    private void createUserWithRole(UserRegistrationDto registrationDto, String adminToken) {
        // Step 1: Create the user
        String userId = createUser(registrationDto, adminToken);
        log.info("User created with ID: {}", userId);

        // Step 2: Assign role to the user
        assignClientRole(userId, adminToken);
        log.info("Role assigned successfully to user: {}", registrationDto.getUsername());
    }

    /**
     * Create a user in Keycloak.
     *
     * @param registrationDto User registration data
     * @param adminToken Admin access token
     * @return User ID of the created user
     * @throws KeycloakIntegrationException if user creation fails
     */
    private String createUser(UserRegistrationDto registrationDto, String adminToken) {
        String usersUrl = keycloakUrl + "/admin/realms/" + realm + "/users";
        log.info("Creating user at URL: {}", usersUrl);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(adminToken);

        // Create credentials map
        Map<String, Object> credentials = new HashMap<>();
        credentials.put("type", "password");
        credentials.put("value", registrationDto.getPassword());
        credentials.put("temporary", false);

        // Create user map
        Map<String, Object> user = new HashMap<>();
        user.put("username", registrationDto.getUsername());
        user.put("email", registrationDto.getEmail());
        user.put("firstName", registrationDto.getFirstName());
        user.put("lastName", registrationDto.getLastName());
        user.put("enabled", true);
        user.put("emailVerified", true);
        user.put("credentials", Collections.singletonList(credentials));

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(user, headers);

        try {
            ResponseEntity<Void> response = restTemplate.exchange(
                    usersUrl,
                    HttpMethod.POST,
                    request,
                    Void.class
            );

            if (response.getStatusCode() == HttpStatus.CREATED) {
                String locationHeader = response.getHeaders().getFirst("Location");
                if (locationHeader != null) {
                    return locationHeader.substring(locationHeader.lastIndexOf("/") + 1);
                }
                throw new KeycloakIntegrationException("User created but location header not found");
            } else {
                throw new KeycloakIntegrationException("Failed to create user. Status code: " + response.getStatusCode());
            }
        } catch (HttpClientErrorException e) {
            throw new KeycloakIntegrationException("Client error when creating user: " + e.getStatusCode() + " - " + e.getResponseBodyAsString(), e);
        } catch (HttpServerErrorException e) {
            throw new KeycloakIntegrationException("Server error when creating user: " + e.getStatusCode() + " - " + e.getResponseBodyAsString(), e);
        } catch (RestClientException e) {
            throw new KeycloakIntegrationException("REST client error when creating user: " + e.getMessage(), e);
        }
    }

    /**
     * Assign the manager role to a user.
     *
     * @param userId User ID
     * @param adminToken Admin access token
     * @throws KeycloakIntegrationException if role assignment fails
     */
    private void assignClientRole(String userId, String adminToken) {
        try {
            // Get the manager role
            Map<String, Object> clientRole = findClientRole(adminToken);

            // Assign the role to the user
            String assignRoleUrl = keycloakUrl + "/admin/realms/" + realm + "/users/" + userId + "/role-mappings/realm";
            log.info("Assigning role at URL: {}", assignRoleUrl);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(adminToken);

            HttpEntity<List<Map<String, Object>>> request = new HttpEntity<>(
                    Collections.singletonList(clientRole),
                    headers
            );

            ResponseEntity<Void> response = restTemplate.exchange(
                    assignRoleUrl,
                    HttpMethod.POST,
                    request,
                    Void.class
            );

            if (response.getStatusCode() != HttpStatus.NO_CONTENT) {
                throw new KeycloakIntegrationException("Failed to assign role to user. Status code: " + response.getStatusCode());
            }
        } catch (HttpClientErrorException e) {
            throw new KeycloakIntegrationException("Client error when assigning role: " + e.getStatusCode() + " - " + e.getResponseBodyAsString(), e);
        } catch (HttpServerErrorException e) {
            throw new KeycloakIntegrationException("Server error when assigning role: " + e.getStatusCode() + " - " + e.getResponseBodyAsString(), e);
        } catch (RestClientException e) {
            throw new KeycloakIntegrationException("REST client error when assigning role: " + e.getMessage(), e);
        }
    }

    /**
     * Find the manager role in Keycloak.
     *
     * @param adminToken Admin access token
     * @return Manager role representation
     * @throws KeycloakIntegrationException if the manager role is not found
     */
    private Map<String, Object> findClientRole(String adminToken) {
        String rolesUrl = keycloakUrl + "/admin/realms/" + realm + "/roles";
        log.info("Fetching roles from URL: {}", rolesUrl);

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(adminToken);

        HttpEntity<Void> request = new HttpEntity<>(headers);

        try {
            ResponseEntity<List<Map<String, Object>>> response = restTemplate.exchange(
                    rolesUrl,
                    HttpMethod.GET,
                    request,
                    new ParameterizedTypeReference<List<Map<String, Object>>>() {}
            );

            if (response.getStatusCode() != HttpStatus.OK || response.getBody() == null) {
                throw new KeycloakIntegrationException("Failed to fetch roles. Status code: " + response.getStatusCode());
            }

            // Find the client role
            for (Map<String, Object> role : response.getBody()) {
                if ("client".equals(role.get("name"))) {
                    return role;
                }
            }

            // If we get here, the client role was not found
            List<String> availableRoles = new ArrayList<>();
            for (Map<String, Object> role : response.getBody()) {
                if (role.containsKey("name")) {
                    availableRoles.add(role.get("name").toString());
                }
            }

            throw new KeycloakIntegrationException(
                    "Manager role not found. Available roles: " + String.join(", ", availableRoles) +
                            ". Please create the 'client' role in Keycloak."
            );
        } catch (HttpClientErrorException e) {
            throw new KeycloakIntegrationException("Client error when fetching roles: " + e.getStatusCode() + " - " + e.getResponseBodyAsString(), e);
        } catch (HttpServerErrorException e) {
            throw new KeycloakIntegrationException("Server error when fetching roles: " + e.getStatusCode() + " - " + e.getResponseBodyAsString(), e);
        } catch (RestClientException e) {
            throw new KeycloakIntegrationException("REST client error when fetching roles: " + e.getMessage(), e);
        }
    }

    /**
     * Delete a user from Keycloak.
     *
     * @param userId User ID to delete
     * @param adminToken Admin access token
     * @throws KeycloakIntegrationException if user deletion fails
     */
    private void deleteUser(String userId, String adminToken) {
        String deleteUrl = keycloakUrl + "/admin/realms/" + realm + "/users/" + userId;
        log.info("Deleting user with ID: {} at URL: {}", userId, deleteUrl);

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(adminToken);

        HttpEntity<Void> request = new HttpEntity<>(headers);

        try {
            ResponseEntity<Void> response = restTemplate.exchange(
                    deleteUrl,
                    HttpMethod.DELETE,
                    request,
                    Void.class
            );

            if (response.getStatusCode() == HttpStatus.NO_CONTENT) {
                log.info("User deleted successfully: {}", userId);
            } else {
                log.error("Failed to delete user. Status code: {}", response.getStatusCode());
                throw new KeycloakIntegrationException("Failed to delete user. Status code: " + response.getStatusCode());
            }
        } catch (HttpClientErrorException e) {
            log.error("Client error when deleting user: {} - {}", e.getStatusCode(), e.getResponseBodyAsString(), e);
            throw new KeycloakIntegrationException("Failed to delete user: " + e.getMessage(), e);
        } catch (HttpServerErrorException e) {
            log.error("Server error when deleting user: {} - {}", e.getStatusCode(), e.getResponseBodyAsString(), e);
            throw new KeycloakIntegrationException("Failed to delete user: " + e.getMessage(), e);
        } catch (RestClientException e) {
            log.error("REST client error when deleting user: {}", e.getMessage(), e);
            throw new KeycloakIntegrationException("Failed to delete user: " + e.getMessage(), e);
        }
    }

}
