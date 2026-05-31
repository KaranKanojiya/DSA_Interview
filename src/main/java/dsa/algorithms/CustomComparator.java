package dsa.algorithms;

import java.util.*;

/**
 * ============================================================================
 * Implement Custom Comparator / Multi-Field Sorting
 * ============================================================================
 * Companies: Amazon, Google, Meta | Difficulty: Medium
 *
 * Comparable: natural ordering (compareTo on the class itself)
 * Comparator: custom ordering (separate compare function)
 * Chaining: thenComparing for multi-field sort
 */
public class CustomComparator {

    record Employee(String name, String dept, int age, double salary)
            implements Comparable<Employee> {
        @Override
        public int compareTo(Employee o) { return this.name.compareTo(o.name); } // natural order: by name
    }

    public static void main(String[] args) {
        System.out.println("=== Custom Comparator ===\n");
        List<Employee> emps = new ArrayList<>(List.of(
                new Employee("Karan", "Engineering", 30, 120000),
                new Employee("Alice", "Engineering", 25, 110000),
                new Employee("Bob", "Sales", 35, 90000),
                new Employee("Alice", "Sales", 28, 95000),
                new Employee("Charlie", "Engineering", 30, 130000)
        ));

        // Natural order (Comparable — by name)
        Collections.sort(emps);
        System.out.println("Natural order (name):");
        emps.forEach(e -> System.out.println("  " + e));

        // Custom comparator: by salary descending
        emps.sort(Comparator.comparingDouble(Employee::salary).reversed());
        System.out.println("\nBy salary (desc):");
        emps.forEach(e -> System.out.println("  " + e.name() + " $" + e.salary()));

        // Multi-field: by dept, then by age, then by name
        emps.sort(Comparator.comparing(Employee::dept)
                .thenComparingInt(Employee::age)
                .thenComparing(Employee::name));
        System.out.println("\nBy dept → age → name:");
        emps.forEach(e -> System.out.println("  " + e.dept() + " | " + e.name() + " | " + e.age()));

        // Lambda comparator
        emps.sort((a, b) -> Double.compare(b.salary(), a.salary()));
        System.out.println("\nLambda (salary desc):");
        emps.forEach(e -> System.out.println("  " + e.name() + " $" + e.salary()));
    }
}
