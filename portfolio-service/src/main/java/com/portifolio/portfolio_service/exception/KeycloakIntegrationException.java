package com.portifolio.portfolio_service.exception;

/**
 * Exception thrown when there is an issue with Keycloak integration.
 */
public class KeycloakIntegrationException extends RuntimeException {

    /**
     * Constructs a new KeycloakIntegrationException with the specified detail message.
     *
     * @param message the detail message
     */
    public KeycloakIntegrationException(String message) {
        super(message);
    }

    /**
     * Constructs a new KeycloakIntegrationException with the specified detail message and cause.
     *
     * @param message the detail message
     * @param cause the cause
     */
    public KeycloakIntegrationException(String message, Throwable cause) {
        super(message, cause);
    }
}
