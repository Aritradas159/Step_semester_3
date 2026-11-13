package sorting_algorithms.assigment_problems;

/**
 * STEP 11 - Assignment Problem 3 (Problem 8): The Library Book Shelf
 *
 * Algorithm: INSERTION SORT (Ascending Order)
 *
 * Sorts an array of book numbers in ascending order by maintaining a sorted prefix
 * on the shelf and sliding each new book (starting from index 1) into its correct
 * position by shifting larger elements one position to the right.
 * Prints the entire shelf after each book has been placed.
 *
 * Complexity:
 * - Worst/Average Time Complexity: O(n^2) comparisons and shifts.
 * - Best-Case Time Complexity: O(n) comparisons when the shelf is already sorted.
 * - Auxiliary Space Complexity: O(1) in-place sorting.
 */
public class LibraryBookShelf {

    /**
     * Sorts books in ascending order using Insertion Sort and prints the shelf after each insertion.
     *
     * @param books array of book numbers
     * @throws IllegalArgumentException if books is null or empty
     */
    public static void shelf(int[] books) {
        if (books == null || books.length == 0) {
            throw new IllegalArgumentException("Books array must not be null or empty.");
        }

        for (int i = 1; i < books.length; i++) {
            int key = books[i];
            int j = i - 1;
            while (j >= 0 && books[j] > key) {
                books[j + 1] = books[j];
                j--;
            }
            books[j + 1] = key;
            System.out.println("After placing " + key + ": " + formatArray(books));
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
     * Parses inputs such as "books = [30, 10, 40, 20]" or "[30, 10, 40, 20]".
     */
    public static int[] parseBooks(String rawInput) {
        if (rawInput == null) {
            throw new IllegalArgumentException("Input cannot be null.");
        }
        String text = rawInput.trim();
        int eqIdx = text.indexOf('=');
        if (eqIdx != -1) {
            String lhs = text.substring(0, eqIdx).trim();
            if (!lhs.equals("books")) {
                throw new IllegalArgumentException("Unexpected variable name: " + lhs);
            }
            text = text.substring(eqIdx + 1).trim();
        }

        if (!text.startsWith("[") || !text.endsWith("]")) {
            throw new IllegalArgumentException("Books input must be enclosed in '[' and ']'.");
        }

        String inner = text.substring(1, text.length() - 1).trim();
        if (inner.isEmpty()) {
            throw new IllegalArgumentException("Books array cannot be empty.");
        }

        String[] tokens = inner.split(",");
        int[] books = new int[tokens.length];
        for (int i = 0; i < tokens.length; i++) {
            String token = tokens[i].trim();
            if (token.isEmpty()) {
                throw new IllegalArgumentException("Empty element in books array.");
            }
            try {
                books[i] = Integer.parseInt(token);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Invalid integer in books array: '" + token + "'", e);
            }
        }
        return books;
    }

    public static void main(String[] args) {
        if (args.length == 0) {
            int[] sample = {30, 10, 40, 20};
            shelf(sample);
            return;
        }

        String combinedInput = String.join(" ", args);
        try {
            int[] books = parseBooks(combinedInput);
            shelf(books);
        } catch (IllegalArgumentException e) {
            System.err.println("Error: " + e.getMessage());
            System.exit(1);
        }
    }
}
