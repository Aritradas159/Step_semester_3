package sorting_algorithms.class_problems;

/**
 * STEP 11 - Class Problem 5: Remove Duplicates From a Sorted Array In-Place
 *
 * Uses the Two-Pointer technique to deduplicate a non-decreasing sorted array in-place
 * while preserving the relative order of unique elements.
 *
 * Complexity & In-Place Justification:
 * - Time Complexity: O(n), because the fast pointer scans the array of length n once.
 * - Additional Space Complexity: O(1), because it modifies the existing array directly
 *   using only two integer index pointers (writeIndex and readIndex) without allocating
 *   a second array of comparable size to the input.
 */
public class RemoveDuplicatesSortedArray {

    /**
     * Removes consecutive duplicate values in-place from a sorted ascending (non-decreasing) array
     * and returns the count k of unique elements placed in nums[0..k-1].
     *
     * @param nums sorted array in non-decreasing order
     * @return count of unique elements
     * @throws IllegalArgumentException if nums is null or not sorted in non-decreasing order
     */
    public static int removeDuplicates(int[] nums) {
        if (nums == null) {
            throw new IllegalArgumentException("Input array must not be null.");
        }
        if (nums.length == 0) {
            return 0;
        }

        for (int i = 1; i < nums.length; i++) {
            if (nums[i] < nums[i - 1]) {
                throw new IllegalArgumentException(
                        "Input array must be sorted in ascending (non-decreasing) order.");
            }
        }

        int writeIndex = 1;
        for (int readIndex = 1; readIndex < nums.length; readIndex++) {
            if (nums[readIndex] != nums[writeIndex - 1]) {
                nums[writeIndex] = nums[readIndex];
                writeIndex++;
            }
        }
        return writeIndex;
    }

    /**
     * Formats the prefix nums[0..length-1] as a bracketed array string.
     */
    public static String formatPrefix(int[] nums, int length) {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < length; i++) {
            if (i > 0) {
                sb.append(", ");
            }
            sb.append(nums[i]);
        }
        sb.append("]");
        return sb.toString();
    }

    /**
     * Formats the output matching the PDF specification:
     * length = 5
     * nums[0..4] = [1, 2, 3, 4, 5]
     */
    public static String formatResult(int[] nums, int length) {
        if (length == 0) {
            return "length = 0" + System.lineSeparator() + "nums = []";
        }
        return "length = " + length + System.lineSeparator()
                + "nums[0.." + (length - 1) + "] = " + formatPrefix(nums, length);
    }

    /**
     * Parses strings such as "nums = [1, 1, 2, 2, 2, 3, 4, 4, 5]" or "[1, 1, 2]".
     */
    public static int[] parseNums(String rawInput) {
        if (rawInput == null) {
            throw new IllegalArgumentException("Input cannot be null.");
        }
        String text = rawInput.trim();
        int eqIdx = text.indexOf('=');
        if (eqIdx != -1) {
            String lhs = text.substring(0, eqIdx).trim();
            if (!lhs.equals("nums")) {
                throw new IllegalArgumentException("Unexpected variable name: " + lhs);
            }
            text = text.substring(eqIdx + 1).trim();
        }

        if (!text.startsWith("[") || !text.endsWith("]")) {
            throw new IllegalArgumentException("Input must be enclosed in '[' and ']'.");
        }

        String inner = text.substring(1, text.length() - 1).trim();
        if (inner.isEmpty()) {
            return new int[0];
        }

        String[] tokens = inner.split(",");
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
        return nums;
    }

    public static void main(String[] args) {
        if (args.length == 0) {
            int[] sample = {1, 1, 2, 2, 2, 3, 4, 4, 5};
            int length = removeDuplicates(sample);
            System.out.println(formatResult(sample, length));
            return;
        }

        String combinedInput = String.join(" ", args);
        try {
            int[] nums = parseNums(combinedInput);
            int length = removeDuplicates(nums);
            System.out.println(formatResult(nums, length));
        } catch (IllegalArgumentException e) {
            System.err.println("Error: " + e.getMessage());
            System.exit(1);
        }
    }
}
