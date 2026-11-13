package data_structures.assigment_problems;

import java.util.Scanner;

/**
 * Problem 4: Exam Score Band Counter
 * Counts how many students scored within a band [low, high] (inclusive) in a sorted array using binary search.
 *
 * Complexity Analysis:
 * - Linear scan complexity: O(n) time, O(1) space.
 * - Binary search solution complexity: O(log n) time, O(1) extra space.
 * - Handling duplicates at band edges: Standard binary search stops at an arbitrary matching element.
 *   By finding the first index with score >= low (lower bound) and the first index with score > high (upper bound),
 *   all duplicate values equal to low or high are included, and their difference gives the exact count.
 */
public class ExamScoreBandCounter {

    /**
     * Returns the number of scores s such that low <= s <= high using binary search.
     *
     * @param scores sorted array of exam marks in ascending order
     * @param low    lower bound of the score band (inclusive)
     * @param high   upper bound of the score band (inclusive)
     * @return count of scores within [low, high]
     * @throws IllegalArgumentException if constraints are violated
     */
    public static int countInBand(int[] scores, int low, int high) {
        if (scores == null || scores.length < 1 || scores.length > 1_000_000) {
            throw new IllegalArgumentException("Length of scores must be between 1 and 10^6.");
        }
        if (low < 0 || high > 100 || low > high) {
            throw new IllegalArgumentException("Invalid band: must satisfy 0 <= low <= high <= 100.");
        }

        int firstAtLeastLow = findFirstGreaterOrEqual(scores, low);
        int firstGreaterThanHigh = findFirstStrictlyGreater(scores, high);

        return firstGreaterThanHigh - firstAtLeastLow;
    }

    /**
     * Binary search to find the first index i such that scores[i] >= target.
     * Returns scores.length if all elements are < target.
     */
    private static int findFirstGreaterOrEqual(int[] scores, int target) {
        int left = 0;
        int right = scores.length - 1;
        int result = scores.length;

        while (left <= right) {
            int mid = left + (right - left) / 2;
            if (scores[mid] >= target) {
                result = mid;
                right = mid - 1;
            } else {
                left = mid + 1;
            }
        }
        return result;
    }

    /**
     * Binary search to find the first index i such that scores[i] > target.
     * Returns scores.length if all elements are <= target.
     */
    private static int findFirstStrictlyGreater(int[] scores, int target) {
        int left = 0;
        int right = scores.length - 1;
        int result = scores.length;

        while (left <= right) {
            int mid = left + (right - left) / 2;
            if (scores[mid] > target) {
                result = mid;
                right = mid - 1;
            } else {
                left = mid + 1;
            }
        }
        return result;
    }

    /**
     * Validates that the scores array is sorted in ascending order and within [0, 100] (used when parsing CLI input).
     */
    public static void validateInputArray(int[] scores) {
        if (scores == null || scores.length == 0) {
            throw new IllegalArgumentException("Scores array cannot be empty.");
        }
        for (int i = 0; i < scores.length; i++) {
            if (scores[i] < 0 || scores[i] > 100) {
                throw new IllegalArgumentException("Score out of valid range [0, 100]: " + scores[i]);
            }
            if (i > 0 && scores[i] < scores[i - 1]) {
                throw new IllegalArgumentException("Scores array must be sorted in ascending order.");
            }
        }
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
            int openBracket = input.indexOf('[');
            int closeBracket = input.indexOf(']');
            if (openBracket == -1 || closeBracket == -1 || closeBracket < openBracket) {
                throw new IllegalArgumentException("Expected scores array in brackets [...]");
            }

            String arrayPart = input.substring(openBracket + 1, closeBracket).trim();
            if (arrayPart.isEmpty()) {
                throw new IllegalArgumentException("Scores array cannot be empty.");
            }

            String[] scoreTokens = arrayPart.split("[,\\s]+");
            int[] scores = new int[scoreTokens.length];
            for (int i = 0; i < scoreTokens.length; i++) {
                scores[i] = Integer.parseInt(scoreTokens[i].trim());
            }
            validateInputArray(scores);

            String remainder = input.substring(closeBracket + 1).trim();
            String cleanedRemainder = remainder.replaceAll("[^0-9\\-]+", " ").trim();
            if (cleanedRemainder.isEmpty()) {
                throw new IllegalArgumentException("Missing low and high band values.");
            }
            String[] bandTokens = cleanedRemainder.split("\\s+");
            if (bandTokens.length < 2) {
                throw new IllegalArgumentException("Both low and high values are required.");
            }
            int low = Integer.parseInt(bandTokens[0]);
            int high = Integer.parseInt(bandTokens[1]);

            System.out.println(countInBand(scores, low, high));
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
            System.exit(1);
        }
    }
}
