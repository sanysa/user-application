package com.bitlab.project.config;

import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KeycloakClientConfig {

    @Bean
    public Keycloak keycloak() {
        return KeycloakBuilder.builder()
                .serverUrl("http://keycloak:8080")
                .realm("master") // административный realm
                .clientId("admin-cli")
                .username("admin")
                .password("password")
                .grantType(OAuth2Constants.PASSWORD)
                .build();
    }
}
