package com.bitlab.project.controller;


import com.bitlab.project.model.dto.ChangePasswordRequest;
import com.bitlab.project.model.dto.RegisterRequest;
import com.bitlab.project.model.dto.UpdateUserProfileRequest;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.core.Response;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final Keycloak keycloak;
    private final String realm = "realm-demo";

    public UserController(Keycloak keycloak) {
        this.keycloak = keycloak;
    }


    @PostMapping("/register")
    @PreAuthorize("hasRole('role_admin')")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        UserRepresentation user = new UserRepresentation();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setEmailVerified(true);
        user.setEnabled(true);

        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());

        CredentialRepresentation credential = new CredentialRepresentation();
        credential.setType(CredentialRepresentation.PASSWORD);
        credential.setValue(request.getPassword());
        credential.setTemporary(false);

        user.setCredentials(List.of(credential));

        try {
            Response response = keycloak.realm(realm).users().create(user);
            if (response.getStatus() == 201) {
                return ResponseEntity.ok("User registered");
            } else {
                return ResponseEntity.status(response.getStatus()).body("Registration failed");
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Exception: " + e.getMessage());
        }
    }

    @PutMapping("/change-password")
    public ResponseEntity<?> changePassword(@RequestBody ChangePasswordRequest request) {
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

    @PutMapping("/update-profile")
    public ResponseEntity<?> updateProfile(
            @AuthenticationPrincipal Jwt principal,
            @RequestBody UpdateUserProfileRequest request
    ) {
        String userId = principal.getSubject();

        try {
            UserResource userResource = keycloak.realm(realm).users().get(userId);
            UserRepresentation user = userResource.toRepresentation();

            if (request.getEmail() != null) user.setEmail(request.getEmail());
            if (request.getFirstName() != null) user.setFirstName(request.getFirstName());
            if (request.getLastName() != null) user.setLastName(request.getLastName());

            user.setEmailVerified(true);

            userResource.update(user);

            return ResponseEntity.ok("User profile updated.");
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to update profile: " + e.getMessage());
        }
    }



}
