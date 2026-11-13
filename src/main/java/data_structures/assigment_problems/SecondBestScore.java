package data_structures.assigment_problems;

import java.util.Scanner;

/**
 * Problem 7: Second-Best Score
 * Finds the second-highest distinct score in an array without sorting.
 *
 * Complexity Analysis:
 * - Time Complexity: O(n) — single traversal over the array of n elements.
 *   (Compare with sorting first: O(n log n) time).
 * - Extra Space Complexity: O(1) — only two scalar variables (highest, second) are used.
 */
public class SecondBestScore {

    /**
     * Returns the second-highest distinct score, or -1 if it does not exist.
     *
     * @param scores array of player scores (1 <= length <= 1000, 0 <= scores[i] <= 100)
     * @return the second-highest distinct score, or -1 if none exists
     * @throws IllegalArgumentException if scores is null, empty, length > 1000, or contains scores outside [0, 100]
     */
    public static int secondHighest(int[] scores) {
        if (scores == null || scores.length < 1 || scores.length > 1000) {
            throw new IllegalArgumentException("Length of scores must be between 1 and 1000.");
        }

        int highest = -1;
        int second = -1;

        for (int i = 0; i < scores.length; i++) {
            int score = scores[i];
            if (score < 0 || score > 100) {
                throw new IllegalArgumentException("Each score must be between 0 and 100.");
            }

            if (score > highest) {
                second = highest;
                highest = score;
            } else if (score < highest && score > second) {
                second = score;
            }
        }

        return second;
    }

    /**
     * Parses a line such as "scores = [45, 78, 92, 78, 60]" or "[45, 78, 92, 78, 60]" into an int array.
     */
    public static int[] parseScores(String line) {
        if (line == null || line.trim().isEmpty()) {
            throw new IllegalArgumentException("Input cannot be empty.");
        }
        String cleaned = line.trim();
        int eqIdx = cleaned.indexOf('=');
        if (eqIdx != -1) {
            cleaned = cleaned.substring(eqIdx + 1).trim();
        }
        if (cleaned.startsWith("[") && cleaned.endsWith("]")) {
            cleaned = cleaned.substring(1, cleaned.length() - 1).trim();
        }
        if (cleaned.isEmpty()) {
            throw new IllegalArgumentException("Scores array cannot be empty.");
        }
        String[] parts = cleaned.split("[,\\s]+");
        int[] scores = new int[parts.length];
        for (int i = 0; i < parts.length; i++) {
            try {
                scores[i] = Integer.parseInt(parts[i].trim());
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Invalid integer score: " + parts[i]);
            }
        }
        return scores;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        if (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            try {
                int[] scores = parseScores(line);
                System.out.println(secondHighest(scores));
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
