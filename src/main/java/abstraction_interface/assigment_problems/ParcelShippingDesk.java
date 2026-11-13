package abstraction_interface.assigment_problems;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Problem 2: Parcel Shipping Desk.
 * Every parcel answers "how much is shipping" (abstract Parcel class).
 * Only some parcels answer "can it be insured" (the Insurable interface).
 */
public class ParcelShippingDesk {

    /** Optional capability: parcels that can be insured. */
    interface Insurable {
        double INSURANCE_RATE = 0.02;

        double getDeclaredValue();

        default double calculateInsurance() {
            return getDeclaredValue() * INSURANCE_RATE;
        }
    }

    abstract static class Parcel {
        private final double weightKg;
        private final double declaredValue;

        Parcel(double weightKg, double declaredValue) {
            this.weightKg = weightKg;
            this.declaredValue = declaredValue;
        }

        double getWeightKg() {
            return weightKg;
        }

        public double getDeclaredValue() {
            return declaredValue;
        }

        abstract String getTypeName();

        abstract double calculateCharge();

        /** Insurance is zero unless the parcel has the Insurable capability. */
        double getInsurance() {
            if (this instanceof Insurable) {
                return ((Insurable) this).calculateInsurance();
            }
            return 0;
        }

        double getTotal() {
            return calculateCharge() + getInsurance();
        }

        /** Shared rule: standard shipping is 40 plus 10 per kg. */
        protected double standardCharge() {
            return 40 + 10 * weightKg;
        }
    }

    /** Standard parcels are NOT insurable. */
    static class StandardParcel extends Parcel {
        StandardParcel(double weightKg, double declaredValue) {
            super(weightKg, declaredValue);
        }

        @Override
        String getTypeName() {
            return "STANDARD";
        }

        @Override
        double calculateCharge() {
            return standardCharge();
        }
    }

    static class ExpressParcel extends Parcel implements Insurable {
        ExpressParcel(double weightKg, double declaredValue) {
            super(weightKg, declaredValue);
        }

        @Override
        String getTypeName() {
            return "EXPRESS";
        }

        @Override
        double calculateCharge() {
            return 80 + 15 * getWeightKg();
        }
    }

    static class FragileParcel extends Parcel implements Insurable {
        private static final double HANDLING_FEE = 50;

        FragileParcel(double weightKg, double declaredValue) {
            super(weightKg, declaredValue);
        }

        @Override
        String getTypeName() {
            return "FRAGILE";
        }

        @Override
        double calculateCharge() {
            return standardCharge() + HANDLING_FEE;
        }
    }

    private static Parcel createParcel(String line) {
        String[] parts = line.trim().split("\\s+");
        if (parts.length != 3) {
            throw new IllegalArgumentException("Wrong number of fields: " + line);
        }
        double weight = number(parts[1]);
        double value = number(parts[2]);
        switch (parts[0].toUpperCase()) {
            case "STANDARD":
                return new StandardParcel(weight, value);
            case "EXPRESS":
                return new ExpressParcel(weight, value);
            case "FRAGILE":
                return new FragileParcel(weight, value);
            default:
                throw new IllegalArgumentException("Unknown parcel type: " + parts[0]);
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
            throw new IllegalArgumentException("Value must be a non-negative number: " + text);
        }
        return value;
    }

    private static String run(BufferedReader reader) throws IOException {
        String first = reader.readLine();
        if (first == null || first.trim().isEmpty()) {
            throw new IllegalArgumentException("Missing number of parcels");
        }
        int count;
        try {
            count = Integer.parseInt(first.trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid number of parcels: " + first.trim());
        }
        if (count < 1) {
            throw new IllegalArgumentException("Number of parcels must be at least 1");
        }

        List<Parcel> parcels = new ArrayList<>();
        while (parcels.size() < count) {
            String line = reader.readLine();
            if (line == null) {
                throw new IllegalArgumentException("Expected " + count + " parcels but found " + parcels.size());
            }
            if (line.trim().isEmpty()) {
                continue;
            }
            parcels.add(createParcel(line));
        }

        StringBuilder out = new StringBuilder();
        double grandTotal = 0;
        for (Parcel parcel : parcels) {
            double total = parcel.getTotal();
            grandTotal += total;
            out.append(String.format(Locale.US, "%s: Charge=%.2f Insurance=%.2f Total=%.2f\n",
                    parcel.getTypeName(), parcel.calculateCharge(), parcel.getInsurance(), total));
        }
        out.append(String.format(Locale.US, "Grand Total: %.2f\n", grandTotal));
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
