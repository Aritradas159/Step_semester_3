package sorting_algorithms.assigment_problems;

/**
 * STEP 11 - Assignment Problem 4 (Problem 9): Is the Queue in Order?
 *
 * Algorithm: Single-Pass Monotonic (Non-Decreasing) Order Verification
 *
 * Checks whether student token numbers in a queue are in non-decreasing order
 * (equal adjacent token numbers are allowed). Returns the zero-based index of the
 * first element that is strictly smaller than the element immediately before it,
 * or -1 if the entire queue is in order. An empty queue or single-student queue
 * is always in order and returns -1.
 *
 * Complexity:
 * - Time Complexity: O(n), scanning the array at most once from index 1 to n - 1.
 * - Extra Space Complexity: O(1).
 */
public class QueueInOrder {

    /**
     * Returns the zero-based index of the first element smaller than its predecessor,
     * or -1 if the array is in non-decreasing order.
     *
     * @param tokens array of student token numbers
     * @return index of first order violation, or -1 if sorted in non-decreasing order
     * @throws IllegalArgumentException if tokens is null
     */
    public static int firstBreak(int[] tokens) {
        if (tokens == null) {
            throw new IllegalArgumentException("Tokens array must not be null.");
        }
        if (tokens.length <= 1) {
            return -1;
        }

        for (int i = 1; i < tokens.length; i++) {
            if (tokens[i] < tokens[i - 1]) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Parses inputs such as "tokens = [3, 5, 5, 8, 6, 9]", "[1, 2, 2, 7]", or "[]".
     */
    public static int[] parseTokens(String rawInput) {
        if (rawInput == null) {
            throw new IllegalArgumentException("Input cannot be null.");
        }
        String text = rawInput.trim();
        int eqIdx = text.indexOf('=');
        if (eqIdx != -1) {
            String lhs = text.substring(0, eqIdx).trim();
            if (!lhs.equals("tokens")) {
                throw new IllegalArgumentException("Unexpected variable name: " + lhs);
            }
            text = text.substring(eqIdx + 1).trim();
        }

        if (!text.startsWith("[") || !text.endsWith("]")) {
            throw new IllegalArgumentException("Tokens input must be enclosed in '[' and ']'.");
        }

        String inner = text.substring(1, text.length() - 1).trim();
        if (inner.isEmpty()) {
            return new int[0];
        }

        String[] parts = inner.split(",");
        int[] tokens = new int[parts.length];
        for (int i = 0; i < parts.length; i++) {
            String token = parts[i].trim();
            if (token.isEmpty()) {
                throw new IllegalArgumentException("Empty element in tokens array.");
            }
            try {
                tokens[i] = Integer.parseInt(token);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Invalid integer in tokens array: '" + token + "'", e);
            }
        }
        return tokens;
    }

    public static void main(String[] args) {
        if (args.length == 0) {
            int[] sample1 = {3, 5, 5, 8, 6, 9};
            int[] sample2 = {1, 2, 2, 7};
            System.out.println(firstBreak(sample1));
            System.out.println(firstBreak(sample2));
            return;
        }

        String combinedInput = String.join(" ", args);
        try {
            int[] tokens = parseTokens(combinedInput);
            System.out.println(firstBreak(tokens));
        } catch (IllegalArgumentException e) {
            System.err.println("Error: " + e.getMessage());
            System.exit(1);
        }
    }
}
