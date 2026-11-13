package data_structures.assigment_problems;

import java.util.Scanner;

/**
 * Problem 10: Busiest Bus Row
 * Finds the index (0-based) of the row with the largest student total and that total in a 2D seat grid.
 * On ties, preserves the smaller row index.
 *
 * Complexity Analysis:
 * - Time Complexity: O(r * c), where r is the number of rows and c is the number of columns.
 * - Extra Space Complexity: O(1) — only scalar variables (bestRow, maxTotal, rowSum) are used.
 */
public class BusiestBusRow {

    /**
     * Returns an array of two integers: [bestRowIndex, maxTotal].
     *
     * @param grid 2D array of seat counts (1 <= rows, columns <= 10, 0 <= grid[i][j] <= 3)
     * @return int array where index 0 is the 0-based row index and index 1 is the row total
     * @throws IllegalArgumentException if grid is null, empty, ragged, or violates constraints
     */
    public static int[] busiestRow(int[][] grid) {
        if (grid == null || grid.length < 1 || grid.length > 10) {
            throw new IllegalArgumentException("Number of rows must be between 1 and 10.");
        }
        if (grid[0] == null || grid[0].length < 1 || grid[0].length > 10) {
            throw new IllegalArgumentException("Number of columns must be between 1 and 10.");
        }

        int cols = grid[0].length;
        int bestRow = 0;
        int maxTotal = -1;

        for (int i = 0; i < grid.length; i++) {
            if (grid[i] == null || grid[i].length != cols) {
                throw new IllegalArgumentException("All rows in the grid must have the same number of columns.");
            }

            int rowSum = 0;
            for (int j = 0; j < cols; j++) {
                int seats = grid[i][j];
                if (seats < 0 || seats > 3) {
                    throw new IllegalArgumentException("Seat count must be between 0 and 3.");
                }
                rowSum += seats;
            }

            if (rowSum > maxTotal) {
                maxTotal = rowSum;
                bestRow = i;
            }
        }

        return new int[]{bestRow, maxTotal};
    }

    /**
     * Formats the result matching the exact PDF output format: "Row <index>, Total <total>".
     */
    public static String formatResult(int[] result) {
        return "Row " + result[0] + ", Total " + result[1];
    }

    /**
     * Parses a 2D bracketed grid string such as "grid = [[2, 0, 1], [3, 3, 1], [1, 1, 1]]" into an int[][].
     */
    public static int[][] parseGrid(String input) {
        if (input == null || input.trim().isEmpty()) {
            throw new IllegalArgumentException("Input cannot be empty.");
        }
        String cleaned = input.trim();
        int eqIdx = cleaned.indexOf('=');
        if (eqIdx != -1) {
            cleaned = cleaned.substring(eqIdx + 1).trim();
        }
        if (!cleaned.startsWith("[[") || !cleaned.endsWith("]]")) {
            throw new IllegalArgumentException("Expected 2D grid in [[...], [...]] format.");
        }
        cleaned = cleaned.substring(2, cleaned.length() - 2).trim();
        if (cleaned.isEmpty()) {
            throw new IllegalArgumentException("Grid cannot be empty.");
        }

        String[] rowStrings = cleaned.split("\\]\\s*,\\s*\\[");
        int[][] grid = new int[rowStrings.length][];
        for (int i = 0; i < rowStrings.length; i++) {
            String rowStr = rowStrings[i].replace("[", "").replace("]", "").trim();
            if (rowStr.isEmpty()) {
                throw new IllegalArgumentException("Grid row cannot be empty.");
            }
            String[] colTokens = rowStr.split("[,\\s]+");
            grid[i] = new int[colTokens.length];
            for (int j = 0; j < colTokens.length; j++) {
                try {
                    grid[i][j] = Integer.parseInt(colTokens[j].trim());
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException("Invalid integer in grid: " + colTokens[j]);
                }
            }
        }
        return grid;
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
            int[][] grid = parseGrid(input);
            int[] result = busiestRow(grid);
            System.out.println(formatResult(result));
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
            System.exit(1);
        }
    }
}
