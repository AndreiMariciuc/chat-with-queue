package com.cpd.chatwrite;

import com.cpd.chatwrite.config.RabbitProps;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class MessageWriter {
    private final RabbitTemplate rabbitTemplate;
    private final RabbitProps rabbitProps;

    @PostConstruct
    void testSend() {
        log.info("Am trimis un msg");
        sendMessage("Un mesaj de test!\n");
    }

    public void sendMessage(String message) {
        rabbitTemplate.setExchange(rabbitProps.getExchange());
        rabbitTemplate.setRoutingKey(rabbitProps.getRoutingKey());
        rabbitTemplate.convertAndSend(message);
    }
}
