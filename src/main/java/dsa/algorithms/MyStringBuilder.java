package dsa.algorithms;

import java.util.Arrays;

/**
 * ============================================================================
 * Implement StringBuilder from Scratch
 * ============================================================================
 * Companies: Amazon, Microsoft | Difficulty: Medium
 *
 * Dynamic char array that avoids O(n^2) string concatenation.
 * append O(1) amortized, insert O(n), reverse O(n), toString O(n).
 */
public class MyStringBuilder {

    private char[] buffer;
    private int length;

    public MyStringBuilder() { buffer = new char[16]; length = 0; }

    public MyStringBuilder append(String str) {
        ensureCapacity(length + str.length());
        str.getChars(0, str.length(), buffer, length);
        length += str.length();
        return this;
    }

    public MyStringBuilder append(char c) {
        ensureCapacity(length + 1);
        buffer[length++] = c;
        return this;
    }

    public MyStringBuilder insert(int index, String str) {
        if (index < 0 || index > length) throw new IndexOutOfBoundsException();
        ensureCapacity(length + str.length());
        System.arraycopy(buffer, index, buffer, index + str.length(), length - index);
        str.getChars(0, str.length(), buffer, index);
        length += str.length();
        return this;
    }

    public MyStringBuilder delete(int start, int end) {
        System.arraycopy(buffer, end, buffer, start, length - end);
        length -= (end - start);
        return this;
    }

    public MyStringBuilder reverse() {
        for (int i = 0, j = length - 1; i < j; i++, j--) {
            char temp = buffer[i]; buffer[i] = buffer[j]; buffer[j] = temp;
        }
        return this;
    }

    public char charAt(int index) { return buffer[index]; }
    public int length() { return length; }

    private void ensureCapacity(int minCapacity) {
        if (minCapacity > buffer.length) {
            buffer = Arrays.copyOf(buffer, Math.max(buffer.length * 2, minCapacity));
        }
    }

    @Override public String toString() { return new String(buffer, 0, length); }

    public static void main(String[] args) {
        System.out.println("=== StringBuilder Implementation ===\n");
        MyStringBuilder sb = new MyStringBuilder();
        sb.append("Hello").append(" ").append("World");
        System.out.println("append: " + sb);

        sb.insert(5, ",");
        System.out.println("insert: " + sb);

        sb.reverse();
        System.out.println("reverse: " + sb);

        sb.reverse().delete(5, 6);
        System.out.println("delete comma: " + sb);
        System.out.println("length: " + sb.length());

        System.out.println("\nappend O(1) amortized, avoids O(n^2) string concat");
    }
}
