package abstraction_interface.class_problems;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Problem 5: Travel Booking with a Common Fee.
 * The booking fee lives in exactly one place: the abstract Booking class.
 * Subclasses only supply their own base fare.
 */
public class TravelBooking {

    abstract static class Booking {
        /** The one and only place where the booking fee is written. */
        private static final double BOOKING_FEE = 50;

        private final double distanceKm;

        Booking(double distanceKm) {
            this.distanceKm = distanceKm;
        }

        double getDistanceKm() {
            return distanceKm;
        }

        abstract String getModeName();

        /** Each travel mode calculates its own fare. */
        abstract double calculateBaseFare();

        /** Shared behaviour: fare plus the common booking fee. */
        final double calculateTotal() {
            return calculateBaseFare() + BOOKING_FEE;
        }
    }

    static class BusBooking extends Booking {
        private static final double RATE_PER_KM = 2;

        BusBooking(double distanceKm) {
            super(distanceKm);
        }

        @Override
        String getModeName() {
            return "BUS";
        }

        @Override
        double calculateBaseFare() {
            return getDistanceKm() * RATE_PER_KM;
        }
    }

    static class TrainBooking extends Booking {
        private static final double RATE_PER_KM = 1.5;

        TrainBooking(double distanceKm) {
            super(distanceKm);
        }

        @Override
        String getModeName() {
            return "TRAIN";
        }

        @Override
        double calculateBaseFare() {
            return getDistanceKm() * RATE_PER_KM;
        }
    }

    static class FlightBooking extends Booking {
        private static final double BASE_FARE = 2500;
        private static final double RATE_PER_KM = 4;

        FlightBooking(double distanceKm) {
            super(distanceKm);
        }

        @Override
        String getModeName() {
            return "FLIGHT";
        }

        @Override
        double calculateBaseFare() {
            return BASE_FARE + getDistanceKm() * RATE_PER_KM;
        }
    }

    private static Booking createBooking(String line) {
        String[] parts = line.trim().split("\\s+");
        if (parts.length != 2) {
            throw new IllegalArgumentException("Wrong number of fields: " + line);
        }
        double distance = number(parts[1]);
        switch (parts[0].toUpperCase()) {
            case "BUS":
                return new BusBooking(distance);
            case "TRAIN":
                return new TrainBooking(distance);
            case "FLIGHT":
                return new FlightBooking(distance);
            default:
                throw new IllegalArgumentException("Unknown travel mode: " + parts[0]);
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
            throw new IllegalArgumentException("Distance must be a non-negative number: " + text);
        }
        return value;
    }

    private static String run(BufferedReader reader) throws IOException {
        String first = reader.readLine();
        if (first == null || first.trim().isEmpty()) {
            throw new IllegalArgumentException("Missing number of bookings");
        }
        int count;
        try {
            count = Integer.parseInt(first.trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid number of bookings: " + first.trim());
        }
        if (count < 1) {
            throw new IllegalArgumentException("Number of bookings must be at least 1");
        }

        List<Booking> bookings = new ArrayList<>();
        while (bookings.size() < count) {
            String line = reader.readLine();
            if (line == null) {
                throw new IllegalArgumentException("Expected " + count + " bookings but found " + bookings.size());
            }
            if (line.trim().isEmpty()) {
                continue;
            }
            bookings.add(createBooking(line));
        }

        StringBuilder out = new StringBuilder();
        for (Booking booking : bookings) {
            out.append(String.format(Locale.US, "%s: %.2f\n", booking.getModeName(), booking.calculateTotal()));
        }
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
