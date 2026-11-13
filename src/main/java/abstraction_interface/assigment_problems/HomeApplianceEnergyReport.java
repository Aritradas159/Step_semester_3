package abstraction_interface.assigment_problems;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Problem 5: Home Appliance Energy Report.
 * Every appliance calculates units from its own power rating (abstract Appliance class).
 * Saver mode is an optional capability (SaverMode interface) of AC and Washer only.
 */
public class HomeApplianceEnergyReport {

    /** Optional capability: appliances with a saver mode. */
    interface SaverMode {
        double ENERGY_REDUCTION = 0.25;

        default double applySaverMode(double units) {
            return units - units * ENERGY_REDUCTION;
        }
    }

    abstract static class Appliance {
        private static final double COST_PER_UNIT = 8;

        abstract String getApplianceName();

        abstract double getPowerWatts();

        double calculateUnits(double hours) {
            return getPowerWatts() * hours / 1000;
        }

        double calculateCost(double units) {
            return units * COST_PER_UNIT;
        }
    }

    static class Fridge extends Appliance {
        @Override
        String getApplianceName() {
            return "FRIDGE";
        }

        @Override
        double getPowerWatts() {
            return 150;
        }
    }

    static class AirConditioner extends Appliance implements SaverMode {
        @Override
        String getApplianceName() {
            return "AC";
        }

        @Override
        double getPowerWatts() {
            return 1500;
        }
    }

    static class Television extends Appliance {
        @Override
        String getApplianceName() {
            return "TV";
        }

        @Override
        double getPowerWatts() {
            return 100;
        }
    }

    static class WashingMachine extends Appliance implements SaverMode {
        @Override
        String getApplianceName() {
            return "WASHER";
        }

        @Override
        double getPowerWatts() {
            return 500;
        }
    }

    private static class Usage {
        final Appliance appliance;
        final double hours;
        final boolean saverRequested;

        Usage(Appliance appliance, double hours, boolean saverRequested) {
            this.appliance = appliance;
            this.hours = hours;
            this.saverRequested = saverRequested;
        }
    }

    private static Usage createUsage(String line) {
        String[] parts = line.trim().split("\\s+");
        if (parts.length != 2 && parts.length != 3) {
            throw new IllegalArgumentException("Wrong number of fields: " + line);
        }
        Appliance appliance;
        switch (parts[0].toUpperCase()) {
            case "FRIDGE":
                appliance = new Fridge();
                break;
            case "AC":
                appliance = new AirConditioner();
                break;
            case "TV":
                appliance = new Television();
                break;
            case "WASHER":
                appliance = new WashingMachine();
                break;
            default:
                throw new IllegalArgumentException("Unknown appliance: " + parts[0]);
        }
        double hours = number(parts[1]);
        boolean saver = false;
        if (parts.length == 3) {
            if (!parts[2].equalsIgnoreCase("SAVER")) {
                throw new IllegalArgumentException("Unknown option (expected SAVER): " + parts[2]);
            }
            saver = true;
        }
        return new Usage(appliance, hours, saver);
    }

    private static double number(String text) {
        double value;
        try {
            value = Double.parseDouble(text);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Not a number: " + text);
        }
        if (Double.isNaN(value) || Double.isInfinite(value) || value < 0) {
            throw new IllegalArgumentException("Hours must be a non-negative number: " + text);
        }
        return value;
    }

    private static String run(BufferedReader reader) throws IOException {
        String first = reader.readLine();
        if (first == null || first.trim().isEmpty()) {
            throw new IllegalArgumentException("Missing number of appliances");
        }
        int count;
        try {
            count = Integer.parseInt(first.trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid number of appliances: " + first.trim());
        }
        if (count < 1) {
            throw new IllegalArgumentException("Number of appliances must be at least 1");
        }

        List<Usage> usages = new ArrayList<>();
        while (usages.size() < count) {
            String line = reader.readLine();
            if (line == null) {
                throw new IllegalArgumentException("Expected " + count + " appliances but found " + usages.size());
            }
            if (line.trim().isEmpty()) {
                continue;
            }
            usages.add(createUsage(line));
        }

        StringBuilder out = new StringBuilder();
        double totalCost = 0;
        for (Usage usage : usages) {
            Appliance appliance = usage.appliance;
            double units = appliance.calculateUnits(usage.hours);
            if (usage.saverRequested) {
                if (appliance instanceof SaverMode) {
                    units = ((SaverMode) appliance).applySaverMode(units);
                } else {
                    out.append(appliance.getApplianceName()).append(": saver mode not supported\n");
                    continue;
                }
            }
            double cost = appliance.calculateCost(units);
            totalCost += cost;
            out.append(String.format(Locale.US, "%s: Units=%.2f Cost=%.2f\n",
                    appliance.getApplianceName(), units, cost));
        }
        out.append(String.format(Locale.US, "Total Cost: %.2f\n", totalCost));
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
