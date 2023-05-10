package com.cpd.chatread.models;

import java.util.List;

public record Message(
        String from,
        List<String> tos,
        String text
) {
}
