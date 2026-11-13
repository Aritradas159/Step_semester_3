package sorting_algorithms.class_problems;

/**
 * STEP 11 - Class Problem 1: Warehouse Bin Grid Scan
 *
 * Scans an m x n warehouse floor grid of non-negative item counts to compute:
 * 1. The total number of items across all bins.
 * 2. The zero-based (row, col) coordinates of the bin holding the maximum items.
 *    If multiple bins tie for the maximum value, the first one encountered when
 *    scanning row by row from left to right is returned.
 *
 * Complexity:
 * - Time Complexity: O(m * n), where m is the number of rows and n is the number of columns,
 *   because every cell in the grid is inspected exactly once.
 * - Additional Space Complexity: O(1) beyond the input grid itself, using only a few scalar
 *   variables to track the running total, maximum value, and coordinates.
 */
public class WarehouseBinGridScan {

    /**
     * Immutable value object holding the warehouse scan summary.
     */
    public static final class WarehouseSummary {
        private final long total;
        private final int maxValue;
        private final int maxRow;
        private final int maxCol;

        public WarehouseSummary(long total, int maxValue, int maxRow, int maxCol) {
            this.total = total;
            this.maxValue = maxValue;
            this.maxRow = maxRow;
            this.maxCol = maxCol;
        }

        public long getTotal() {
            return total;
        }

        public int getMaxValue() {
            return maxValue;
        }

        public int getMaxRow() {
            return maxRow;
        }

        public int getMaxCol() {
            return maxCol;
        }

        @Override
        public String toString() {
            return "total = " + total + ", maxCoordinate = (" + maxRow + ", " + maxCol + ")";
        }
    }

    /**
     * Scans the warehouse grid and returns the total item count and coordinates of the maximum bin.
     *
     * @param grid 2D array of non-negative item counts
     * @return WarehouseSummary containing total items and (row, col) of the first maximum bin
     * @throws IllegalArgumentException if the grid is null, empty, ragged, or contains negative values
     */
    public static WarehouseSummary warehouseSummary(int[][] grid) {
        if (grid == null || grid.length == 0) {
            throw new IllegalArgumentException("Grid must not be null or empty.");
        }
        if (grid[0] == null || grid[0].length == 0) {
            throw new IllegalArgumentException("Grid rows must not be null or empty.");
        }

        int cols = grid[0].length;
        long total = 0L;
        int maxValue = -1;
        int maxRow = 0;
        int maxCol = 0;

        for (int r = 0; r < grid.length; r++) {
            int[] row = grid[r];
            if (row == null || row.length != cols) {
                throw new IllegalArgumentException("All grid rows must be non-null and have equal length.");
            }
            for (int c = 0; c < cols; c++) {
                int count = row[c];
                if (count < 0) {
                    throw new IllegalArgumentException(
                            "Bin item count cannot be negative at (" + r + ", " + c + "): " + count);
                }
                total += count;
                // Strictly greater (>) ensures the first encountered maximum in row-major order wins ties.
                if (count > maxValue) {
                    maxValue = count;
                    maxRow = r;
                    maxCol = c;
                }
            }
        }

        return new WarehouseSummary(total, maxValue, maxRow, maxCol);
    }

    /**
     * Parses a 2D integer grid from strings such as:
     * "grid = [ [4, 9, 2], [7, 1, 6], [3, 12, 5] ]" or "[[4, 9, 2], [7, 1, 6], [3, 12, 5]]".
     */
    public static int[][] parseGrid(String rawInput) {
        if (rawInput == null) {
            throw new IllegalArgumentException("Input string cannot be null.");
        }
        String text = rawInput.trim();
        int eqIndex = text.indexOf('=');
        if (eqIndex != -1) {
            String lhs = text.substring(0, eqIndex).trim();
            if (!lhs.equals("grid")) {
                throw new IllegalArgumentException("Unexpected variable name: " + lhs);
            }
            text = text.substring(eqIndex + 1).trim();
        }

        if (!text.startsWith("[") || !text.endsWith("]")) {
            throw new IllegalArgumentException("Grid input must be enclosed in square brackets.");
        }

        String inner = text.substring(1, text.length() - 1).trim();
        if (inner.isEmpty()) {
            throw new IllegalArgumentException("Grid cannot be empty.");
        }

        java.util.List<int[]> rows = new java.util.ArrayList<>();
        int idx = 0;
        while (idx < inner.length()) {
            while (idx < inner.length() && (Character.isWhitespace(inner.charAt(idx)) || inner.charAt(idx) == ',')) {
                idx++;
            }
            if (idx >= inner.length()) {
                break;
            }
            if (inner.charAt(idx) != '[') {
                throw new IllegalArgumentException("Expected '[' at start of row in grid input.");
            }
            int closeBracket = inner.indexOf(']', idx);
            if (closeBracket == -1) {
                throw new IllegalArgumentException("Missing closing ']' for grid row.");
            }
            String rowContent = inner.substring(idx + 1, closeBracket).trim();
            if (rowContent.isEmpty()) {
                throw new IllegalArgumentException("Grid row cannot be empty.");
            }
            String[] parts = rowContent.split(",");
            int[] row = new int[parts.length];
            for (int i = 0; i < parts.length; i++) {
                String token = parts[i].trim();
                if (token.isEmpty()) {
                    throw new IllegalArgumentException("Empty value in grid row.");
                }
                try {
                    row[i] = Integer.parseInt(token);
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException("Invalid integer in grid: '" + token + "'", e);
                }
            }
            rows.add(row);
            idx = closeBracket + 1;
        }

        if (rows.isEmpty()) {
            throw new IllegalArgumentException("No rows found in grid.");
        }

        int[][] grid = new int[rows.size()][];
        for (int i = 0; i < rows.size(); i++) {
            grid[i] = rows.get(i);
        }
        return grid;
    }

    public static void main(String[] args) {
        if (args.length == 0) {
            int[][] sampleGrid = {
                {4, 9, 2},
                {7, 1, 6},
                {3, 12, 5}
            };
            WarehouseSummary result = warehouseSummary(sampleGrid);
            System.out.println(result);
            return;
        }

        String combinedInput = String.join(" ", args);
        try {
            int[][] grid = parseGrid(combinedInput);
            WarehouseSummary summary = warehouseSummary(grid);
            System.out.println(summary);
        } catch (IllegalArgumentException e) {
            System.err.println("Error: " + e.getMessage());
            System.exit(1);
        }
    }
}
