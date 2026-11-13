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
 * Assignment Problem 3: The Hostel Electricity Bill.
 */
public class HostelElectricityBill {

    abstract static class Room {
        protected final BigDecimal units;

        Room(BigDecimal units) { this.units = units; }

        abstract String roomType();

        abstract BigDecimal bill();
    }

    static class SingleRoom extends Room {
        SingleRoom(BigDecimal u) { super(u); }
        @Override String roomType() { return "SINGLE"; }
        @Override BigDecimal bill() { return units.multiply(new BigDecimal("8")); }
    }

    /** The extra value (occupants) is stored in the subclass, so callers still treat it as a Room. */
    static class SharedRoom extends Room {
        private final int occupants;

        SharedRoom(BigDecimal u, int occupants) {
            super(u);
            this.occupants = occupants;
        }
        @Override String roomType() { return "SHARED"; }

        @Override BigDecimal bill() {
            return units.multiply(new BigDecimal("6")).divide(BigDecimal.valueOf(occupants), 10, RoundingMode.HALF_UP);
        }
    }

    static class AcRoom extends Room {
        AcRoom(BigDecimal u) { super(u); }
        @Override String roomType() { return "AC"; }
        @Override BigDecimal bill() { return units.multiply(BigDecimal.TEN).add(new BigDecimal("200")); }
    }

    static Room create(List<String> t) {
        if (t.size() < 2) throw new IllegalArgumentException("Expected: RoomType Units [Occupants]");
        BigDecimal u = num(t.get(1));
        switch (t.get(0).toUpperCase(Locale.ROOT)) {
            case "SINGLE": return new SingleRoom(u);
            case "SHARED":
                if (t.size() != 3) throw new IllegalArgumentException("SHARED needs Occupants");
                int occ = Integer.parseInt(t.get(2));
                if (occ < 2 || occ > 4) throw new IllegalArgumentException("Occupants must be between 2 and 4");
                return new SharedRoom(u, occ);
            case "AC": return new AcRoom(u);
            default: throw new IllegalArgumentException("Unknown room type: " + t.get(0));
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
            List<Room> items = new ArrayList<>();
            for (int i = 1; i <= n; i++) items.add(create(tokenize(lines.get(i))));

            StringBuilder out = new StringBuilder();
            BigDecimal total = BigDecimal.ZERO;
            for (Room it : items) {                       // runtime polymorphism
                BigDecimal v = it.bill().setScale(2, RoundingMode.HALF_UP);
                total = total.add(v);
                out.append(it.roomType()).append(": ").append(fmt(v)).append('\n');
            }
            out.append("Total: ").append(fmt(total)).append('\n');
            System.out.print(out);
        } catch (RuntimeException e) {
            System.err.println("Error: " + e.getMessage());
            System.exit(1);
        }
    }
}
