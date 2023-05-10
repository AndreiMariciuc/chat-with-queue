package com.cpd.chatwrite;

import com.cpd.chatwrite.config.RabbitProps;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class MessageWriter {

    private final RabbitProps rabbitProps;
    private final RabbitTemplate rabbitTemplate;

    public void sendMessage(String message) {
        rabbitTemplate.setExchange(rabbitProps.getExchange());
        rabbitTemplate.setRoutingKey(rabbitProps.getRoutingKey());
        rabbitTemplate.convertAndSend(message);
    }
}
