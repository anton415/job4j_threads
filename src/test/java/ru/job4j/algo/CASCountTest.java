package ru.job4j.algo;

import org.junit.jupiter.api.Test;

import java.util.concurrent.CountDownLatch;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

class CASCountTest {

    @Test
    void whenCreatedThenZero() {
        var count = new CASCount();
        assertThat(count.get()).isZero();
    }

    @Test
    void whenIncrementSeveralTimesThenGetUpdatedValue() {
        var count = new CASCount();
        IntStream.range(0, 3).forEach(value -> count.increment());
        assertThat(count.get()).isEqualTo(3);
    }

    @Test
    void whenIncrementFromSeveralThreadsThenAllIncrementsCounted() throws InterruptedException {
        var count = new CASCount();
        int threads = 10;
        int increments = 1000;
        var start = new CountDownLatch(1);
        Thread[] workers = new Thread[threads];
        for (int index = 0; index < threads; index++) {
            workers[index] = new Thread(() -> {
                try {
                    start.await();
                    IntStream.range(0, increments).forEach(value -> count.increment());
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });
            workers[index].start();
        }
        start.countDown();
        for (Thread worker : workers) {
            worker.join();
        }
        assertThat(count.get()).isEqualTo(threads * increments);
    }
}
