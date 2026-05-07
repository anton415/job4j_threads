package ru.job4j.pool;

import java.util.concurrent.CompletableFuture;

public class RolColSum {
    public static Sums[] sum(int[][] matrix) {
        int size = matrix.length;
        Sums[] result = new Sums[size];
        for (int index = 0; index < size; index++) {
            result[index] = calc(matrix, index);
        }
        return result;
    }

    public static Sums[] asyncSum(int[][] matrix) {
        int size = matrix.length;
        Sums[] result = new Sums[size];
        CompletableFuture<?>[] futures = new CompletableFuture[size];
        for (int index = 0; index < size; index++) {
            int taskIndex = index;
            futures[index] = CompletableFuture.supplyAsync(() -> calc(matrix, taskIndex))
                    .thenAccept(sums -> result[taskIndex] = sums);
        }
        CompletableFuture.allOf(futures).join();
        return result;
    }

    private static Sums calc(int[][] matrix, int index) {
        int rowSum = 0;
        int colSum = 0;
        for (int i = 0; i < matrix.length; i++) {
            rowSum += matrix[index][i];
            colSum += matrix[i][index];
        }
        return new Sums(rowSum, colSum);
    }
}
