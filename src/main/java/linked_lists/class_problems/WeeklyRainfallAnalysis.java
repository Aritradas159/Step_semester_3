package linked_lists.class_problems;

import java.io.IOException;
import java.util.Locale;
import java.util.Scanner;

/**
 * STEP 12 - Category C Practice Problem 5: Weekly Rainfall Analysis
 *
 * Task:
 * Rainfall is recorded for seven days. Report the total, the average to two decimals,
 * the wettest day (counting from 1) with its rainfall, and the number of dry days.
 * Store the readings in a double[] array of size 7 and compute everything with loops.
 *
 * Complexity Analysis:
 * - Time Complexity: O(n) where n = 7 days (which is O(1) constant time for a fixed
 *   7-day week), as we iterate through the 7 readings with simple loops.
 * - Space Complexity: O(n) where n = 7 (O(1) constant space for the double[7] array).
 */
public class WeeklyRainfallAnalysis {

    public static final int DAYS_IN_WEEK = 7;

    public static class RainfallReport {
        public final double total;
        public final double average;
        public final int wettestDay; // 1-based index
        public final double maxRainfall;
        public final int dryDays;

        public RainfallReport(double total, double average, int wettestDay, double maxRainfall, int dryDays) {
            this.total = total;
            this.average = average;
            this.wettestDay = wettestDay;
            this.maxRainfall = maxRainfall;
            this.dryDays = dryDays;
        }
    }

    /**
     * Analyzes a 7-day rainfall array using loops and prints the formatted report.
     */
    public static RainfallReport analyzeRainfall(double[] readings) {
        if (readings == null || readings.length != DAYS_IN_WEEK) {
            throw new IllegalArgumentException("Readings array must contain exactly 7 days of rainfall data.");
        }

        for (int i = 0; i < DAYS_IN_WEEK; i++) {
            if (readings[i] < 0 || readings[i] > 500) {
                throw new IllegalArgumentException("Rainfall must be between 0 and 500 mm.");
            }
        }

        StringBuilder line = new StringBuilder("Rainfall (mm):");
        for (int i = 0; i < DAYS_IN_WEEK; i++) {
            line.append(" ").append(readings[i]);
        }
        System.out.println(line);

        double total = 0.0;
        int wettestIndex = 0;
        int dryDays = 0;

        for (int i = 0; i < DAYS_IN_WEEK; i++) {
            total += readings[i];
            if (readings[i] > readings[wettestIndex]) {
                wettestIndex = i;
            }
            if (readings[i] == 0.0) {
                dryDays++;
            }
        }

        double average = total / DAYS_IN_WEEK;
        int wettestDay = wettestIndex + 1;
        double maxRainfall = readings[wettestIndex];

        System.out.println("Total Rainfall: " + total + " mm");
        System.out.printf(Locale.US, "Average Rainfall: %.2f mm%n", average);
        System.out.println("Wettest Day: Day " + wettestDay + " (" + maxRainfall + " mm)");
        System.out.println("Total Dry Days: " + dryDays);

        return new RainfallReport(total, average, wettestDay, maxRainfall, dryDays);
    }

    public static void processFromScanner(Scanner scanner) {
        double[] readings = new double[DAYS_IN_WEEK];
        for (int i = 0; i < DAYS_IN_WEEK; i++) {
            if (!scanner.hasNextDouble()) {
                throw new IllegalArgumentException("Expected 7 rainfall readings.");
            }
            readings[i] = scanner.nextDouble();
        }
        analyzeRainfall(readings);
    }

    public static void main(String[] args) throws IOException {
        if (System.in.available() > 0) {
            Scanner scanner = new Scanner(System.in);
            processFromScanner(scanner);
            return;
        }

        // Default execution with the exact PDF sample input
        double[] sampleReadings = new double[] { 12, 0, 25, 18, 0, 30, 15 };
        analyzeRainfall(sampleReadings);
    }
}
