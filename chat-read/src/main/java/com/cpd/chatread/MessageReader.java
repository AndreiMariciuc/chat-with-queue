package com.cpd.chatread;

import com.cpd.chatread.models.Message;
import com.cpd.chatread.services.SubscribeService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Sinks;

@Slf4j
@Service
@RequiredArgsConstructor
@RabbitListener(queues = "${rabbitmq.queue}")
public class MessageReader {
    private final ObjectMapper jsonConverter = new ObjectMapper();

    private final SubscribeService subscribeService;

    @RabbitHandler
    private void receiveMsg(String json) {
        log.info("Am primit un msg " + json);

        Message msg;
        try {
            msg = jsonConverter.readValue(json, Message.class);
        } catch (JsonProcessingException e) {
            log.warn("Could not parse MQ message JSON.");
            return;
        }

        for(String to : msg.tos()) {
            var sink = subscribeService.getSubscription(to);
            if(sink == null) {
                log.warn("Unknown identifier: " + to);
                continue;
            }

            sink.emitNext(json, Sinks.EmitFailureHandler.FAIL_FAST);
        }
    }
}
