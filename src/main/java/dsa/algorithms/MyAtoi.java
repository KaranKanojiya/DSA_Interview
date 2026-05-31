package dsa.algorithms;

/**
 * ============================================================================
 * Implement atoi — String to Integer
 * ============================================================================
 * Companies: Amazon, Microsoft, Meta | LeetCode: #8 | Difficulty: Medium
 *
 * Handle: whitespace, +/- sign, digits, overflow, invalid characters.
 * Steps: skip whitespace → check sign → parse digits → clamp to INT range.
 */
public class MyAtoi {

    public static int myAtoi(String s) {
        if (s == null || s.isEmpty()) return 0;

        int i = 0, n = s.length();

        // Step 1: Skip leading whitespace
        while (i < n && s.charAt(i) == ' ') i++;
        if (i == n) return 0;

        // Step 2: Check sign
        int sign = 1;
        if (s.charAt(i) == '+' || s.charAt(i) == '-') {
            sign = s.charAt(i) == '-' ? -1 : 1;
            i++;
        }

        // Step 3: Parse digits, handle overflow
        long result = 0;
        while (i < n && Character.isDigit(s.charAt(i))) {
            result = result * 10 + (s.charAt(i) - '0');

            // Step 4: Clamp to INT range
            if (result * sign > Integer.MAX_VALUE) return Integer.MAX_VALUE;
            if (result * sign < Integer.MIN_VALUE) return Integer.MIN_VALUE;
            i++;
        }

        return (int) (result * sign);
    }

    public static void main(String[] args) {
        System.out.println("=== atoi (String to Integer) ===\n");
        String[] tests = {"42", "   -42", "4193 with words", "", "words 123",
                "+1", "2147483648", "-2147483649", "   +0 123"};
        for (String t : tests) {
            System.out.printf("  myAtoi(\"%s\") = %d%n", t, myAtoi(t));
        }
        System.out.println("\nHandle: whitespace, sign, digits, overflow, invalid chars");
    }
}
