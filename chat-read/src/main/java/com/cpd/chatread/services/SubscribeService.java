package com.cpd.chatread.services;

import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

import java.util.HashMap;
import java.util.Map;

@Service
public class SubscribeService {
    private final Map<String, Sinks.Many<String>> sinks = new HashMap<>();

    public Flux<String> subscribe(String username) {
        var sink = Sinks.many().unicast().<String>onBackpressureBuffer();
        sinks.put(getUserId(username), sink);
        return sink.asFlux();
    }

    public void unsubscribe(String username) {
        var sink = sinks.remove(getUserId(username));
        if(sink == null) return;
        sink.emitComplete(Sinks.EmitFailureHandler.FAIL_FAST);
    }

    public Sinks.Many<String> getSubscription(String id) {
        return sinks.get(id);
    }

    public Sinks.Many<String> getUserSubscription(String username) {
        return getSubscription(getUserId(username));
    }

    public Sinks.Many<String> getRoomSubscription(String roomName) {
        return getSubscription(getRoomId(roomName));
    }

    public Sinks.Many<String> getOrCreateRoom(String roomName) {
        var id = getRoomId(roomName);

        var sink = sinks.get(id);
        if(sink != null) return sink;

        sink = Sinks.many().multicast().onBackpressureBuffer();
        sinks.put(id, sink);
        return sink;
    }

    private String getRoomId(String roomName) {
        return "RM_" + roomName;
    }

    private String getUserId(String username) {
        return "US_" + username;
    }
}
