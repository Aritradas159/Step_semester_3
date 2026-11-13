package abstraction_interface.assigment_problems;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Problem 4: City Cab Fare Meter.
 * The minimum fare is shared (abstract Cab class); night service is an optional
 * capability (NightService interface) that Mini does not have.
 */
public class CityCabFareMeter {

    /** Optional capability: cabs that offer night service. */
    interface NightService {
        double NIGHT_SURCHARGE_RATE = 0.20;

        default double applyNightSurcharge(double fare) {
            return fare + fare * NIGHT_SURCHARGE_RATE;
        }
    }

    abstract static class Cab {
        /** The minimum-fare rule, written once for every cab. */
        private static final double MINIMUM_FARE = 100;

        abstract String getCabName();

        abstract double getRatePerKm();

        /** Day fare: distance x rate, but never less than the minimum. */
        double calculateFare(double km) {
            return Math.max(km * getRatePerKm(), MINIMUM_FARE);
        }
    }

    static class MiniCab extends Cab {
        @Override
        String getCabName() {
            return "MINI";
        }

        @Override
        double getRatePerKm() {
            return 10;
        }
    }

    static class SedanCab extends Cab implements NightService {
        @Override
        String getCabName() {
            return "SEDAN";
        }

        @Override
        double getRatePerKm() {
            return 14;
        }
    }

    static class SuvCab extends Cab implements NightService {
        @Override
        String getCabName() {
            return "SUV";
        }

        @Override
        double getRatePerKm() {
            return 18;
        }
    }

    private static class Trip {
        final Cab cab;
        final double km;
        final boolean night;

        Trip(Cab cab, double km, boolean night) {
            this.cab = cab;
            this.km = km;
            this.night = night;
        }
    }

    private static Trip createTrip(String line) {
        String[] parts = line.trim().split("\\s+");
        if (parts.length != 3) {
            throw new IllegalArgumentException("Wrong number of fields: " + line);
        }
        Cab cab;
        switch (parts[0].toUpperCase()) {
            case "MINI":
                cab = new MiniCab();
                break;
            case "SEDAN":
                cab = new SedanCab();
                break;
            case "SUV":
                cab = new SuvCab();
                break;
            default:
                throw new IllegalArgumentException("Unknown cab type: " + parts[0]);
        }
        double km = number(parts[1]);
        boolean night;
        if (parts[2].equalsIgnoreCase("NIGHT")) {
            night = true;
        } else if (parts[2].equalsIgnoreCase("DAY")) {
            night = false;
        } else {
            throw new IllegalArgumentException("Time must be DAY or NIGHT: " + parts[2]);
        }
        return new Trip(cab, km, night);
    }

    private static double number(String text) {
        double value;
        try {
            value = Double.parseDouble(text);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Not a number: " + text);
        }
        if (Double.isNaN(value) || Double.isInfinite(value) || value < 0) {
            throw new IllegalArgumentException("Distance must be a non-negative number: " + text);
        }
        return value;
    }

    private static String run(BufferedReader reader) throws IOException {
        String first = reader.readLine();
        if (first == null || first.trim().isEmpty()) {
            throw new IllegalArgumentException("Missing number of trips");
        }
        int count;
        try {
            count = Integer.parseInt(first.trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid number of trips: " + first.trim());
        }
        if (count < 1) {
            throw new IllegalArgumentException("Number of trips must be at least 1");
        }

        List<Trip> trips = new ArrayList<>();
        while (trips.size() < count) {
            String line = reader.readLine();
            if (line == null) {
                throw new IllegalArgumentException("Expected " + count + " trips but found " + trips.size());
            }
            if (line.trim().isEmpty()) {
                continue;
            }
            trips.add(createTrip(line));
        }

        StringBuilder out = new StringBuilder();
        double total = 0;
        for (Trip trip : trips) {
            double fare = trip.cab.calculateFare(trip.km);   // minimum applied first
            if (trip.night) {
                if (trip.cab instanceof NightService) {
                    fare = ((NightService) trip.cab).applyNightSurcharge(fare);
                } else {
                    out.append(trip.cab.getCabName()).append(": night service not available\n");
                    continue;   // rejected trips are not added to the total
                }
            }
            total += fare;
            out.append(String.format(Locale.US, "%s: %.2f\n", trip.cab.getCabName(), fare));
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
