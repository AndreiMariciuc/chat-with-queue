package com.cpd.chatread;

import com.cpd.chatread.config.RabbitProps;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@RabbitListener(queues = "chat-queue")
@RequiredArgsConstructor
@Slf4j
public class MessageReader {

    private final RabbitTemplate rabbitTemplate;
    private final RabbitProps rabbitProps;

    @RabbitHandler
    private void receiveMsg(String msg) {
        log.info("am primit un msg " + msg);
    }

    @PostConstruct
    private void seeAllMessages() {
        Message message = rabbitTemplate.receive(rabbitProps.getQueue());
        while (message != null) {
            System.out.println("Received message: " + new String(message.getBody()));
            message = rabbitTemplate.receive(rabbitProps.getQueue());
        }
    }
}
