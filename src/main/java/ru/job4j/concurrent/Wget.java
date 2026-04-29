package ru.job4j.concurrent;

import java.net.URI;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;

public class Wget implements Runnable {
    private final String url;
    private final int speed;

    public Wget(String url, int speed) {
        this.url = url;
        this.speed = speed;
    }

    @Override
    public void run() {
        try {
            URL url = URI.create(this.url).toURL();
            Path path = Path.of(url.getPath()).getFileName();
            path = path == null ? Path.of("download") : path;
            try (var input = url.openStream();
                 var output = Files.newOutputStream(path)) {
                byte[] buffer = new byte[1024];
                int bytesRead;
                while (true) {
                    long startAt = System.nanoTime();
                    bytesRead = input.read(buffer);
                    long downloadTime = System.nanoTime() - startAt;
                    if (bytesRead == -1) {
                        break;
                    }
                    output.write(buffer, 0, bytesRead);
                    long expectedTime = bytesRead * 1_000_000L / speed;
                    long sleepTime = expectedTime - downloadTime;
                    if (sleepTime > 0) {
                        Thread.sleep(sleepTime / 1_000_000L, (int) (sleepTime % 1_000_000L));
                    }
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        if (args.length != 2) {
            throw new IllegalArgumentException("Usage: java Wget <url> <speed>");
        }
        String url = args[0];
        int speed = Integer.parseInt(args[1]);
        if (speed <= 0) {
            throw new IllegalArgumentException("Speed must be positive");
        }
        Thread wget = new Thread(new Wget(url, speed));
        wget.start();
        wget.join();
    }
}
