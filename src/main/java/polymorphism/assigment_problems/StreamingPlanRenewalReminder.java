package polymorphism.assigment_problems;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Assignment Problem 5: The Streaming Plan Renewal Reminder.
 */
public class StreamingPlanRenewalReminder {

    abstract static class Plan {
        protected final String subscriber;
        protected final LocalDate startDate;

        Plan(String subscriber, LocalDate startDate) {
            this.subscriber = subscriber;
            this.startDate = startDate;
        }

        abstract int validityDays();

        LocalDate renewalDate() { return startDate.plusDays(validityDays()); }
    }

    static class BasicPlan extends Plan {
        BasicPlan(String n, LocalDate d) { super(n, d); }
        @Override int validityDays() { return 30; }
    }

    static class StandardPlan extends Plan {
        StandardPlan(String n, LocalDate d) { super(n, d); }
        @Override int validityDays() { return 90; }
    }

    static class PremiumPlan extends Plan {
        PremiumPlan(String n, LocalDate d) { super(n, d); }
        @Override int validityDays() { return 365; }
    }

    static Plan create(List<String> t) {
        if (t.size() != 3) throw new IllegalArgumentException("Expected: PlanType Name StartDate");
        LocalDate d;
        try {
            d = LocalDate.parse(t.get(2));
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid date (use YYYY-MM-DD): " + t.get(2));
        }
        switch (t.get(0).toUpperCase(Locale.ROOT)) {
            case "BASIC": return new BasicPlan(t.get(1), d);
            case "STANDARD": return new StandardPlan(t.get(1), d);
            case "PREMIUM": return new PremiumPlan(t.get(1), d);
            default: throw new IllegalArgumentException("Unknown plan type: " + t.get(0));
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
            List<Plan> plans = new ArrayList<>();
            for (int i = 1; i <= n; i++) plans.add(create(tokenize(lines.get(i))));

            StringBuilder out = new StringBuilder();
            for (Plan p : plans) {                           // runtime polymorphism
                out.append(p.subscriber).append(": ").append(p.renewalDate()).append('\n');
            }
            System.out.print(out);
        } catch (RuntimeException e) {
            System.err.println("Error: " + e.getMessage());
            System.exit(1);
        }
    }
}
