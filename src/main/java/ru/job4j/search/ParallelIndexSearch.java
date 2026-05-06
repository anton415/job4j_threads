package ru.job4j.search;

import java.util.Objects;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

public class ParallelIndexSearch<T> extends RecursiveTask<Integer> {
    private final T[] array;
    private final T target;
    private final int from;
    private final int to;

    public ParallelIndexSearch(T[] array, T target) {
        this(array, target, 0, array.length);
    }

    public static <T> int search(T[] array, T target) {
        return ForkJoinPool.commonPool().invoke(new ParallelIndexSearch<>(array, target));
    }

    private ParallelIndexSearch(T[] array, T target, int from, int to) {
        this.array = array;
        this.target = target;
        this.from = from;
        this.to = to;
    }

    @Override
    protected Integer compute() {
        if (to - from <= 10) {
            return linearSearch();
        }
        int middle = (from + to) / 2;
        ParallelIndexSearch<T> left = new ParallelIndexSearch<>(array, target, from, middle);
        ParallelIndexSearch<T> right = new ParallelIndexSearch<>(array, target, middle, to);
        left.fork();
        right.fork();
        Integer leftResult = left.join();
        Integer rightResult = right.join();
        return leftResult != -1 ? leftResult : rightResult;
    }

    private int linearSearch() {
        for (int index = from; index < to; index++) {
            if (Objects.equals(array[index], target)) {
                return index;
            }
        }
        return -1;
    }
}
