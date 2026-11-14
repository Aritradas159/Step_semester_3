package linked_lists.class_problems;

import java.io.IOException;
import java.util.Scanner;

/**
 * STEP 12 - Category C Practice Problem 4: Payment Processing
 *
 * Task:
 * A shop accepts cash, card and UPI payments. Card payments carry a 2% fee;
 * cash and UPI carry none.
 * Define an interface PaymentMode with double processPayment(double amount)
 * and implement it in CashPayment, CardPayment and UpiPayment. Store the payments
 * in one PaymentMode[] array, print each payment's details and the total processed.
 *
 * Complexity Analysis:
 * - Time Complexity: O(n), where n is the number of payments. Each payment in the
 *   PaymentMode[] array is processed once in O(1) time via polymorphic dispatch.
 * - Space Complexity: O(n) to store the n PaymentMode instances and their amounts.
 */
public class PaymentProcessing {

    public interface PaymentMode {
        double processPayment(double amount);
    }

    public static class CashPayment implements PaymentMode {
        @Override
        public double processPayment(double amount) {
            validateAmount(amount);
            double fee = 0.0;
            double processed = amount + fee;
            System.out.println("Cash: amount " + amount + ", fee " + fee + ", processed " + processed);
            return processed;
        }
    }

    public static class CardPayment implements PaymentMode {
        private static final double FEE_RATE = 0.02;

        @Override
        public double processPayment(double amount) {
            validateAmount(amount);
            double fee = amount * FEE_RATE;
            double processed = amount + fee;
            System.out.println("Card: amount " + amount + ", fee " + fee + ", processed " + processed);
            return processed;
        }
    }

    public static class UpiPayment implements PaymentMode {
        @Override
        public double processPayment(double amount) {
            validateAmount(amount);
            double fee = 0.0;
            double processed = amount + fee;
            System.out.println("UPI: amount " + amount + ", fee " + fee + ", processed " + processed);
            return processed;
        }
    }

    private static void validateAmount(double amount) {
        if (amount <= 0 || amount > 1_000_000) {
            throw new IllegalArgumentException("Payment amount must be between 1 and 1000000.");
        }
    }

    public static PaymentMode createPaymentMode(String mode) {
        if (mode == null) {
            throw new IllegalArgumentException("Payment mode cannot be null.");
        }
        switch (mode.trim()) {
            case "Cash":
                return new CashPayment();
            case "Card":
                return new CardPayment();
            case "UPI":
                return new UpiPayment();
            default:
                throw new IllegalArgumentException("Unknown payment mode: " + mode);
        }
    }

    /**
     * Processes all payments in a single loop over the PaymentMode[] array without checking types.
     */
    public static double processAllPayments(PaymentMode[] payments, double[] amounts) {
        if (payments == null || amounts == null || payments.length != amounts.length) {
            throw new IllegalArgumentException("Payments and amounts arrays must be non-null and equal in length.");
        }
        double totalProcessed = 0.0;
        for (int i = 0; i < payments.length; i++) {
            totalProcessed += payments[i].processPayment(amounts[i]);
        }
        System.out.println("Total Amount Processed: " + totalProcessed);
        return totalProcessed;
    }

    public static void processFromScanner(Scanner scanner) {
        if (!scanner.hasNextInt()) {
            return;
        }
        int n = scanner.nextInt();
        if (n < 1 || n > 100) {
            System.out.println("Invalid number of payments: must be between 1 and 100.");
            return;
        }
        PaymentMode[] payments = new PaymentMode[n];
        double[] amounts = new double[n];
        for (int i = 0; i < n; i++) {
            String mode = scanner.next();
            double amount = scanner.nextDouble();
            payments[i] = createPaymentMode(mode);
            amounts[i] = amount;
        }
        processAllPayments(payments, amounts);
    }

    public static void main(String[] args) throws IOException {
        if (System.in.available() > 0) {
            Scanner scanner = new Scanner(System.in);
            processFromScanner(scanner);
            return;
        }

        // Default execution with the exact PDF sample input
        PaymentMode[] samplePayments = new PaymentMode[] {
            new CashPayment(),
            new CardPayment(),
            new UpiPayment(),
            new CardPayment()
        };
        double[] sampleAmounts = new double[] { 1000, 2000, 1500, 3000 };

        processAllPayments(samplePayments, sampleAmounts);
    }
}
