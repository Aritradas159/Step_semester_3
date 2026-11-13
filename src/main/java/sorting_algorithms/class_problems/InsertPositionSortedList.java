package sorting_algorithms.class_problems;

/**
 * STEP 11 - Class Problem 4: Insert Position in a Sorted List
 *
 * Uses Binary Search on a sorted array of distinct integers to find the index of a target
 * value, or the index where it should be inserted to maintain ascending sorted order.
 *
 * Complexity & Justification:
 * - Time Complexity: O(log n), because binary search halves the search range in every iteration.
 *   For very large arrays queried frequently (e.g., n = 1,000,000), binary search needs at most
 *   20 comparisons vs. up to 1,000,000 in a linear scan.
 * - Extra Space Complexity: O(1), using iterative pointers (low, high, mid).
 */
public class InsertPositionSortedList {

    /**
     * Finds the zero-based index of target if present, or the index where it should be inserted
     * to keep the strictly increasing array sorted.
     *
     * @param nums   ascending sorted array of distinct integers
     * @param target value to search or insert
     * @return target index or insertion position
     * @throws IllegalArgumentException if nums is null, empty, not strictly ascending, or has duplicates
     */
    public static int searchInsertPosition(int[] nums, int target) {
        if (nums == null || nums.length == 0) {
            throw new IllegalArgumentException("Input array must not be null or empty.");
        }
        for (int i = 1; i < nums.length; i++) {
            if (nums[i] <= nums[i - 1]) {
                throw new IllegalArgumentException(
                        "Array must be sorted in strictly ascending order with distinct elements.");
            }
        }

        int low = 0;
        int high = nums.length - 1;

        while (low <= high) {
            int mid = low + (high - low) / 2;
            if (nums[mid] == target) {
                return mid;
            } else if (nums[mid] < target) {
                low = mid + 1;
            } else {
                high = mid - 1;
            }
        }

        return low;
    }

    /**
     * Parsed representation of nums and target from CLI input.
     */
    public static final class ParsedQuery {
        private final int[] nums;
        private final int target;

        public ParsedQuery(int[] nums, int target) {
            this.nums = nums;
            this.target = target;
        }

        public int[] getNums() {
            return nums;
        }

        public int getTarget() {
            return target;
        }
    }

    /**
     * Parses inputs such as:
     * "nums = [1, 3, 5, 6], target = 5" or "[1, 3, 5, 6] 5"
     */
    public static ParsedQuery parseInput(String rawInput) {
        if (rawInput == null) {
            throw new IllegalArgumentException("Input cannot be null.");
        }
        String text = rawInput.trim();
        int openBracket = text.indexOf('[');
        int closeBracket = text.indexOf(']');
        if (openBracket == -1 || closeBracket == -1 || closeBracket < openBracket) {
            throw new IllegalArgumentException("Expected array enclosed in '[' and ']'.");
        }

        String prefix = text.substring(0, openBracket).trim();
        if (!prefix.isEmpty()) {
            if (prefix.endsWith("=")) {
                String varName = prefix.substring(0, prefix.length() - 1).trim();
                if (!varName.equals("nums")) {
                    throw new IllegalArgumentException("Unexpected array variable name: " + varName);
                }
            } else {
                throw new IllegalArgumentException("Malformed prefix before '[': " + prefix);
            }
        }

        String arrayBody = text.substring(openBracket + 1, closeBracket).trim();
        if (arrayBody.isEmpty()) {
            throw new IllegalArgumentException("nums array must not be empty.");
        }

        String[] tokens = arrayBody.split(",");
        int[] nums = new int[tokens.length];
        for (int i = 0; i < tokens.length; i++) {
            String token = tokens[i].trim();
            if (token.isEmpty()) {
                throw new IllegalArgumentException("Empty element in nums array.");
            }
            try {
                nums[i] = Integer.parseInt(token);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Invalid integer in nums array: '" + token + "'", e);
            }
        }

        String remainder = text.substring(closeBracket + 1).trim();
        if (remainder.startsWith(",")) {
            remainder = remainder.substring(1).trim();
        }
        if (remainder.isEmpty()) {
            throw new IllegalArgumentException("Missing target value.");
        }

        int eqIdx = remainder.indexOf('=');
        if (eqIdx != -1) {
            String targetVar = remainder.substring(0, eqIdx).trim();
            if (!targetVar.equals("target")) {
                throw new IllegalArgumentException("Unexpected target variable name: " + targetVar);
            }
            remainder = remainder.substring(eqIdx + 1).trim();
        }

        int target;
        try {
            target = Integer.parseInt(remainder);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid integer for target: '" + remainder + "'", e);
        }

        return new ParsedQuery(nums, target);
    }

    public static void main(String[] args) {
        if (args.length == 0) {
            int[] sampleNums = {1, 3, 5, 6};
            System.out.println(searchInsertPosition(sampleNums, 5));
            System.out.println(searchInsertPosition(sampleNums, 2));
            return;
        }

        String combinedInput = String.join(" ", args);
        try {
            ParsedQuery query = parseInput(combinedInput);
            System.out.println(searchInsertPosition(query.getNums(), query.getTarget()));
        } catch (IllegalArgumentException e) {
            System.err.println("Error: " + e.getMessage());
            System.exit(1);
        }
    }
}
