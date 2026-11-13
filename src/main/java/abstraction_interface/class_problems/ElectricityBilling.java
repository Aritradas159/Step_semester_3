package abstraction_interface.class_problems;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Problem 4: Electricity Connection Billing.
 * The abstract base class stores the common units; each connection type bills differently.
 */
public class ElectricityBilling {

    abstract static class ElectricityConnection {
        private final double units;

        ElectricityConnection(double units) {
            this.units = units;
        }

        double getUnits() {
            return units;
        }

        abstract String getTypeName();

        abstract double calculateBill();
    }

    static class HomeConnection extends ElectricityConnection {
        private static final double FIRST_SLAB_UNITS = 100;
        private static final double FIRST_SLAB_RATE = 5;
        private static final double NEXT_SLAB_RATE = 7;

        HomeConnection(double units) {
            super(units);
        }

        @Override
        String getTypeName() {
            return "HOME";
        }

        @Override
        double calculateBill() {
            double firstSlab = Math.min(getUnits(), FIRST_SLAB_UNITS);
            double remaining = Math.max(getUnits() - FIRST_SLAB_UNITS, 0);
            return firstSlab * FIRST_SLAB_RATE + remaining * NEXT_SLAB_RATE;
        }
    }

    static class ShopConnection extends ElectricityConnection {
        private static final double RATE_PER_UNIT = 8;
        private static final double FIXED_CHARGE = 100;

        ShopConnection(double units) {
            super(units);
        }

        @Override
        String getTypeName() {
            return "SHOP";
        }

        @Override
        double calculateBill() {
            return getUnits() * RATE_PER_UNIT + FIXED_CHARGE;
        }
    }

    static class FactoryConnection extends ElectricityConnection {
        private static final double RATE_PER_UNIT = 6;
        private static final double MINIMUM_BILL = 1000;

        FactoryConnection(double units) {
            super(units);
        }

        @Override
        String getTypeName() {
            return "FACTORY";
        }

        @Override
        double calculateBill() {
            return Math.max(getUnits() * RATE_PER_UNIT, MINIMUM_BILL);
        }
    }

    private static ElectricityConnection createConnection(String line) {
        String[] parts = line.trim().split("\\s+");
        if (parts.length != 2) {
            throw new IllegalArgumentException("Wrong number of fields: " + line);
        }
        double units = number(parts[1]);
        switch (parts[0].toUpperCase()) {
            case "HOME":
                return new HomeConnection(units);
            case "SHOP":
                return new ShopConnection(units);
            case "FACTORY":
                return new FactoryConnection(units);
            default:
                throw new IllegalArgumentException("Unknown connection type: " + parts[0]);
        }
    }

    private static double number(String text) {
        double value;
        try {
            value = Double.parseDouble(text);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Not a number: " + text);
        }
        if (Double.isNaN(value) || Double.isInfinite(value) || value < 0) {
            throw new IllegalArgumentException("Units must be a non-negative number: " + text);
        }
        return value;
    }

    private static String run(BufferedReader reader) throws IOException {
        String first = reader.readLine();
        if (first == null || first.trim().isEmpty()) {
            throw new IllegalArgumentException("Missing number of connections");
        }
        int count;
        try {
            count = Integer.parseInt(first.trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid number of connections: " + first.trim());
        }
        if (count < 1) {
            throw new IllegalArgumentException("Number of connections must be at least 1");
        }

        List<ElectricityConnection> connections = new ArrayList<>();
        while (connections.size() < count) {
            String line = reader.readLine();
            if (line == null) {
                throw new IllegalArgumentException(
                        "Expected " + count + " connections but found " + connections.size());
            }
            if (line.trim().isEmpty()) {
                continue;
            }
            connections.add(createConnection(line));
        }

        StringBuilder out = new StringBuilder();
        double total = 0;
        for (ElectricityConnection connection : connections) {
            double bill = connection.calculateBill();   // runtime polymorphism
            total += bill;
            out.append(String.format(Locale.US, "%s: %.2f\n", connection.getTypeName(), bill));
        }
        out.append(String.format(Locale.US, "Total: %.2f\n", total));
        return out.toString();
    }

    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        try {
            System.out.print(run(reader));
        } catch (IllegalArgumentException e) {
            System.err.println("Error: " + e.getMessage());
            System.exit(1);
        }
    }
}
