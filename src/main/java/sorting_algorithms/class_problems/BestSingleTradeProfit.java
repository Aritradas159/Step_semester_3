package sorting_algorithms.class_problems;

/**
 * STEP 11 - Class Problem 3: Best Single Trade Profit
 *
 * Computes the maximum profit achievable from at most one buy followed by one sell
 * across chronological daily stock prices.
 *
 * Complexity & Scale Comparison:
 * - Single-Pass Solution (Implemented):
 *   - Time Complexity: O(n), scanning the prices array once while maintaining the
 *     minimum price seen so far and the maximum profit achievable on each day.
 *   - Space Complexity: O(1) auxiliary space.
 * - Brute-Force Comparison (Checking Every Pair of Days (i, j) with i < j):
 *   - Time Complexity: O(n^2), requiring n * (n - 1) / 2 comparisons.
 *   - Space Complexity: O(1).
 * - Why Single-Pass is Preferable at Scale:
 *   - For large historical datasets or intraday tick series (e.g., n = 1,000,000),
 *     an O(n^2) algorithm performs ~5 x 10^11 operations (taking several minutes),
 *     whereas the O(n) single-pass algorithm performs only 1,000,000 operations and
 *     completes in a few milliseconds using constant memory.
 */
public class BestSingleTradeProfit {

    /**
     * Returns the maximum profit from buying on one day and selling on a later day,
     * or 0 if no profit is possible.
     *
     * @param prices chronological array of non-negative daily stock prices
     * @return maximum achievable profit (>= 0)
     * @throws IllegalArgumentException if prices is null, empty, or contains negative values
     */
    public static int maxProfit(int[] prices) {
        if (prices == null || prices.length == 0) {
            throw new IllegalArgumentException("Prices array must not be null or empty.");
        }
        if (prices[0] < 0) {
            throw new IllegalArgumentException("Stock price cannot be negative: " + prices[0]);
        }

        int minPriceSoFar = prices[0];
        long bestProfit = 0L;

        for (int i = 1; i < prices.length; i++) {
            int currentPrice = prices[i];
            if (currentPrice < 0) {
                throw new IllegalArgumentException("Stock price cannot be negative at index " + i + ": " + currentPrice);
            }

            long candidateProfit = (long) currentPrice - (long) minPriceSoFar;
            if (candidateProfit > bestProfit) {
                bestProfit = candidateProfit;
            }
            if (currentPrice < minPriceSoFar) {
                minPriceSoFar = currentPrice;
            }
        }

        return (int) Math.min(bestProfit, Integer.MAX_VALUE);
    }

    /**
     * Parses an integer array from strings like "prices = [7, 1, 5, 3, 6, 4]" or "[7, 6, 4, 3, 1]".
     */
    public static int[] parsePrices(String rawInput) {
        if (rawInput == null) {
            throw new IllegalArgumentException("Input cannot be null.");
        }
        String text = rawInput.trim();
        int eqIdx = text.indexOf('=');
        if (eqIdx != -1) {
            String lhs = text.substring(0, eqIdx).trim();
            if (!lhs.equals("prices")) {
                throw new IllegalArgumentException("Unexpected variable name: " + lhs);
            }
            text = text.substring(eqIdx + 1).trim();
        }

        if (!text.startsWith("[") || !text.endsWith("]")) {
            throw new IllegalArgumentException("Prices input must be enclosed in '[' and ']'.");
        }

        String inner = text.substring(1, text.length() - 1).trim();
        if (inner.isEmpty()) {
            throw new IllegalArgumentException("Prices array cannot be empty.");
        }

        String[] tokens = inner.split(",");
        int[] prices = new int[tokens.length];
        for (int i = 0; i < tokens.length; i++) {
            String token = tokens[i].trim();
            if (token.isEmpty()) {
                throw new IllegalArgumentException("Empty value in prices array.");
            }
            try {
                prices[i] = Integer.parseInt(token);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Invalid integer in prices array: '" + token + "'", e);
            }
        }
        return prices;
    }

    public static void main(String[] args) {
        if (args.length == 0) {
            int[] sample1 = {7, 1, 5, 3, 6, 4};
            int[] sample2 = {7, 6, 4, 3, 1};
            System.out.println(maxProfit(sample1));
            System.out.println(maxProfit(sample2));
            return;
        }

        String combinedInput = String.join(" ", args);
        try {
            int[] prices = parsePrices(combinedInput);
            System.out.println(maxProfit(prices));
        } catch (IllegalArgumentException e) {
            System.err.println("Error: " + e.getMessage());
            System.exit(1);
        }
    }
}
