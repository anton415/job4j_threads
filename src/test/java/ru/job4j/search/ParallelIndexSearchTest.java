package ru.job4j.search;

import org.junit.jupiter.api.Test;

import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

class ParallelIndexSearchTest {

    @Test
    void whenSearchIntegerInSmallArrayThenIndex() {
        Integer[] array = {1, 2, 3, 4, 5};
        int result = new ParallelIndexSearch<>(array, 4).compute();
        assertThat(result).isEqualTo(3);
    }

    @Test
    void whenSearchStringInSmallArrayThenIndex() {
        String[] array = {"one", "two", "three", "four"};
        int result = new ParallelIndexSearch<>(array, "two").compute();
        assertThat(result).isEqualTo(1);
    }

    @Test
    void whenSearchInBigArrayThenIndex() {
        Integer[] array = IntStream.range(0, 100)
                .boxed()
                .toArray(Integer[]::new);
        int result = new ParallelIndexSearch<>(array, 73).compute();
        assertThat(result).isEqualTo(73);
    }

    @Test
    void whenElementNotFoundThenMinusOne() {
        String[] array = IntStream.range(0, 20)
                .mapToObj(String::valueOf)
                .toArray(String[]::new);
        int result = new ParallelIndexSearch<>(array, "not found").compute();
        assertThat(result).isEqualTo(-1);
    }
}
