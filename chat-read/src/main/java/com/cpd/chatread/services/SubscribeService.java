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
        sinks.put(username, sink);
        return sink.asFlux();
    }

    public void unsubscribe(String username) {
        var sink = sinks.remove(username);
        if(sink == null) return;
        sink.emitComplete(Sinks.EmitFailureHandler.FAIL_FAST);
    }

    public Sinks.Many<String> getSubscription(String id) {
        return sinks.get(id);
    }

    public Sinks.Many<String> getOrCreateRoom(String roomName) {

        var sink = sinks.get(roomName);
        if(sink != null) return sink;

        sink = Sinks.many().multicast().onBackpressureBuffer();
        sinks.put(roomName, sink);
        return sink;
    }
}
