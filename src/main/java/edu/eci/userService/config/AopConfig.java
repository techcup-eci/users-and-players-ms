package edu.eci.userService.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * Habilita el soporte de AspectJ en Spring Boot.
 * Con proxyTargetClass = true se usan proxies CGLIB (sin necesidad de interfaces).
 */
@Configuration
@EnableAspectJAutoProxy(proxyTargetClass = true)
public class AopConfig {
}
