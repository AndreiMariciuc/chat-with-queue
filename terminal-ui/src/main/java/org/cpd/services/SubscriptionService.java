package org.cpd.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.time.LocalTime;

@Slf4j
public class SubscriptionService {

    private static final ParameterizedTypeReference<ServerSentEvent<String>> type = new ParameterizedTypeReference<>() {};

    private WebClient client;

    public void login(String username) {
        client = WebClient.create("http://localhost:8080/read");

        Flux<ServerSentEvent<String>> eventStream = client.get()
            .uri("/subscribe/" + username)
            .retrieve()
            .bodyToFlux(type);

        eventStream.subscribe(
                content -> log.info("Time: {} - event: name[{}], id [{}], content[{}] ",
                        LocalTime.now(), content.event(), content.id(), content.data()),
                error -> log.error("Error receiving SSE: {}", error),
                () -> log.info("Completed!!!")
        );
    }
}
