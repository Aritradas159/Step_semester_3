package polymorphism.class_problems;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Class (Practice) Problem 2: Library Item Due Date Calculator.
 */
public class LibraryDueDateCalculator {

    static final LocalDate TODAY = LocalDate.of(2023, 10, 26);

    abstract static class LibraryItem {
        protected final String title;

        LibraryItem(String title) { this.title = title; }

        abstract int borrowDays();

        LocalDate dueDate() { return TODAY.plusDays(borrowDays()); }
    }

    static class Book extends LibraryItem {
        Book(String t) { super(t); }
        @Override int borrowDays() { return 14; }
    }

    static class Dvd extends LibraryItem {
        Dvd(String t) { super(t); }
        @Override int borrowDays() { return 7; }
    }

    static class Magazine extends LibraryItem {
        Magazine(String t) { super(t); }
        @Override int borrowDays() { return 3; }
    }

    static LibraryItem create(List<String> t) {
        if (t.size() != 2) throw new IllegalArgumentException("Expected: ItemType \"ItemTitle\"");
        switch (t.get(0).toUpperCase(Locale.ROOT)) {
            case "BOOK": return new Book(t.get(1));
            case "DVD": return new Dvd(t.get(1));
            case "MAGAZINE": return new Magazine(t.get(1));
            default: throw new IllegalArgumentException("Unknown item type: " + t.get(0));
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
            List<LibraryItem> items = new ArrayList<>();
            for (int i = 1; i <= n; i++) items.add(create(tokenize(lines.get(i))));

            StringBuilder out = new StringBuilder();
            for (LibraryItem it : items) {                   // runtime polymorphism
                out.append(it.title).append(": ").append(it.dueDate()).append('\n');
            }
            System.out.print(out);
        } catch (RuntimeException e) {
            System.err.println("Error: " + e.getMessage());
            System.exit(1);
        }
    }
}
