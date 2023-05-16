package org.cpd.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

@Slf4j
public class SubscriptionService {

    private static final ParameterizedTypeReference<ServerSentEvent<String>> type = new ParameterizedTypeReference<>() {
    };

    private WebClient client;

    public SubscriptionService() {
        client = WebClient.create("http://localhost:8080/read");
    }

    public void login(String username) {
        Flux<ServerSentEvent<String>> eventStream = client.get()
                .uri("/subscribe/" + username)
                .retrieve()
                .bodyToFlux(type);

        eventStream.subscribe(
                content -> log.info("Message received: {}", content.data()),
                error -> log.error("Error receiving SSE: {}", error),
                () -> log.info("Completed!!!"));
    }

    public void join(String roomName) {
        Flux<ServerSentEvent<String>> eventStream = client.get()
                .uri("/join/" + roomName)
                .retrieve()
                .bodyToFlux(type);

        eventStream.subscribe(
                content -> log.info("Message received: {}", content.data()),
                error -> log.error("Error receiving SSE: {}", error),
                () -> log.info("Completed!!!"));
    }
}
