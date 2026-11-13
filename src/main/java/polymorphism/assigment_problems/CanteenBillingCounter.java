package polymorphism.assigment_problems;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Assignment Problem 1: The Canteen Billing Counter.
 */
public class CanteenBillingCounter {

    abstract static class Bill {
        protected final BigDecimal amount;

        Bill(BigDecimal amount) { this.amount = amount; }

        abstract String customerType();

        abstract BigDecimal finalAmount();
    }

    static class StudentBill extends Bill {
        StudentBill(BigDecimal a) { super(a); }
        @Override String customerType() { return "STUDENT"; }
        @Override BigDecimal finalAmount() { return amount.multiply(new BigDecimal("0.90")); }
    }

    static class StaffBill extends Bill {
        StaffBill(BigDecimal a) { super(a); }
        @Override String customerType() { return "STAFF"; }
        @Override BigDecimal finalAmount() { return amount.multiply(new BigDecimal("0.95")); }
    }

    static class GuestBill extends Bill {
        GuestBill(BigDecimal a) { super(a); }
        @Override String customerType() { return "GUEST"; }
        @Override BigDecimal finalAmount() { return amount.add(BigDecimal.TEN); }
    }

    static Bill create(List<String> t) {
        if (t.size() != 2) throw new IllegalArgumentException("Expected: CustomerType Amount");
        BigDecimal a = num(t.get(1));
        switch (t.get(0).toUpperCase(Locale.ROOT)) {
            case "STUDENT": return new StudentBill(a);
            case "STAFF": return new StaffBill(a);
            case "GUEST": return new GuestBill(a);
            default: throw new IllegalArgumentException("Unknown customer type: " + t.get(0));
        }
    }

    static String fmt(BigDecimal v) {
        return v.setScale(2, RoundingMode.HALF_UP).toPlainString();
    }

    static BigDecimal num(String s) {
        BigDecimal v = new BigDecimal(s);
        if (v.signum() < 0) throw new IllegalArgumentException("Negative value not allowed: " + s);
        return v;
    }

    /** Splits on whitespace but keeps "quoted strings" together (quotes removed). */
    static List<String> tokenize(String line) {
        List<String> out = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        boolean inQuote = false;
        for (char c : line.toCharArray()) {
            if (c == '"') {
                if (inQuote) { out.add(sb.toString()); sb.setLength(0); inQuote = false; }
                else { inQuote = true; }
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
                throw new IllegalArgumentException("Invalid N or missing input lines");
            }
            List<Bill> items = new ArrayList<>();
            for (int i = 1; i <= n; i++) items.add(create(tokenize(lines.get(i))));

            StringBuilder out = new StringBuilder();
            BigDecimal total = BigDecimal.ZERO;
            for (Bill it : items) {                       // runtime polymorphism
                BigDecimal v = it.finalAmount().setScale(2, RoundingMode.HALF_UP);
                total = total.add(v);
                out.append(it.customerType()).append(": ").append(fmt(v)).append('\n');
            }
            out.append("Total: ").append(fmt(total)).append('\n');
            System.out.print(out);
        } catch (RuntimeException e) {
            System.err.println("Error: " + e.getMessage());
            System.exit(1);
        }
    }
}
