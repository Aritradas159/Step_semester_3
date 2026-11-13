package sorting_algorithms.class_problems;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * STEP 11 - Class Problem 2: Transaction Duplicate Check
 *
 * Checks whether any transaction ID string appears more than once in a batch.
 *
 * Chosen Approach (HashSet):
 * - Iterates through the array of transaction IDs and inserts each ID into a HashSet.
 * - Because HashSet.add() returns false if the element is already present, the method
 *   immediately returns true upon encountering the first duplicate.
 * - Average Time Complexity: O(n), because each HashSet lookup and insertion takes O(1)
 *   average time across n transaction IDs.
 * - Space Complexity: O(n) auxiliary memory to store up to n unique transaction IDs in the HashSet.
 *
 * Alternative Lower-Memory Approach & Trade-off Discussion:
 * 1. Nested Loop Comparison (Brute-Force):
 *    - Compare every pair (i, j) with 0 <= i < j < n using transactionIds[i].equals(transactionIds[j]).
 *    - Time Complexity: O(n^2) comparisons in the worst case.
 *    - Extra Space Complexity: O(1) beyond the input array.
 * 2. In-Place Sorting Followed by Adjacent Scan:
 *    - Sort the array in-place and then check whether transactionIds[i].equals(transactionIds[i - 1]).
 *    - Time Complexity: O(n log n), Extra Space: O(1) (for an in-place sort like Heapsort).
 *
 * Trade-off Explanation:
 * - For a payments platform processing millions of transactions nightly (e.g., n = 5,000,000),
 *   an O(n^2) nested-loop check requires ~1.25 x 10^13 string comparisons, which would take hours,
 *   whereas the O(n) HashSet approach finishes in a fraction of a second at the cost of a few
 *   dozen megabytes of RAM. When minimizing runtime is the top priority, trading O(n) memory
 *   for O(n) average runtime is strongly preferred.
 */
public class TransactionDuplicateCheck {

    /**
     * Returns true if any transaction ID appears more than once in the input array.
     *
     * @param transactionIds array of transaction ID strings
     * @return true if a duplicate ID exists, false otherwise
     * @throws IllegalArgumentException if the input array is null or contains null/blank IDs
     */
    public static boolean hasDuplicate(String[] transactionIds) {
        if (transactionIds == null) {
            throw new IllegalArgumentException("Transaction ID array must not be null.");
        }

        Set<String> seenIds = new HashSet<>();
        for (int i = 0; i < transactionIds.length; i++) {
            String id = transactionIds[i];
            if (id == null || id.trim().isEmpty()) {
                throw new IllegalArgumentException("Transaction ID at index " + i + " must not be null or empty.");
            }
            if (!seenIds.add(id)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Parses a bracketed string array such as:
     * ["TXN1001", "TXN1002", "TXN1003", "TXN1002", "TXN1004"]
     * or transactionIds = ["TXN2001", "TXN2002", "TXN2003"]
     */
    public static String[] parseStringArray(String rawInput) {
        if (rawInput == null) {
            throw new IllegalArgumentException("Input cannot be null.");
        }
        String text = rawInput.trim();
        int eqIdx = text.indexOf('=');
        if (eqIdx != -1) {
            String lhs = text.substring(0, eqIdx).trim();
            if (!lhs.equals("transactionIds")) {
                throw new IllegalArgumentException("Unexpected variable name: " + lhs);
            }
            text = text.substring(eqIdx + 1).trim();
        }

        if (!text.startsWith("[") || !text.endsWith("]")) {
            throw new IllegalArgumentException("Input must be enclosed in '[' and ']'.");
        }

        String inner = text.substring(1, text.length() - 1).trim();
        if (inner.isEmpty()) {
            return new String[0];
        }

        String[] rawTokens = inner.split(",");
        List<String> items = new ArrayList<>(rawTokens.length);
        for (String rawToken : rawTokens) {
            String token = rawToken.trim();
            if (token.isEmpty()) {
                throw new IllegalArgumentException("Empty element in transaction ID list.");
            }
            if ((token.startsWith("\"") && token.endsWith("\""))
                    || (token.startsWith("'") && token.endsWith("'"))) {
                if (token.length() < 2) {
                    throw new IllegalArgumentException("Malformed quoted string: " + token);
                }
                token = token.substring(1, token.length() - 1);
            }
            if (token.trim().isEmpty()) {
                throw new IllegalArgumentException("Transaction ID cannot be blank.");
            }
            items.add(token);
        }

        return items.toArray(new String[0]);
    }

    public static void main(String[] args) {
        if (args.length == 0) {
            String[] sample1 = {"TXN1001", "TXN1002", "TXN1003", "TXN1002", "TXN1004"};
            String[] sample2 = {"TXN2001", "TXN2002", "TXN2003"};
            System.out.println(hasDuplicate(sample1));
            System.out.println(hasDuplicate(sample2));
            return;
        }

        String combinedInput = String.join(" ", args);
        try {
            String[] transactionIds = parseStringArray(combinedInput);
            System.out.println(hasDuplicate(transactionIds));
        } catch (IllegalArgumentException e) {
            System.err.println("Error: " + e.getMessage());
            System.exit(1);
        }
    }
}
