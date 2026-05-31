package dsa.algorithms;

import java.util.*;

/**
 * ============================================================================
 * Implement Immutable Class + Builder Pattern
 * ============================================================================
 * Companies: Amazon, Google, Microsoft, Apple | Difficulty: Medium
 *
 * Immutable class rules:
 *   1. Class is final (prevent subclassing)
 *   2. All fields are private final
 *   3. No setters
 *   4. Defensive copies in constructor AND getters for mutable fields
 *   5. Constructor or Builder for initialization
 *
 * Builder pattern: step-by-step construction of immutable objects.
 */
public final class ImmutableClassBuilder {

    // === Immutable class ===
    static final class ImmutablePerson {
        private final String name;
        private final int age;
        private final List<String> hobbies; // mutable field — needs defensive copy

        // Private constructor — use Builder
        private ImmutablePerson(Builder builder) {
            this.name = builder.name;
            this.age = builder.age;
            this.hobbies = List.copyOf(builder.hobbies); // defensive copy — truly immutable
        }

        // Getters only, no setters
        public String getName() { return name; }
        public int getAge() { return age; }
        public List<String> getHobbies() { return hobbies; } // already immutable (List.copyOf)

        @Override
        public String toString() {
            return "Person{name='" + name + "', age=" + age + ", hobbies=" + hobbies + "}";
        }

        // === Builder ===
        static class Builder {
            private String name;        // required
            private int age;            // required
            private List<String> hobbies = new ArrayList<>(); // optional

            Builder(String name, int age) { this.name = name; this.age = age; }
            Builder hobby(String hobby) { hobbies.add(hobby); return this; }
            Builder hobbies(List<String> hobbies) { this.hobbies.addAll(hobbies); return this; }
            ImmutablePerson build() { return new ImmutablePerson(this); }
        }
    }

    public static void main(String[] args) {
        System.out.println("=== Immutable Class + Builder Pattern ===\n");

        // Build immutable person
        ImmutablePerson person = new ImmutablePerson.Builder("Karan", 30)
                .hobby("Coding")
                .hobby("Reading")
                .build();

        System.out.println("Created: " + person);

        // Try to modify — fails
        try {
            person.getHobbies().add("Hacking"); // UnsupportedOperationException
        } catch (UnsupportedOperationException e) {
            System.out.println("Cannot modify hobbies: " + e.getClass().getSimpleName());
        }

        System.out.println("After attempted modification: " + person);

        // Another person
        ImmutablePerson person2 = new ImmutablePerson.Builder("Alice", 25)
                .hobbies(List.of("Music", "Art"))
                .build();
        System.out.println("Person 2: " + person2);

        System.out.println("\n=== Rules ===");
        System.out.println("1. final class (no subclassing)");
        System.out.println("2. private final fields (no reassignment)");
        System.out.println("3. No setters");
        System.out.println("4. Defensive copy of mutable fields (List.copyOf)");
        System.out.println("5. Builder for clean construction");
    }
}
