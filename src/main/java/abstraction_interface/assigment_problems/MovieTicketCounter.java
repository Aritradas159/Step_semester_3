package abstraction_interface.assigment_problems;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Problem 1: Movie Ticket Counter.
 * The convenience fee is written once, in the abstract Ticket class, and applied per ticket.
 * A plain "ticket" with no seat type cannot be created because Ticket is abstract.
 */
public class MovieTicketCounter {

    abstract static class Ticket {
        /** The one and only place where the convenience fee is written. */
        private static final double CONVENIENCE_FEE = 20;

        private final int count;

        Ticket(int count) {
            this.count = count;
        }

        abstract String getSeatName();

        /** Each seat type decides its own ticket price. */
        abstract double getTicketPrice();

        /** Shared behaviour: (price + fee) for every ticket. */
        final double calculateAmount() {
            return count * (getTicketPrice() + CONVENIENCE_FEE);
        }
    }

    static class RegularTicket extends Ticket {
        RegularTicket(int count) {
            super(count);
        }

        @Override
        String getSeatName() {
            return "REGULAR";
        }

        @Override
        double getTicketPrice() {
            return 150;
        }
    }

    static class PremiumTicket extends Ticket {
        PremiumTicket(int count) {
            super(count);
        }

        @Override
        String getSeatName() {
            return "PREMIUM";
        }

        @Override
        double getTicketPrice() {
            return 250;
        }
    }

    static class ReclinerTicket extends Ticket {
        ReclinerTicket(int count) {
            super(count);
        }

        @Override
        String getSeatName() {
            return "RECLINER";
        }

        @Override
        double getTicketPrice() {
            return 400;
        }
    }

    private static Ticket createTicket(String line) {
        String[] parts = line.trim().split("\\s+");
        if (parts.length != 2) {
            throw new IllegalArgumentException("Wrong number of fields: " + line);
        }
        int count = wholeNumber(parts[1]);
        switch (parts[0].toUpperCase()) {
            case "REGULAR":
                return new RegularTicket(count);
            case "PREMIUM":
                return new PremiumTicket(count);
            case "RECLINER":
                return new ReclinerTicket(count);
            default:
                throw new IllegalArgumentException("Unknown seat type: " + parts[0]);
        }
    }

    private static int wholeNumber(String text) {
        int value;
        try {
            value = Integer.parseInt(text);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Ticket count must be a whole number: " + text);
        }
        if (value < 0) {
            throw new IllegalArgumentException("Ticket count cannot be negative: " + text);
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

        List<Ticket> bookings = new ArrayList<>();
        while (bookings.size() < count) {
            String line = reader.readLine();
            if (line == null) {
                throw new IllegalArgumentException("Expected " + count + " bookings but found " + bookings.size());
            }
            if (line.trim().isEmpty()) {
                continue;
            }
            bookings.add(createTicket(line));
        }

        StringBuilder out = new StringBuilder();
        double total = 0;
        for (Ticket booking : bookings) {
            double amount = booking.calculateAmount();
            total += amount;
            out.append(String.format(Locale.US, "%s: %.2f\n", booking.getSeatName(), amount));
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
