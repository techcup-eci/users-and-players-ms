package edu.eci.userService.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class GatewayConfig {

    private final String gatewayUrl;

    public GatewayConfig(@Value("${app.gateway.url}") String gatewayUrl) {
        this.gatewayUrl = gatewayUrl;
    }

    public String getGatewayUrl() {
        return gatewayUrl;
    }
}
