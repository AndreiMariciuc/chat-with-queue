package org.cpd;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.cpd.models.Message;

import java.util.Arrays;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CliParser {

    // send a,b,c text
    private final static String pattern = "^((?:[^ ]+,?)+) (.+)";
    private final static Pattern r = Pattern.compile(pattern);
    private final static ObjectMapper jsonConverter = new ObjectMapper();

    static Map<String, OppType> option = Map.of(
            "send", OppType.SEND,
            "s", OppType.SEND,
            "login", OppType.LOGIN,
            "lin", OppType.LOGIN,
            "lout", OppType.LOGOUT,
            "logout", OppType.LOGOUT
    );

    @SneakyThrows
    public static Pair<OppType, String> readInput() {
        var s = new Scanner(System.in);

        String line = s.nextLine();

        if (line == null) {
            return new Pair<>(OppType.ERROR, "");
        }

        var words = line.split(" ");
        var firstWord = words[0];
        var oppType = option.get(firstWord.toLowerCase());
        if(oppType == null) {
            return new Pair<>(OppType.ERROR, "");
        }

        line = line.replaceAll("^[^ ]+ ", "");

        if(words.length == 2) {
            return new Pair<>(oppType, words[1]);
        }

        Matcher m = r.matcher(line);
        if(!m.find()) {
            return new Pair<>(OppType.ERROR, "matching error");
        }

        var msg = new Message(App.uname, Arrays.stream(m.group(1).split(",")).toList(), m.group(2));
        return new Pair<>(oppType, jsonConverter.writeValueAsString(msg));
    }

    enum OppType {
        LOGIN, LOGOUT, SEND, ERROR
    }
}
