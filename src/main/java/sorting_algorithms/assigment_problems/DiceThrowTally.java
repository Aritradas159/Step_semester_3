package sorting_algorithms.assigment_problems;

/**
 * STEP 11 - Assignment Problem 5 (Problem 10): The Dice Throw Tally
 *
 * Algorithm: COUNTING SORT (Domain 1..6)
 *
 * Tallies dice roll results (integers from 1 to 6) using a fixed-size frequency array
 * of length 7 (indices 1 through 6), prints the frequency counts, and reconstructs
 * the sorted array in ascending order without comparing any two dice throws with each other.
 *
 * Complexity:
 * - Time Complexity: O(n + k) where k = 6, which simplifies to O(n).
 * - Space Complexity: O(n) for the returned sorted result array and O(1) auxiliary space
 *   for the fixed-size frequency array of size 7.
 */
public class DiceThrowTally {

    /**
     * Counts frequencies of each dice face (1..6), prints the frequency summary,
     * and builds and returns a new sorted array in ascending order.
     *
     * @param rolls array of dice throws (each between 1 and 6 inclusive)
     * @return new array containing the rolls in ascending order
     * @throws IllegalArgumentException if rolls is null, empty, or contains a value outside 1..6
     */
    public static int[] diceSort(int[] rolls) {
        if (rolls == null || rolls.length == 0) {
            throw new IllegalArgumentException("Rolls array must not be null or empty.");
        }

        int[] count = new int[7];
        for (int i = 0; i < rolls.length; i++) {
            int roll = rolls[i];
            if (roll < 1 || roll > 6) {
                throw new IllegalArgumentException(
                        "Invalid dice roll at index " + i + ": " + roll + " (must be between 1 and 6).");
            }
            count[roll]++;
        }

        StringBuilder countLine = new StringBuilder("Counts:");
        for (int face = 1; face <= 6; face++) {
            countLine.append(" ").append(face).append(":").append(count[face]);
        }
        System.out.println(countLine);

        int[] sorted = new int[rolls.length];
        int writeIdx = 0;
        for (int face = 1; face <= 6; face++) {
            for (int c = 0; c < count[face]; c++) {
                sorted[writeIdx++] = face;
            }
        }

        return sorted;
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
     * Parses inputs such as "rolls = [4, 1, 6, 4, 2, 1, 4]" or "[4, 1, 6, 4, 2, 1, 4]".
     */
    public static int[] parseRolls(String rawInput) {
        if (rawInput == null) {
            throw new IllegalArgumentException("Input cannot be null.");
        }
        String text = rawInput.trim();
        int eqIdx = text.indexOf('=');
        if (eqIdx != -1) {
            String lhs = text.substring(0, eqIdx).trim();
            if (!lhs.equals("rolls")) {
                throw new IllegalArgumentException("Unexpected variable name: " + lhs);
            }
            text = text.substring(eqIdx + 1).trim();
        }

        if (!text.startsWith("[") || !text.endsWith("]")) {
            throw new IllegalArgumentException("Rolls input must be enclosed in '[' and ']'.");
        }

        String inner = text.substring(1, text.length() - 1).trim();
        if (inner.isEmpty()) {
            throw new IllegalArgumentException("Rolls array cannot be empty.");
        }

        String[] tokens = inner.split(",");
        int[] rolls = new int[tokens.length];
        for (int i = 0; i < tokens.length; i++) {
            String token = tokens[i].trim();
            if (token.isEmpty()) {
                throw new IllegalArgumentException("Empty element in rolls array.");
            }
            try {
                rolls[i] = Integer.parseInt(token);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Invalid integer in rolls array: '" + token + "'", e);
            }
        }
        return rolls;
    }

    public static void main(String[] args) {
        if (args.length == 0) {
            int[] sample = {4, 1, 6, 4, 2, 1, 4};
            int[] sorted = diceSort(sample);
            System.out.println("Sorted: " + formatArray(sorted));
            return;
        }

        String combinedInput = String.join(" ", args);
        try {
            int[] rolls = parseRolls(combinedInput);
            int[] sorted = diceSort(rolls);
            System.out.println("Sorted: " + formatArray(sorted));
        } catch (IllegalArgumentException e) {
            System.err.println("Error: " + e.getMessage());
            System.exit(1);
        }
    }
}
