package ru.job4j.notify;

import java.util.LinkedList;
import java.util.Queue;

import net.jcip.annotations.GuardedBy;
import net.jcip.annotations.ThreadSafe;

@ThreadSafe
public class SimpleBlockingQueue<T> {
    @GuardedBy("this")
    private final Queue<T> queue = new LinkedList<>();

    public void offer(T value) {
        synchronized (this) {
            queue.offer(value);
            this.notifyAll();
        }
    }

    public T poll() throws InterruptedException {
        synchronized (this) {
            while (queue.isEmpty()) {
                this.wait();
            }
            this.notifyAll();
            return queue.poll();
        }
    }
}
