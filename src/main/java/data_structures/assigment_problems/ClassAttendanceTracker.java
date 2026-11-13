package data_structures.assigment_problems;

import java.util.Scanner;

/**
 * Problem 6: Class Attendance Tracker
 * Calculates the total number of present days and the longest unbroken streak of present days in a single pass.
 *
 * Complexity Analysis:
 * - Time Complexity: O(n) — single traversal over the days array. Both answers are found in one pass.
 * - Extra Space Complexity: O(1) — only scalar counters (presentCount, currentStreak, longestStreak) are used.
 */
public class ClassAttendanceTracker {

    /**
     * Returns an array of two integers: [presentCount, longestStreak] in a single pass.
     *
     * @param days array of 0s and 1s (1 <= length <= 100)
     * @return int array where index 0 is total present days and index 1 is longest consecutive streak
     * @throws IllegalArgumentException if days is null, empty, length > 100, or contains values other than 0 or 1
     */
    public static int[] attendanceSummary(int[] days) {
        if (days == null || days.length < 1 || days.length > 100) {
            throw new IllegalArgumentException("Length of days must be between 1 and 100.");
        }

        int presentCount = 0;
        int currentStreak = 0;
        int longestStreak = 0;

        for (int i = 0; i < days.length; i++) {
            int day = days[i];
            if (day != 0 && day != 1) {
                throw new IllegalArgumentException("Each attendance value must be 0 or 1.");
            }

            if (day == 1) {
                presentCount++;
                currentStreak++;
                if (currentStreak > longestStreak) {
                    longestStreak = currentStreak;
                }
            } else {
                currentStreak = 0;
            }
        }

        return new int[]{presentCount, longestStreak};
    }

    /**
     * Formats the attendance summary matching the exact PDF output format.
     */
    public static String formatSummary(int[] summary) {
        return "Present: " + summary[0] + ", Longest streak: " + summary[1];
    }

    /**
     * Parses a line such as "days = [1, 1, 0, 1, 1, 1, 0, 1]" into an int array.
     */
    public static int[] parseDays(String line) {
        if (line == null || line.trim().isEmpty()) {
            throw new IllegalArgumentException("Input cannot be empty.");
        }
        String cleaned = line.trim();
        int eqIdx = cleaned.indexOf('=');
        if (eqIdx != -1) {
            cleaned = cleaned.substring(eqIdx + 1).trim();
        }
        if (cleaned.startsWith("[") && cleaned.endsWith("]")) {
            cleaned = cleaned.substring(1, cleaned.length() - 1).trim();
        }
        if (cleaned.isEmpty()) {
            throw new IllegalArgumentException("Days array cannot be empty.");
        }
        String[] parts = cleaned.split("[,\\s]+");
        int[] days = new int[parts.length];
        for (int i = 0; i < parts.length; i++) {
            try {
                days[i] = Integer.parseInt(parts[i].trim());
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Invalid integer value: " + parts[i]);
            }
        }
        return days;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        if (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            try {
                int[] days = parseDays(line);
                int[] summary = attendanceSummary(days);
                System.out.println(formatSummary(summary));
            } catch (IllegalArgumentException e) {
                System.out.println("Error: " + e.getMessage());
                System.exit(1);
            }
        } else {
            System.out.println("Error: No input provided.");
            System.exit(1);
        }
        scanner.close();
    }
}
