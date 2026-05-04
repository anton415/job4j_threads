package ru.job4j.notify;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

class SimpleBlockingQueueTest {

    @Test
    void whenProducerAddsThenConsumerPolls() throws InterruptedException {
        SimpleBlockingQueue<Integer> queue = new SimpleBlockingQueue<>();
        List<Integer> buffer = new ArrayList<>();
        Thread producer = new Thread(() -> {
            queue.offer(1);
            queue.offer(2);
        });
        Thread consumer = new Thread(() -> {
            try {
                buffer.add(queue.poll());
                buffer.add(queue.poll());
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
        consumer.start();
        producer.start();
        producer.join();
        consumer.join();
        assertThat(buffer).containsExactly(1, 2);
    }

    @Test
    void whenFetchAllThenGetIt() throws InterruptedException {
        CopyOnWriteArrayList<Integer> buffer = new CopyOnWriteArrayList<>();
        SimpleBlockingQueue<Integer> queue = new SimpleBlockingQueue<>();

        Thread producer = new Thread(() ->
                IntStream.range(0, 5).forEach(queue::offer)
        );

        Thread consumer = new Thread(() -> {
            while (!queue.isEmpty() || !Thread.currentThread().isInterrupted()) {
                try {
                    buffer.add(queue.poll());
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        });

        producer.start();
        consumer.start();

        producer.join();
        consumer.interrupt();
        consumer.join();

        assertThat(buffer).containsExactly(0, 1, 2, 3, 4);
    }
}
