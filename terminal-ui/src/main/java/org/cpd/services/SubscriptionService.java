package org.cpd.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.impl.client.HttpClients;
import org.cpd.App;
import org.cpd.models.Message;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class SubscriptionService {

    private static final ParameterizedTypeReference<ServerSentEvent<String>> type = new ParameterizedTypeReference<>() {
    };

    private final static ObjectMapper jsonConverter = new ObjectMapper();
    private final WebClient client;

    private final List<String> rooms = new ArrayList<>();

    public SubscriptionService() {
        client = WebClient.create("http://localhost:8080/read");
    }

    public void login(String username) {
        Flux<ServerSentEvent<String>> eventStream = client.get()
                .uri("/subscribe/" + username)
                .retrieve()
                .bodyToFlux(type);

        eventStream.subscribe(
                content -> {
                    try {
                        var json = content.data();
                        var msg = jsonConverter.readValue(json, Message.class);
                        System.out.printf("""
                                <Private> %s said: %s
                                """, msg.from(), msg.text());
                    } catch (JsonProcessingException e) {
                        log.error("Could not parse message: {}", e.getMessage());
                    }
                },
                error -> log.error("Error receiving SSE: {}", error.getMessage()),
                () -> log.info("Successfully closed private stream !!!")
        );
    }

    public void logout(String username) {
        try (var httpclient = HttpClients.createDefault()) {
            var req = new HttpDelete("http://localhost:8080/read/unsubscribe/" + username);
            httpclient.execute(req);
        } catch (IOException e) {
            log.error("Could not logout: {}", e.getMessage());
        }
    }

    public void join(String roomName) {
        rooms.add(roomName);
        Flux<ServerSentEvent<String>> eventStream = client.get()
                .uri("/join/" + roomName)
                .retrieve()
                .bodyToFlux(type);

        eventStream.subscribe(
                content -> {
                    if(App.uname == null) return;
                    if(!rooms.contains(roomName)) return;
                    try {
                        var json = content.data();
                        var msg = jsonConverter.readValue(json, Message.class);
                        if (msg.from().equals(App.uname)) return;
                        System.out.printf("""
                        <Room> %s said in %s: %s
                        """, msg.from(), roomName, msg.text());
                    } catch (JsonProcessingException e) {
                        log.error("Could not parse message: {}", e.getMessage());
                    }
                },
                error -> {
                    log.error("Error receiving SSE: {}", error.getMessage());
                    rooms.remove(roomName);
                },
                () -> log.info("Successfully closed stream for room: {} !!!", roomName)
        );
    }

    public void leaveRooms() {
        rooms.clear();
    }
}
