package com.cpd.chatread;

import com.cpd.chatread.services.SubscribeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

@RestController
@EnableScheduling
@RequiredArgsConstructor
@RequestMapping("/read")
public class ReadController {
    private final Sinks.Many<String> sink = Sinks.many().unicast().onBackpressureBuffer();

    private final SubscribeService subscribeService;

    @GetMapping
    public String test() {
        return "Hello world";
    }

    @GetMapping(path = "/test", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> streamFlux() {
        return sink.asFlux();
    }

    @GetMapping(path = "/subscribe/{username}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> subscribe(@PathVariable String username) {
        return subscribeService.subscribe(username);
    }

    @GetMapping(path = "/unsubscribe/{username}")
    public void unsubscribe(@PathVariable String username) {
        subscribeService.unsubscribe(username);
    }

    @GetMapping(path = "/join/{roomName}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> joinRoom(@PathVariable String roomName) {
        var sink = subscribeService.getOrCreateRoom(roomName);
        return sink.asFlux();
    }

    @Scheduled(fixedRate = 1000)
    void run() {
        System.out.println("Am trimis");
        sink.emitNext("mai trimit ceva", Sinks.EmitFailureHandler.FAIL_FAST);
    }
}
