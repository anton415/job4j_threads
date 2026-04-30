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
                long currentTimeMillis = System.currentTimeMillis();
                int totalBytesRead = 0;
                while (true) {
                    bytesRead = input.read(buffer);
                    output.write(buffer, 0, bytesRead);
                    totalBytesRead += bytesRead;
                    if (totalBytesRead >= speed) {
                        long elapsedTime = System.currentTimeMillis() - currentTimeMillis;
                        if (elapsedTime < 1000) {
                            Thread.sleep(1000 - elapsedTime);
                        }
                        currentTimeMillis = System.currentTimeMillis();
                        totalBytesRead = 0;
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
