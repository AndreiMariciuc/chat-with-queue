package com.cpd.chatread.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;


@Data
@Configuration
@ConfigurationProperties(prefix = "rabbitmq")
public class RabbitProps {
    private String queue;
    private String exchange;
    private String routingKey;
}
