package sorting_algorithms.assigment_problems;

/**
 * STEP 11 - Assignment Problem 1 (Problem 6): The Height Line-Up
 *
 * Algorithm: BUBBLE SORT (Ascending Order with Early-Stop Flag)
 *
 * Sorts an array of student heights in ascending order by repeatedly sweeping from
 * left to right, comparing adjacent elements (heights[j] and heights[j + 1]), and
 * swapping them when the left element is strictly greater than the right element.
 * After every sweep, the entire line is printed as "Pass k: [...]".
 * If a sweep performs no swaps at all, that sweep is printed and the algorithm stops early.
 *
 * Complexity:
 * - Worst/Average Time Complexity: O(n^2) comparisons and swaps.
 * - Best-Case Time Complexity: O(n) comparisons when the array is already sorted (1 sweep).
 * - Auxiliary Space Complexity: O(1) in-place sorting.
 */
public class HeightLineUp {

    /**
     * Sorts heights in ascending order using Bubble Sort and prints the array after every sweep.
     *
     * @param heights array of student heights
     * @throws IllegalArgumentException if heights is null or empty
     */
    public static void lineUp(int[] heights) {
        if (heights == null || heights.length == 0) {
            throw new IllegalArgumentException("Heights array must not be null or empty.");
        }

        int n = heights.length;
        if (n == 1) {
            System.out.println("Pass 1: " + formatArray(heights));
            return;
        }

        for (int pass = 1; pass <= n - 1; pass++) {
            boolean swapped = false;
            for (int j = 0; j < n - 1; j++) {
                if (heights[j] > heights[j + 1]) {
                    int temp = heights[j];
                    heights[j] = heights[j + 1];
                    heights[j + 1] = temp;
                    swapped = true;
                }
            }
            System.out.println("Pass " + pass + ": " + formatArray(heights));
            if (!swapped) {
                break;
            }
        }
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
     * Parses inputs such as "heights = [4, 2, 5, 1]" or "[4, 2, 5, 1]".
     */
    public static int[] parseHeights(String rawInput) {
        if (rawInput == null) {
            throw new IllegalArgumentException("Input cannot be null.");
        }
        String text = rawInput.trim();
        int eqIdx = text.indexOf('=');
        if (eqIdx != -1) {
            String lhs = text.substring(0, eqIdx).trim();
            if (!lhs.equals("heights")) {
                throw new IllegalArgumentException("Unexpected variable name: " + lhs);
            }
            text = text.substring(eqIdx + 1).trim();
        }

        if (!text.startsWith("[") || !text.endsWith("]")) {
            throw new IllegalArgumentException("Heights input must be enclosed in '[' and ']'.");
        }

        String inner = text.substring(1, text.length() - 1).trim();
        if (inner.isEmpty()) {
            throw new IllegalArgumentException("Heights array cannot be empty.");
        }

        String[] tokens = inner.split(",");
        int[] heights = new int[tokens.length];
        for (int i = 0; i < tokens.length; i++) {
            String token = tokens[i].trim();
            if (token.isEmpty()) {
                throw new IllegalArgumentException("Empty element in heights array.");
            }
            try {
                heights[i] = Integer.parseInt(token);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Invalid integer in heights array: '" + token + "'", e);
            }
        }
        return heights;
    }

    public static void main(String[] args) {
        if (args.length == 0) {
            int[] sample1 = {4, 2, 5, 1};
            lineUp(sample1);
            System.out.println();
            int[] sample2 = {1, 3, 2, 4};
            lineUp(sample2);
            return;
        }

        String combinedInput = String.join(" ", args);
        try {
            int[] heights = parseHeights(combinedInput);
            lineUp(heights);
        } catch (IllegalArgumentException e) {
            System.err.println("Error: " + e.getMessage());
            System.exit(1);
        }
    }
}
