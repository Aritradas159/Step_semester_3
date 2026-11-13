package data_structures.class_problems;

import java.util.Scanner;

/**
 * Problem 9: Leap Year Detector
 * Determines if a given year is a leap year.
 * Rule: Divisible by 4, but not by 100, unless also divisible by 400.
 */
public class LeapYearDetector {

    /**
     * Checks whether the given year is a leap year.
     *
     * @param year positive year value
     * @return true if leap year, false otherwise
     * @throws IllegalArgumentException if year <= 0
     */
    public static boolean isLeapYear(int year) {
        if (year <= 0) {
            throw new IllegalArgumentException("Year must be a positive integer.");
        }
        return (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0);
    }

    /**
     * Formats the leap year result matching the PDF output format.
     */
    public static String checkAndFormat(int year) {
        if (isLeapYear(year)) {
            return "Leap year";
        } else {
            return "Not a leap year";
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean hadInput = false;
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine().trim();
            if (line.isEmpty()) {
                continue;
            }
            hadInput = true;
            try {
                int year = Integer.parseInt(line);
                System.out.println(checkAndFormat(year));
            } catch (NumberFormatException e) {
                System.out.println("Error: Invalid year input.");
                System.exit(1);
            } catch (IllegalArgumentException e) {
                System.out.println("Error: " + e.getMessage());
                System.exit(1);
            }
        }
        if (!hadInput) {
            System.out.println("Error: No input provided.");
            System.exit(1);
        }
        scanner.close();
    }
}
