package org.cpd;


import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.function.Consumer;

@Slf4j
public class App {

    static String uname = null;

    static Map<CliParser.OppType, Consumer<String>> parseAndActionMap = Map.of(
            CliParser.OppType.LOGIN, s -> {
                log.info("Te-ai logat ca {}", s);
                //ar trebui sa deschid fulxurile de comunicare catre read!
                uname = s;
            },
            CliParser.OppType.LOGOUT, s -> {
                log.info("Te-ai delogat ca {}", uname);
                //ar trebui sa inchid fluxul de comunicare catre read
                uname = null;
            },
            CliParser.OppType.SEND, s -> {
                //tre sa fac parsarea! si sa trimit un obiect in http catre write!??
            },
            CliParser.OppType.ERROR, s -> {
                log.error("Commanda nu exista!!");
            }
    );

    static void parseCommands() throws ExecutionException, InterruptedException {
        ExecutorService exe = Executors.newSingleThreadExecutor();

        while (true) {
            Future<Pair<CliParser.OppType, String>> inputFuture = exe.submit(CliParser::readInput);
            var res = inputFuture.get();
            var cons = parseAndActionMap.get(res.first());
            cons.accept(res.second());
        }
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {
//        WebClient client = WebClient.create("http://localhost:8080/read");
//        var type = new ParameterizedTypeReference<ServerSentEvent<String>>() {
//        };
//
//
//        Flux<ServerSentEvent<String>> eventStream = client.get()
//                .uri("/stream-flux")
//                .retrieve()
//                .bodyToFlux(type);
//
//        eventStream.subscribe(
//                content -> log.info("Time: {} - event: name[{}], id [{}], content[{}] ",
//                        LocalTime.now(), content.event(), content.id(), content.data()),
//                error -> log.error("Error receiving SSE: {}", error),
//                () -> log.info("Completed!!!"));
//
//        Executors.newSingleThreadExecutor().execute(() -> {
//            System.out.println("sunt aici");
//        });

        parseCommands();
    }
}
