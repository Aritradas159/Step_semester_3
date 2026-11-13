package data_structures.class_problems;

import java.util.Arrays;
import java.util.Scanner;

/**
 * Problem 4: In-Place Array Reversal
 * Reverses the order of numbers in an array directly in-place using two pointers without creating a second array.
 */
public class InPlaceArrayReversal {

    /**
     * Reverses the given array in-place using two pointers.
     * Time Complexity: O(n), Extra Space Complexity: O(1).
     *
     * @param arr the array to reverse in-place
     * @throws IllegalArgumentException if the array is null
     */
    public static void reverseInPlace(int[] arr) {
        if (arr == null) {
            throw new IllegalArgumentException("Array cannot be null.");
        }

        int left = 0;
        int right = arr.length - 1;

        while (left < right) {
            int temp = arr[left];
            arr[left] = arr[right];
            arr[right] = temp;
            left++;
            right--;
        }
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
                int[] arr = parseArray(line);
                reverseInPlace(arr);
                System.out.println(Arrays.toString(arr));
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
