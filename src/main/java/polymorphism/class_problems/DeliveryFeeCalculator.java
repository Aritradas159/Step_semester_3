package polymorphism.class_problems;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Class (Practice) Problem 3: Delivery Fee Calculator.
 */
public class DeliveryFeeCalculator {

    abstract static class Delivery {
        protected final BigDecimal weight, distance;

        Delivery(BigDecimal weight, BigDecimal distance) {
            this.weight = weight;
            this.distance = distance;
        }

        abstract String name();

        abstract BigDecimal fee();

        protected BigDecimal compute(String base, String perKg, String perKm) {
            return new BigDecimal(base)
                    .add(weight.multiply(new BigDecimal(perKg)))
                    .add(distance.multiply(new BigDecimal(perKm)));
        }
    }

    static class StandardDelivery extends Delivery {
        StandardDelivery(BigDecimal w, BigDecimal d) { super(w, d); }
        @Override String name() { return "STANDARD"; }
        @Override BigDecimal fee() { return compute("5", "0.50", "0.10"); }
    }

    static class ExpressDelivery extends Delivery {
        ExpressDelivery(BigDecimal w, BigDecimal d) { super(w, d); }
        @Override String name() { return "EXPRESS"; }
        @Override BigDecimal fee() { return compute("15", "1.00", "0.20"); }
    }

    static class InternationalDelivery extends Delivery {
        private final BigDecimal customsFee;

        InternationalDelivery(BigDecimal w, BigDecimal d, BigDecimal customsFee) {
            super(w, d);
            this.customsFee = customsFee;
        }
        @Override String name() { return "INTERNATIONAL"; }
        @Override BigDecimal fee() { return compute("25", "2.00", "0.50").add(customsFee); }
    }

    static Delivery create(List<String> t) {
        if (t.size() < 3) throw new IllegalArgumentException("Expected: DeliveryType Weight Distance [CustomsFee]");
        BigDecimal w = num(t.get(1)), d = num(t.get(2));
        switch (t.get(0).toUpperCase(Locale.ROOT)) {
            case "STANDARD": return new StandardDelivery(w, d);
            case "EXPRESS": return new ExpressDelivery(w, d);
            case "INTERNATIONAL":
                if (t.size() != 4) throw new IllegalArgumentException("INTERNATIONAL needs a CustomsFee");
                return new InternationalDelivery(w, d, num(t.get(3)));
            default: throw new IllegalArgumentException("Unknown delivery type: " + t.get(0));
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
            List<Delivery> items = new ArrayList<>();
            for (int i = 1; i <= n; i++) items.add(create(tokenize(lines.get(i))));

            StringBuilder out = new StringBuilder();
            BigDecimal total = BigDecimal.ZERO;
            for (Delivery it : items) {                       // runtime polymorphism
                BigDecimal v = it.fee().setScale(2, RoundingMode.HALF_UP);
                total = total.add(v);
                out.append(it.name()).append(": ").append(fmt(v)).append('\n');
            }
            out.append("Total: ").append(fmt(total)).append('\n');
            System.out.print(out);
        } catch (RuntimeException e) {
            System.err.println("Error: " + e.getMessage());
            System.exit(1);
        }
    }
}
