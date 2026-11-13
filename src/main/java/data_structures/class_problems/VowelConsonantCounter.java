package data_structures.class_problems;

import java.util.Scanner;

/**
 * Problem 1: Vowel and Consonant Counter
 * Counts the total number of vowels and consonants in a single alphabetic word (case-insensitive).
 */
public class VowelConsonantCounter {

    /**
     * Counts vowels and consonants in the given word.
     *
     * @param word the input word containing only alphabetic characters
     * @return an int array where index 0 is vowel count and index 1 is consonant count
     * @throws IllegalArgumentException if the input is null, empty, or contains non-alphabetic characters
     */
    public static int[] countVowelsAndConsonants(String word) {
        if (word == null) {
            throw new IllegalArgumentException("Input word cannot be null.");
        }
        String trimmed = word.trim();
        if (trimmed.isEmpty()) {
            throw new IllegalArgumentException("Input word cannot be empty.");
        }

        int vowels = 0;
        int consonants = 0;

        for (int i = 0; i < trimmed.length(); i++) {
            char ch = Character.toLowerCase(trimmed.charAt(i));
            if (ch < 'a' || ch > 'z') {
                throw new IllegalArgumentException("Input word must contain only alphabetic characters.");
            }
            if (ch == 'a' || ch == 'e' || ch == 'i' || ch == 'o' || ch == 'u') {
                vowels++;
            } else {
                consonants++;
            }
        }

        return new int[]{vowels, consonants};
    }

    /**
     * Formats the vowel and consonant counts matching the PDF output format.
     */
    public static String formatResult(int[] counts) {
        return "Vowels: " + counts[0] + "\nConsonants: " + counts[1];
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        if (scanner.hasNextLine()) {
            String input = scanner.nextLine();
            try {
                int[] result = countVowelsAndConsonants(input);
                System.out.println("Vowels: " + result[0]);
                System.out.println("Consonants: " + result[1]);
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
