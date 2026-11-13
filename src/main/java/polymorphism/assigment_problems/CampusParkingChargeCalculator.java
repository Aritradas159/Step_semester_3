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
 * Assignment Problem 2: The Campus Parking Charge Calculator.
 */
public class CampusParkingChargeCalculator {

    abstract static class Vehicle {
        protected final int hours;

        Vehicle(int hours) { this.hours = hours; }

        abstract String vehicleType();

        abstract BigDecimal charge();
    }

    static class Bike extends Vehicle {
        Bike(int h) { super(h); }
        @Override String vehicleType() { return "BIKE"; }
        @Override BigDecimal charge() { return BigDecimal.valueOf(10L * hours); }
    }

    static class Car extends Vehicle {
        Car(int h) { super(h); }
        @Override String vehicleType() { return "CAR"; }
        @Override BigDecimal charge() { return BigDecimal.valueOf(30L + 20L * (hours - 1)); }
    }

    static class Truck extends Vehicle {
        Truck(int h) { super(h); }
        @Override String vehicleType() { return "TRUCK"; }
        @Override BigDecimal charge() { return BigDecimal.valueOf(Math.max(100L, 50L * hours)); }
    }

    static Vehicle create(List<String> t) {
        if (t.size() != 2) throw new IllegalArgumentException("Expected: VehicleType Hours");
        int h = Integer.parseInt(t.get(1));
        if (h < 1) throw new IllegalArgumentException("Hours must be at least 1");
        switch (t.get(0).toUpperCase(Locale.ROOT)) {
            case "BIKE": return new Bike(h);
            case "CAR": return new Car(h);
            case "TRUCK": return new Truck(h);
            default: throw new IllegalArgumentException("Unknown vehicle type: " + t.get(0));
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
            List<Vehicle> items = new ArrayList<>();
            for (int i = 1; i <= n; i++) items.add(create(tokenize(lines.get(i))));

            StringBuilder out = new StringBuilder();
            BigDecimal total = BigDecimal.ZERO;
            for (Vehicle it : items) {                       // runtime polymorphism
                BigDecimal v = it.charge().setScale(2, RoundingMode.HALF_UP);
                total = total.add(v);
                out.append(it.vehicleType()).append(": ").append(fmt(v)).append('\n');
            }
            out.append("Total: ").append(fmt(total)).append('\n');
            System.out.print(out);
        } catch (RuntimeException e) {
            System.err.println("Error: " + e.getMessage());
            System.exit(1);
        }
    }
}
