package org.cpd;

import java.util.Map;
import java.util.Scanner;

public class CliParser {

    static Map<String, OppType> option = Map.of(
            "send", OppType.SEND,
            "s", OppType.SEND,
            "login", OppType.LOGIN,
            "lin", OppType.LOGIN,
            "lout", OppType.LOGOUT,
            "logout", OppType.LOGOUT);

    enum OppType {
        LOGIN, LOGOUT, SEND, ERROR
    }

    public static Pair<OppType, String> readInput() {
        var s = new Scanner(System.in);

        String line = s.nextLine();

        if (line != null) {
            var firstWord = (line.split(" "))[0];
            var oppType = option.get(firstWord.toLowerCase());
            if (oppType != null) {
                return new Pair<>(oppType, line);
            }
        }

        return new Pair<>(OppType.ERROR, "");
    }
}
