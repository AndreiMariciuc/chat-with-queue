package com.cpd.chatwrite;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.time.LocalTime;

@Slf4j
@SpringBootApplication
public class ChatWriteApplication {

    public static void main(String[] args) {
        SpringApplication.run(ChatWriteApplication.class, args);
    }

    @PostConstruct
    void testWebFlux() {
        WebClient client = WebClient.create("http://localhost:8080/read");
        var type = new ParameterizedTypeReference<ServerSentEvent<String>>() {
        };

        System.out.println("vad sau nu ceva\n");

        Flux<ServerSentEvent<String>> eventStream = client.get()
                .uri("/stream-flux")
                .retrieve()
                .bodyToFlux(type);

        eventStream.subscribe(
                content -> log.info("Time: {} - event: name[{}], id [{}], content[{}] ",
                        LocalTime.now(), content.event(), content.id(), content.data()),
                error -> log.error("Error receiving SSE: {}", error),
                () -> log.info("Completed!!!"));
    }

}
