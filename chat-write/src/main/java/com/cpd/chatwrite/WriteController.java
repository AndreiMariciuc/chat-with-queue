package com.cpd.chatwrite;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/write")
public class WriteController {

    private final MessageWriter messageWriter;

    @PostMapping
    public void sendUserMessage(@RequestBody String json) {
        messageWriter.sendMessage(json);
    }
}
