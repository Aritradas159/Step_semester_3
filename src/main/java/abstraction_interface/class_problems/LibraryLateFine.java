package abstraction_interface.class_problems;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Problem 3: Library Late Fine Counter.
 * LibraryItem is abstract: each item type supplies its own fine rule.
 */
public class LibraryLateFine {

    abstract static class LibraryItem {
        private final String title;
        private final int daysLate;

        LibraryItem(String title, int daysLate) {
            this.title = title;
            this.daysLate = daysLate;
        }

        String getTitle() {
            return title;
        }

        int getDaysLate() {
            return daysLate;
        }

        abstract double calculateFine();
    }

    static class Book extends LibraryItem {
        private static final double FINE_PER_DAY = 2;

        Book(String title, int daysLate) {
            super(title, daysLate);
        }

        @Override
        double calculateFine() {
            return getDaysLate() * FINE_PER_DAY;
        }
    }

    static class Dvd extends LibraryItem {
        private static final double FINE_PER_DAY = 5;
        private static final double MAXIMUM_FINE = 50;

        Dvd(String title, int daysLate) {
            super(title, daysLate);
        }

        @Override
        double calculateFine() {
            return Math.min(getDaysLate() * FINE_PER_DAY, MAXIMUM_FINE);
        }
    }

    static class Magazine extends LibraryItem {
        private static final double FINE_PER_DAY = 1;

        Magazine(String title, int daysLate) {
            super(title, daysLate);
        }

        @Override
        double calculateFine() {
            return getDaysLate() * FINE_PER_DAY;
        }
    }

    private static LibraryItem createItem(String line) {
        String[] parts = line.trim().split("\\s+");
        if (parts.length != 3) {
            throw new IllegalArgumentException("Wrong number of fields: " + line);
        }
        int daysLate = wholeNumber(parts[2]);
        switch (parts[0].toUpperCase()) {
            case "BOOK":
                return new Book(parts[1], daysLate);
            case "DVD":
                return new Dvd(parts[1], daysLate);
            case "MAGAZINE":
                return new Magazine(parts[1], daysLate);
            default:
                throw new IllegalArgumentException("Unknown item type: " + parts[0]);
        }
    }

    private static int wholeNumber(String text) {
        int value;
        try {
            value = Integer.parseInt(text);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Not a whole number: " + text);
        }
        if (value < 0) {
            throw new IllegalArgumentException("Days late cannot be negative: " + text);
        }
        return value;
    }

    private static String run(BufferedReader reader) throws IOException {
        String first = reader.readLine();
        if (first == null || first.trim().isEmpty()) {
            throw new IllegalArgumentException("Missing number of items");
        }
        int count;
        try {
            count = Integer.parseInt(first.trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid number of items: " + first.trim());
        }
        if (count < 1) {
            throw new IllegalArgumentException("Number of items must be at least 1");
        }

        List<LibraryItem> items = new ArrayList<>();
        while (items.size() < count) {
            String line = reader.readLine();
            if (line == null) {
                throw new IllegalArgumentException("Expected " + count + " items but found " + items.size());
            }
            if (line.trim().isEmpty()) {
                continue;
            }
            items.add(createItem(line));
        }

        StringBuilder out = new StringBuilder();
        double total = 0;
        for (LibraryItem item : items) {
            double fine = item.calculateFine();   // runtime polymorphism
            total += fine;
            out.append(String.format(Locale.US, "%s: %.2f\n", item.getTitle(), fine));
        }
        out.append(String.format(Locale.US, "Total Fines: %.2f\n", total));
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
