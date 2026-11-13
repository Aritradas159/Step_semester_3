package data_structures.class_problems;

import java.util.Scanner;

/**
 * Problem 6: Prime Number Checker
 * Reads a number greater than 1 and determines whether it is a prime number.
 */
public class PrimeNumberChecker {

    /**
     * Checks if a number greater than 1 is prime by testing divisors from 2 up to sqrt(n).
     *
     * @param n number greater than 1
     * @return true if prime, false if not prime
     * @throws IllegalArgumentException if n <= 1
     */
    public static boolean isPrime(int n) {
        if (n <= 1) {
            throw new IllegalArgumentException("Number must be greater than 1.");
        }
        for (int i = 2; i * i <= n; i++) {
            if (n % i == 0) {
                return false;
            }
        }
        return true;
    }

    /**
     * Formats the primality result matching the PDF output format.
     */
    public static String checkAndFormat(int n) {
        if (isPrime(n)) {
            return n + " is prime";
        } else {
            return n + " is not prime";
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
                int n = Integer.parseInt(line);
                System.out.println(checkAndFormat(n));
            } catch (NumberFormatException e) {
                System.out.println("Error: Invalid integer input.");
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
