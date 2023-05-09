package com.cpd.chatread;

import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

@RestController
@RequestMapping("/read")
@EnableScheduling
public class ReadController {

    private Sinks.Many<String> sink;

    @GetMapping
    public String test() {
        return "Hello world";
    }

    @GetMapping(path = "/stream-flux", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> streamFlux() {
        var s = Sinks.many().multicast().<String>onBackpressureBuffer();

        sink = s;

        return s.asFlux();
    }

    @Scheduled(fixedRate = 1000)
    void run() {
        System.out.println("Am trimis");
        if (sink != null)
            sink.emitNext("mai trimit ceva", Sinks.EmitFailureHandler.FAIL_FAST);
    }
}
