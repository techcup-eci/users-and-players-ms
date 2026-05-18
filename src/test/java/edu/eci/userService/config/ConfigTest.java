package edu.eci.userService.config;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

class ConfigTest {

    @Test
    void aopConfigTest() {
        AopConfig config = new AopConfig();
        assertThat(config).isNotNull();
    }

    @Test
    void gatewayConfigTest() {
        String url = "http://gateway:8080";
        GatewayConfig config = new GatewayConfig(url);
        assertThat(config.getGatewayUrl()).isEqualTo(url);
    }
}
