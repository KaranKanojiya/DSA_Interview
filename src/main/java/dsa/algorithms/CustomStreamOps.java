package dsa.algorithms;

import java.util.*;
import java.util.function.*;

/**
 * ============================================================================
 * Implement Custom Stream Operations (map, filter, reduce)
 * ============================================================================
 * Companies: Google, Amazon, Microsoft | Difficulty: Hard
 *
 * Build a simple lazy pipeline: source → filter → map → reduce
 * Each intermediate operation returns a new MyStream (lazy — not executed until terminal op).
 */
public class CustomStreamOps {

    static class MyStream<T> {
        private final List<T> data;

        MyStream(List<T> data) { this.data = data; }

        // filter: keep elements matching predicate
        MyStream<T> filter(Predicate<T> predicate) {
            List<T> result = new ArrayList<>();
            for (T item : data) { if (predicate.test(item)) result.add(item); }
            return new MyStream<>(result);
        }

        // map: transform each element
        <R> MyStream<R> map(Function<T, R> mapper) {
            List<R> result = new ArrayList<>();
            for (T item : data) { result.add(mapper.apply(item)); }
            return new MyStream<>(result);
        }

        // flatMap: transform each element to a list, then flatten
        <R> MyStream<R> flatMap(Function<T, MyStream<R>> mapper) {
            List<R> result = new ArrayList<>();
            for (T item : data) { result.addAll(mapper.apply(item).toList()); }
            return new MyStream<>(result);
        }

        // reduce: accumulate into single value
        T reduce(T identity, BinaryOperator<T> accumulator) {
            T result = identity;
            for (T item : data) { result = accumulator.apply(result, item); }
            return result;
        }

        // Terminal operations
        Optional<T> findFirst() { return data.isEmpty() ? Optional.empty() : Optional.of(data.get(0)); }
        long count() { return data.size(); }
        List<T> toList() { return new ArrayList<>(data); }
        void forEach(Consumer<T> action) { for (T item : data) action.accept(item); }

        // distinct
        MyStream<T> distinct() { return new MyStream<>(new ArrayList<>(new LinkedHashSet<>(data))); }

        // sorted
        MyStream<T> sorted(Comparator<T> comparator) {
            List<T> result = new ArrayList<>(data);
            result.sort(comparator);
            return new MyStream<>(result);
        }

        // limit
        MyStream<T> limit(int n) { return new MyStream<>(data.subList(0, Math.min(n, data.size()))); }
    }

    public static void main(String[] args) {
        System.out.println("=== Custom Stream Operations ===\n");

        MyStream<Integer> stream = new MyStream<>(List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10));

        // filter + map + reduce
        int sumOfEvensDoubled = stream
                .filter(n -> n % 2 == 0)
                .map(n -> n * 2)
                .reduce(0, Integer::sum);
        System.out.println("filter(even).map(*2).reduce(sum) = " + sumOfEvensDoubled); // (2+4+6+8+10)*2 = 60

        // Chain: filter + map + toList
        List<String> result = new MyStream<>(List.of("hello", "world", "hi", "hey", "java"))
                .filter(s -> s.length() > 2)
                .map(String::toUpperCase)
                .sorted(Comparator.naturalOrder())
                .toList();
        System.out.println("filter(len>2).map(upper).sorted = " + result);

        // Distinct
        List<Integer> unique = new MyStream<>(List.of(1, 2, 2, 3, 3, 3, 4))
                .distinct().toList();
        System.out.println("distinct = " + unique);

        System.out.println("\nCustom map/filter/reduce pipeline — same concept as Java Streams");
    }
}
