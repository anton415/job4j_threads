package ru.job4j.email;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class EmailNotification implements AutoCloseable {
    private final ExecutorService executor = Executors.newFixedThreadPool(
            Runtime.getRuntime().availableProcessors()
    );

    public void emailTo(User user) {
        executor.submit(() -> {
            String subject = String.format("Notification %s to email %s", user.username(), user.email());
            String body = String.format("Add a new event to %s", user.username());
            send(subject, body, user.email());
        });
    }

    @Override
    public void close() {
        executor.shutdown();
    }

    public void send(String subject, String body, String email) {

    }
}
