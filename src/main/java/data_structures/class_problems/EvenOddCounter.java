package data_structures.class_problems;

import java.util.Scanner;

/**
 * Problem 3: Even and Odd Numbers in a List
 * Counts how many numbers in an integer list are even and how many are odd.
 */
public class EvenOddCounter {

    /**
     * Counts even and odd numbers in the given array.
     *
     * @param numbers array of integers
     * @return int array where index 0 is even count and index 1 is odd count
     * @throws IllegalArgumentException if array is null
     */
    public static int[] countEvenAndOdd(int[] numbers) {
        if (numbers == null) {
            throw new IllegalArgumentException("Input array cannot be null.");
        }

        int evenCount = 0;
        int oddCount = 0;

        for (int i = 0; i < numbers.length; i++) {
            if (numbers[i] % 2 == 0) {
                evenCount++;
            } else {
                oddCount++;
            }
        }

        return new int[]{evenCount, oddCount};
    }

    /**
     * Parses a bracketed or comma/space-separated string of integers into an int array.
     */
    public static int[] parseArray(String line) {
        if (line == null || line.trim().isEmpty()) {
            throw new IllegalArgumentException("Input cannot be empty.");
        }
        String cleaned = line.trim();
        if (cleaned.startsWith("[") && cleaned.endsWith("]")) {
            cleaned = cleaned.substring(1, cleaned.length() - 1).trim();
        }
        if (cleaned.isEmpty()) {
            return new int[0];
        }
        String[] parts = cleaned.split("[,\\s]+");
        int[] arr = new int[parts.length];
        for (int i = 0; i < parts.length; i++) {
            try {
                arr[i] = Integer.parseInt(parts[i].trim());
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Invalid integer token: " + parts[i]);
            }
        }
        return arr;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        if (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            try {
                int[] numbers = parseArray(line);
                int[] counts = countEvenAndOdd(numbers);
                System.out.println("Even: " + counts[0]);
                System.out.println("Odd: " + counts[1]);
            } catch (IllegalArgumentException e) {
                System.out.println("Error: " + e.getMessage());
                System.exit(1);
            }
        } else {
            System.out.println("Error: No input provided.");
            System.exit(1);
        }
        scanner.close();
    }
}
