package org.cpd;


import lombok.extern.slf4j.Slf4j;
import org.cpd.services.Sender;
import org.cpd.services.SubscriptionService;

import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

@Slf4j
public class App {

    public static String uname = null;

    private static final SubscriptionService subscriptionService = new SubscriptionService();
    private static final Sender sender = new Sender();
    private static boolean shouldExit = false;

    static Map<CliParser.OppType, Consumer<String>> parseAndActionMap = Map.of(
            CliParser.OppType.LOGIN, username -> {
                log.info("Te-ai logat ca {}", username);
                subscriptionService.login(username);
                uname = username;
            },
            CliParser.OppType.LOGOUT, s -> {
                log.info("Te-ai delogat ca {}", uname);
                subscriptionService.logout(uname);
                subscriptionService.leaveRooms();
                uname = null;
            },
            CliParser.OppType.SEND, json -> {
                if (uname == null) {
                    log.info("Trebuie sa te loghezi mai intai");
                    return;
                }

                log.info("Sending {}", json);
                sender.send(json);
            },
            CliParser.OppType.ERROR, s -> log.info("Commanda nu exista!! {}", s),
            CliParser.OppType.JOIN, roomName -> {
                if (uname == null) {
                    log.info("Trebuie sa te loghezi mai intai");
                    return;
                }
                log.info("Te-ai logat in camera {}", roomName);
                subscriptionService.join(roomName);
            },
            CliParser.OppType.EXIT, s -> {
                log.info("Exiting...");
                shouldExit = true;
                subscriptionService.logout(uname);
                subscriptionService.leaveRooms();
            }
    );

    static void parseCommands() throws ExecutionException, InterruptedException {
        ExecutorService exe = Executors.newSingleThreadExecutor();

        while (!shouldExit) {
            var inputFuture = exe.submit(CliParser::readInput);
            var res = inputFuture.get();
            var cons = parseAndActionMap.get(res.first());
            cons.accept(res.second());
        }

        exe.shutdown();
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        parseCommands();
    }
}
