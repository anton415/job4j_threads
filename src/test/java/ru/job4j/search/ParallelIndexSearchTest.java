package ru.job4j.search;

import org.junit.jupiter.api.Test;

import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

class ParallelIndexSearchTest {

    @Test
    void whenSearchIntegerInSmallArrayThenIndex() {
        Integer[] array = {1, 2, 3, 4, 5};
        int result = ParallelIndexSearch.search(array, 4);
        assertThat(result).isEqualTo(3);
    }

    @Test
    void whenSearchStringInSmallArrayThenIndex() {
        String[] array = {"one", "two", "three", "four"};
        int result = ParallelIndexSearch.search(array, "two");
        assertThat(result).isEqualTo(1);
    }

    @Test
    void whenSearchInBigArrayThenIndex() {
        Integer[] array = IntStream.range(0, 100)
                .boxed()
                .toArray(Integer[]::new);
        int result = ParallelIndexSearch.search(array, 73);
        assertThat(result).isEqualTo(73);
    }

    @Test
    void whenElementNotFoundThenMinusOne() {
        String[] array = IntStream.range(0, 20)
                .mapToObj(String::valueOf)
                .toArray(String[]::new);
        int result = ParallelIndexSearch.search(array, "not found");
        assertThat(result).isEqualTo(-1);
    }
}
