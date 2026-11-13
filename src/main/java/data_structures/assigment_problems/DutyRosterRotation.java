package data_structures.assigment_problems;

import java.util.Arrays;
import java.util.Scanner;

/**
 * Problem 8: Duty Roster Rotation
 * Rotates a cleaning duty roster to the right by k places using modulo arithmetic.
 *
 * Complexity Analysis:
 * - Time Complexity: O(n) — each element is placed directly into its target index (i + k % n) % n in a single pass.
 *   Rotating one place at a time k times would take O(n * k) time, which is too slow when k is up to 10^9.
 * - Space Complexity: O(n) to store and return the rotated roster array.
 */
public class DutyRosterRotation {

    /**
     * Returns the roster after rotating it to the right by k places.
     *
     * @param names array of student names (1 <= length <= 100)
     * @param k     number of places to rotate right (0 <= k <= 10^9)
     * @return new String array containing the rotated roster
     * @throws IllegalArgumentException if names is null, empty, length > 100, or k < 0
     */
    public static String[] rotateRoster(String[] names, long k) {
        if (names == null || names.length < 1 || names.length > 100) {
            throw new IllegalArgumentException("Length of names must be between 1 and 100.");
        }
        if (k < 0 || k > 1_000_000_000L) {
            throw new IllegalArgumentException("Rotation count k must be between 0 and 10^9.");
        }

        int n = names.length;
        int shift = (int) (k % n);
        String[] rotated = new String[n];

        for (int i = 0; i < n; i++) {
            if (names[i] == null || names[i].trim().isEmpty()) {
                throw new IllegalArgumentException("Roster names cannot be null or empty.");
            }
            int newIndex = (i + shift) % n;
            rotated[newIndex] = names[i];
        }

        return rotated;
    }

    /**
     * Overloaded helper accepting int k for convenience.
     */
    public static String[] rotateRoster(String[] names, int k) {
        return rotateRoster(names, (long) k);
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        StringBuilder sb = new StringBuilder();
        while (scanner.hasNextLine()) {
            sb.append(scanner.nextLine()).append(" ");
        }
        scanner.close();

        String input = sb.toString().trim();
        if (input.isEmpty()) {
            System.out.println("Error: No input provided.");
            System.exit(1);
        }

        try {
            int commentIdx = input.indexOf("//");
            if (commentIdx != -1) {
                input = input.substring(0, commentIdx).trim();
            }

            int openBracket = input.indexOf('[');
            int closeBracket = input.indexOf(']');
            if (openBracket == -1 || closeBracket == -1 || closeBracket < openBracket) {
                throw new IllegalArgumentException("Expected names array in brackets [...]");
            }

            String arrayPart = input.substring(openBracket + 1, closeBracket).trim();
            if (arrayPart.isEmpty()) {
                throw new IllegalArgumentException("Names array cannot be empty.");
            }

            String[] rawNames = arrayPart.split(",");
            String[] names = new String[rawNames.length];
            for (int i = 0; i < rawNames.length; i++) {
                names[i] = rawNames[i].trim();
            }

            String remainder = input.substring(closeBracket + 1).trim();
            String cleanedRemainder = remainder.replaceAll("[^0-9\\-]+", " ").trim();
            if (cleanedRemainder.isEmpty()) {
                throw new IllegalArgumentException("Missing rotation count k.");
            }
            long k = Long.parseLong(cleanedRemainder.split("\\s+")[0]);

            String[] result = rotateRoster(names, k);
            System.out.println(Arrays.toString(result));
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
            System.exit(1);
        }
    }
}
