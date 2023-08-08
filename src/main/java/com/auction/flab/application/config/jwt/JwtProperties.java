package com.auction.flab.application.config.jwt;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@ConfigurationProperties("jwt")
@Configuration
public class JwtProperties {
    private String issuer;
    private String secretKey;
}
