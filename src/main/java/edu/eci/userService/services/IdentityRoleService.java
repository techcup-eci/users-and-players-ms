package edu.eci.userService.services;

import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import edu.eci.userService.config.GatewayConfig;

@Service
public class IdentityRoleService {

    private final GatewayConfig gatewayConfig;
    private final RestClient restClient;

    public IdentityRoleService(GatewayConfig gatewayConfig) {
        this.gatewayConfig = gatewayConfig;
        this.restClient = RestClient.builder().build();
    }

    public void updateUserRole(long userId, String role, String authorization) {
        if (authorization == null || authorization.isBlank()) {
            throw new IllegalArgumentException("Authorization header is required");
        }
        if (role == null || role.isBlank()) {
            throw new IllegalArgumentException("Role is required");
        }

        String url = gatewayConfig.getGatewayUrl() + "/api/identity/users/" + userId + "/role";
        restClient.patch()
                .uri(url)
                .header(HttpHeaders.AUTHORIZATION, authorization)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Map.of("role", role))
                .retrieve()
                .toBodilessEntity();
    }
}
