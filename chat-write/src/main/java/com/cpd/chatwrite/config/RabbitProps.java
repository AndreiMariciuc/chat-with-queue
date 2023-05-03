package com.cpd.chatwrite.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@Data
@ConfigurationProperties(prefix = "rabbitmq")
public class RabbitProps {
    private String queue;
    private String exchange;
    private String routingKey;
}