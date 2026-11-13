package polymorphism.class_problems;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

/**
 * Class (Practice) Problem 1: Payment System Fee Calculation.
 * Each payment method overrides finalAmount(); the processor treats all uniformly.
 */
public class PaymentFeeCalculator {

    /** Base type: common operation, specialised by each payment method. */
    abstract static class Payment {
        protected final BigDecimal amount;

        Payment(BigDecimal amount) {
            this.amount = amount;
        }

        abstract String name();

        abstract BigDecimal finalAmount();
    }

    static class CardPayment extends Payment {
        CardPayment(BigDecimal amount) { super(amount); }

        @Override String name() { return "CARD"; }

        @Override BigDecimal finalAmount() { return amount.multiply(new BigDecimal("1.02")); }
    }

    static class WalletPayment extends Payment {
        WalletPayment(BigDecimal amount) { super(amount); }

        @Override String name() { return "WALLET"; }

        @Override BigDecimal finalAmount() { return amount.multiply(new BigDecimal("1.01")); }
    }

    static class BankTransferPayment extends Payment {
        BankTransferPayment(BigDecimal amount) { super(amount); }

        @Override String name() { return "BANKTRANSFER"; }

        @Override BigDecimal finalAmount() { return amount; }
    }

    static Payment create(List<String> t) {
        if (t.size() != 2) {
            throw new IllegalArgumentException("Expected: PaymentType Amount");
        }
        BigDecimal amount = new BigDecimal(t.get(1));
        if (amount.signum() < 0) {
            throw new IllegalArgumentException("Amount must not be negative");
        }
        switch (t.get(0).toUpperCase()) {
            case "CARD": return new CardPayment(amount);
            case "WALLET": return new WalletPayment(amount);
            case "BANKTRANSFER": return new BankTransferPayment(amount);
            default: throw new IllegalArgumentException("Unknown payment type: " + t.get(0));
        }
    }

    static String fmt(BigDecimal v) {
        return v.setScale(2, RoundingMode.HALF_UP).toPlainString();
    }

    static List<String> tokenize(String line) {
        List<String> out = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        boolean inQuote = false, pending = false;
        for (char c : line.toCharArray()) {
            if (c == '"') {
                if (inQuote) { out.add(sb.toString()); sb.setLength(0); inQuote = false; pending = false; }
                else { inQuote = true; pending = true; }
            } else if (Character.isWhitespace(c) && !inQuote) {
                if (sb.length() > 0) { out.add(sb.toString()); sb.setLength(0); }
            } else {
                sb.append(c);
            }
        }
        if (inQuote) throw new IllegalArgumentException("Unterminated quote: " + line);
        if (sb.length() > 0) out.add(sb.toString());
        return out;
    }

    static List<String> readLines() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        List<String> lines = new ArrayList<>();
        String s;
        while ((s = br.readLine()) != null) {
            if (!s.trim().isEmpty()) lines.add(s.trim());
        }
        return lines;
    }

    public static void main(String[] args) throws IOException {
        try {
            List<String> lines = readLines();
            if (lines.isEmpty()) throw new IllegalArgumentException("No input");
            int n = Integer.parseInt(lines.get(0));
            if (n < 1 || lines.size() - 1 < n) {
                throw new IllegalArgumentException("Invalid N or missing transaction lines");
            }
            List<Payment> payments = new ArrayList<>();
            for (int i = 1; i <= n; i++) payments.add(create(tokenize(lines.get(i))));

            StringBuilder out = new StringBuilder();
            BigDecimal total = BigDecimal.ZERO;
            for (Payment p : payments) {                       // runtime polymorphism
                BigDecimal v = p.finalAmount().setScale(2, RoundingMode.HALF_UP);
                total = total.add(v);
                out.append(p.name()).append(": ").append(fmt(v)).append('\n');
            }
            out.append("Total: ").append(fmt(total)).append('\n');
            System.out.print(out);
        } catch (RuntimeException e) {
            System.err.println("Error: " + e.getMessage());
            System.exit(1);
        }
    }
}
