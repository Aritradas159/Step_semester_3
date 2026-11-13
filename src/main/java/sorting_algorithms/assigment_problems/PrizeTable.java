package sorting_algorithms.assigment_problems;

/**
 * STEP 11 - Assignment Problem 2 (Problem 7): The Prize Table
 *
 * Algorithm: SELECTION SORT (Descending Order)
 *
 * Sorts student marks from highest to lowest. In each round i, finds the highest mark
 * in the unsorted portion (indices i through n - 1) and swaps it into position i only
 * if the highest mark is not already at position i. Returns the count of actual swaps performed.
 *
 * Complexity:
 * - Time Complexity: O(n^2) comparisons across all inputs (best, average, and worst case),
 *   and at most n - 1 swaps.
 * - Auxiliary Space Complexity: O(1) in-place sorting.
 */
public class PrizeTable {

    /**
     * Sorts marks in descending order using Selection Sort and returns the number of actual swaps.
     *
     * @param marks array of student marks
     * @return number of swaps actually performed
     * @throws IllegalArgumentException if marks is null or empty
     */
    public static int prizeTable(int[] marks) {
        if (marks == null || marks.length == 0) {
            throw new IllegalArgumentException("Marks array must not be null or empty.");
        }

        int swaps = 0;
        int n = marks.length;

        for (int i = 0; i < n - 1; i++) {
            int maxIndex = i;
            for (int j = i + 1; j < n; j++) {
                // Strictly greater (>) preserves the first occurrence of tied maximum marks.
                if (marks[j] > marks[maxIndex]) {
                    maxIndex = j;
                }
            }
            if (maxIndex != i) {
                int temp = marks[i];
                marks[i] = marks[maxIndex];
                marks[maxIndex] = temp;
                swaps++;
            }
        }

        return swaps;
    }

    /**
     * Formats an integer array as "[a, b, c]" without using java.util.Arrays.
     */
    public static String formatArray(int[] arr) {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < arr.length; i++) {
            if (i > 0) {
                sb.append(", ");
            }
            sb.append(arr[i]);
        }
        sb.append("]");
        return sb.toString();
    }

    /**
     * Formats the output line matching the PDF specification: "[90, 75, 60, 45] swaps = 2".
     */
    public static String formatOutput(int[] sortedMarks, int swaps) {
        return formatArray(sortedMarks) + " swaps = " + swaps;
    }

    /**
     * Parses inputs such as "marks = [45, 90, 60, 75]" or "[45, 90, 60, 75]".
     */
    public static int[] parseMarks(String rawInput) {
        if (rawInput == null) {
            throw new IllegalArgumentException("Input cannot be null.");
        }
        String text = rawInput.trim();
        int eqIdx = text.indexOf('=');
        if (eqIdx != -1) {
            String lhs = text.substring(0, eqIdx).trim();
            if (!lhs.equals("marks")) {
                throw new IllegalArgumentException("Unexpected variable name: " + lhs);
            }
            text = text.substring(eqIdx + 1).trim();
        }

        if (!text.startsWith("[") || !text.endsWith("]")) {
            throw new IllegalArgumentException("Marks input must be enclosed in '[' and ']'.");
        }

        String inner = text.substring(1, text.length() - 1).trim();
        if (inner.isEmpty()) {
            throw new IllegalArgumentException("Marks array cannot be empty.");
        }

        String[] tokens = inner.split(",");
        int[] marks = new int[tokens.length];
        for (int i = 0; i < tokens.length; i++) {
            String token = tokens[i].trim();
            if (token.isEmpty()) {
                throw new IllegalArgumentException("Empty element in marks array.");
            }
            try {
                marks[i] = Integer.parseInt(token);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Invalid integer in marks array: '" + token + "'", e);
            }
        }
        return marks;
    }

    public static void main(String[] args) {
        if (args.length == 0) {
            int[] sample1 = {45, 90, 60, 75};
            int swaps1 = prizeTable(sample1);
            System.out.println(formatOutput(sample1, swaps1));

            int[] sample2 = {80, 70, 60};
            int swaps2 = prizeTable(sample2);
            System.out.println(formatOutput(sample2, swaps2));
            return;
        }

        String combinedInput = String.join(" ", args);
        try {
            int[] marks = parseMarks(combinedInput);
            int swaps = prizeTable(marks);
            System.out.println(formatOutput(marks, swaps));
        } catch (IllegalArgumentException e) {
            System.err.println("Error: " + e.getMessage());
            System.exit(1);
        }
    }
}
