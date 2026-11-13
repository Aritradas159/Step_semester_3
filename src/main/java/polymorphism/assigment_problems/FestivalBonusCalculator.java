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
 * Assignment Problem 4: The Festival Bonus Calculator.
 */
public class FestivalBonusCalculator {

    abstract static class Employee {
        protected final String name;
        protected final BigDecimal salary;

        Employee(String name, BigDecimal salary) {
            this.name = name;
            this.salary = salary;
        }

        abstract BigDecimal bonus();
    }

    static class FullTimeEmployee extends Employee {
        FullTimeEmployee(String n, BigDecimal s) { super(n, s); }
        @Override BigDecimal bonus() { return salary.multiply(new BigDecimal("0.10")); }
    }

    static class PartTimeEmployee extends Employee {
        PartTimeEmployee(String n, BigDecimal s) { super(n, s); }
        @Override BigDecimal bonus() { return salary.multiply(new BigDecimal("0.05")); }
    }

    static class Intern extends Employee {
        Intern(String n, BigDecimal s) { super(n, s); }
        @Override BigDecimal bonus() { return new BigDecimal("2000"); }
    }

    static Employee create(List<String> t) {
        if (t.size() != 3) throw new IllegalArgumentException("Expected: EmployeeType Name MonthlySalary");
        BigDecimal s = num(t.get(2));
        switch (t.get(0).toUpperCase(Locale.ROOT)) {
            case "FULLTIME": return new FullTimeEmployee(t.get(1), s);
            case "PARTTIME": return new PartTimeEmployee(t.get(1), s);
            case "INTERN": return new Intern(t.get(1), s);
            default: throw new IllegalArgumentException("Unknown employee type: " + t.get(0));
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
            List<Employee> items = new ArrayList<>();
            for (int i = 1; i <= n; i++) items.add(create(tokenize(lines.get(i))));

            StringBuilder out = new StringBuilder();
            BigDecimal total = BigDecimal.ZERO;
            for (Employee it : items) {                       // runtime polymorphism
                BigDecimal v = it.bonus().setScale(2, RoundingMode.HALF_UP);
                total = total.add(v);
                out.append(it.name).append(": ").append(fmt(v)).append('\n');
            }
            out.append("Total Bonus: ").append(fmt(total)).append('\n');
            System.out.print(out);
        } catch (RuntimeException e) {
            System.err.println("Error: " + e.getMessage());
            System.exit(1);
        }
    }
}
