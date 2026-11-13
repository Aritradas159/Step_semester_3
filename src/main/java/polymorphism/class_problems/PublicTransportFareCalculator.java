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
 * Class (Practice) Problem 5: Public Transport Fare Calculator.
 */
public class PublicTransportFareCalculator {

    abstract static class Journey {
        protected final BigDecimal distance;

        Journey(BigDecimal distance) { this.distance = distance; }

        abstract String name();

        abstract BigDecimal fare();
    }

    static class BusJourney extends Journey {
        private static final BigDecimal MAX_FARE = new BigDecimal("10");

        BusJourney(BigDecimal d) { super(d); }
        @Override String name() { return "BUS"; }

        @Override BigDecimal fare() {
            BigDecimal f = new BigDecimal("2").add(distance.multiply(new BigDecimal("0.10")));
            return f.min(MAX_FARE);
        }
    }

    static class TrainJourney extends Journey {
        TrainJourney(BigDecimal d) { super(d); }
        @Override String name() { return "TRAIN"; }

        @Override BigDecimal fare() {
            return new BigDecimal("3").add(distance.multiply(new BigDecimal("0.15")));
        }
    }

    static class MetroJourney extends Journey {
        private final BigDecimal peakHourFactor;

        MetroJourney(BigDecimal d, BigDecimal factor) {
            super(d);
            this.peakHourFactor = factor;
        }
        @Override String name() { return "METRO"; }

        @Override BigDecimal fare() {
            return new BigDecimal("1.50").add(distance.multiply(new BigDecimal("0.20")))
                    .multiply(peakHourFactor);
        }
    }

    static Journey create(List<String> t) {
        if (t.size() < 2) throw new IllegalArgumentException("Expected: TransportType Distance [PeakHourFactor]");
        BigDecimal d = num(t.get(1));
        switch (t.get(0).toUpperCase(Locale.ROOT)) {
            case "BUS": return new BusJourney(d);
            case "TRAIN": return new TrainJourney(d);
            case "METRO":
                if (t.size() != 3) throw new IllegalArgumentException("METRO needs a PeakHourFactor");
                BigDecimal f = num(t.get(2));
                if (f.compareTo(BigDecimal.ONE) < 0 || f.compareTo(new BigDecimal("2")) > 0) {
                    throw new IllegalArgumentException("PeakHourFactor must be between 1.0 and 2.0");
                }
                return new MetroJourney(d, f);
            default: throw new IllegalArgumentException("Unknown transport type: " + t.get(0));
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
            List<Journey> items = new ArrayList<>();
            for (int i = 1; i <= n; i++) items.add(create(tokenize(lines.get(i))));

            StringBuilder out = new StringBuilder();
            BigDecimal total = BigDecimal.ZERO;
            for (Journey it : items) {                       // runtime polymorphism
                BigDecimal v = it.fare().setScale(2, RoundingMode.HALF_UP);
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
