package com.portifolio.portfolio_service.config;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableCaching
public class CacheConfig {
    // Spring Boot auto-configuration will set up a suitable cache provider
    // based on the dependencies in the classpath
}
