package com.cpd.chatread;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RabbitListener(queues = "chat-queue")
@RequiredArgsConstructor
@Slf4j
public class MessageReader {

    @RabbitHandler
    private void receiveMsg(String msg) {
        log.info("am primit un msg " + msg);
    }
}
