package com.bitlab.project.controller;

import com.bitlab.project.model.dto.ChangePasswordRequest;
import com.bitlab.project.model.dto.LoginRequest;
import com.bitlab.project.model.dto.RegisterRequest;
import com.bitlab.project.controller.com.bitlab.project.model.dto.ChangePasswordRequestPasswordRequest;
import com.bitlab.project.model.dto.ChangePasswordRequest;
import com.bitlab.project.model.dto.LoginRequest;
import jakarta.ws.rs.core.Response;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final Keycloak keycloak;
    private final String realm = "realm-demo";

    public AuthController(Keycloak keycloak) {
        this.keycloak = keycloak;
    }


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody com.bitlab.project.model.dto.LoginRequest request) {
        String tokenUrl = "http://keycloak:8080/realms/realm-demo/protocol/openid-connect/token";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("grant_type", "password");
        form.add("client_id", "demo");
        form.add("username", request.getUsername());
        form.add("password", request.getPassword());

        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(form, headers);
        RestTemplate restTemplate = new RestTemplate();

        try {
            ResponseEntity<String> response = restTemplate.exchange(
                    tokenUrl, HttpMethod.POST, entity, String.class);
            return ResponseEntity.ok(response.getBody());
        } catch (HttpClientErrorException e) {
            return ResponseEntity.status(e.getStatusCode()).body("Login failed: " + e.getResponseBodyAsString());
        }
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(@RequestParam String refreshToken) {
        String tokenUrl = "http://keycloak:8080/realms/realm-demo/protocol/openid-connect/token";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("grant_type", "refresh_token");
        form.add("client_id", "demo");
        form.add("refresh_token", refreshToken);

        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(form, headers);
        RestTemplate restTemplate = new RestTemplate();

        try {
            ResponseEntity<String> response = restTemplate.exchange(
                    tokenUrl, HttpMethod.POST, entity, String.class);
            return ResponseEntity.ok(response.getBody());
        } catch (HttpClientErrorException e) {
            return ResponseEntity.status(e.getStatusCode()).body("Refresh failed: " + e.getResponseBodyAsString());
        }
    }

    @PutMapping("/change-password")
    public ResponseEntity<?> changePassword(@RequestBody ChangePasswordRequestPasswordRequest request) {
        List<UserRepresentation> users = keycloak.realm(realm)
                .users()
                .search(request.getUsername());

        if (users.isEmpty()) {
            return ResponseEntity.badRequest().body("User not found");
        }

        String userId = users.get(0).getId();

        CredentialRepresentation newPassword = new CredentialRepresentation();
        newPassword.setType(CredentialRepresentation.PASSWORD);
        newPassword.setValue(request.getNewPassword());
        newPassword.setTemporary(false);

        keycloak.realm(realm).users().get(userId).resetPassword(newPassword);

        return ResponseEntity.ok("Password updated");
    }


}
