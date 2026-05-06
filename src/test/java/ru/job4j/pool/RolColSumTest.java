package ru.job4j.pool;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

class RolColSumTest {

    @Test
    void whenSumThenRowsAndColumnsCalculated() {
        int[][] matrix = {
                {1, 2, 3},
                {4, 5, 6},
                {7, 8, 9}
        };
        RolColSum.Sums[] result = RolColSum.sum(matrix);
        assertThat(result).extracting(RolColSum.Sums::getRowSum, RolColSum.Sums::getColSum)
                .containsExactly(
                        tuple(6, 12),
                        tuple(15, 15),
                        tuple(24, 18)
                );
    }

    @Test
    void whenAsyncSumThenRowsAndColumnsCalculated() {
        int[][] matrix = {
                {1, 2, 3},
                {4, 5, 6},
                {7, 8, 9}
        };
        RolColSum.Sums[] result = RolColSum.asyncSum(matrix);
        assertThat(result).extracting(RolColSum.Sums::getRowSum, RolColSum.Sums::getColSum)
                .containsExactly(
                        tuple(6, 12),
                        tuple(15, 15),
                        tuple(24, 18)
                );
    }
}
