package com.cpd.chatread;

import com.cpd.chatread.services.SubscribeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

@RestController
@EnableScheduling
@RequiredArgsConstructor
@RequestMapping("/read")
public class ReadController {

    private final SubscribeService subscribeService;

    @GetMapping
    public String test() {
        return "Hello world";
    }

    @GetMapping(path = "/subscribe/{username}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> subscribe(@PathVariable String username) {
        return subscribeService.subscribe(username);
    }

    @DeleteMapping(path = "/unsubscribe/{username}")
    public void unsubscribe(@PathVariable String username) {
        subscribeService.unsubscribe(username);
    }

    @GetMapping(path = "/join/{roomName}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> joinRoom(@PathVariable String roomName) {
        var sink = subscribeService.getOrCreateRoom(roomName);
        return sink.asFlux();
    }
}
