package org.cpd.models;

import java.util.List;

public record Message(
        String from,
        List<String> tos,
        String text
) {
}
