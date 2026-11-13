package abstraction_interface.class_problems;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Problem 2: Weekly Staff Pay.
 * Staff is abstract, so a generic staff member with no pay rule can never be created.
 */
public class WeeklyStaffPay {

    abstract static class Staff {
        private final String name;

        Staff(String name) {
            this.name = name;
        }

        String getName() {
            return name;
        }

        abstract double calculatePay();
    }

    static class FullTimeStaff extends Staff {
        private final double weeklySalary;

        FullTimeStaff(String name, double weeklySalary) {
            super(name);
            this.weeklySalary = weeklySalary;
        }

        @Override
        double calculatePay() {
            return weeklySalary;
        }
    }

    static class HourlyStaff extends Staff {
        private static final double REGULAR_HOURS = 40;
        private static final double OVERTIME_MULTIPLIER = 1.5;
        private final double hours;
        private final double rate;

        HourlyStaff(String name, double hours, double rate) {
            super(name);
            this.hours = hours;
            this.rate = rate;
        }

        @Override
        double calculatePay() {
            double regularHours = Math.min(hours, REGULAR_HOURS);
            double overtimeHours = Math.max(hours - REGULAR_HOURS, 0);
            return regularHours * rate + overtimeHours * rate * OVERTIME_MULTIPLIER;
        }
    }

    static class InternStaff extends Staff {
        private final double stipend;

        InternStaff(String name, double stipend) {
            super(name);
            this.stipend = stipend;
        }

        @Override
        double calculatePay() {
            return stipend;
        }
    }

    private static Staff createStaff(String line) {
        String[] parts = line.trim().split("\\s+");
        String type = parts[0].toUpperCase();
        switch (type) {
            case "FULLTIME":
                requireFields(parts, 3, line);
                return new FullTimeStaff(parts[1], number(parts[2]));
            case "HOURLY":
                requireFields(parts, 4, line);
                return new HourlyStaff(parts[1], number(parts[2]), number(parts[3]));
            case "INTERN":
                requireFields(parts, 3, line);
                return new InternStaff(parts[1], number(parts[2]));
            default:
                throw new IllegalArgumentException("Unknown staff type: " + parts[0]);
        }
    }

    private static void requireFields(String[] parts, int expected, String line) {
        if (parts.length != expected) {
            throw new IllegalArgumentException("Wrong number of fields: " + line);
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
            throw new IllegalArgumentException("Missing number of staff");
        }
        int count;
        try {
            count = Integer.parseInt(first.trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid number of staff: " + first.trim());
        }
        if (count < 1) {
            throw new IllegalArgumentException("Number of staff must be at least 1");
        }

        List<Staff> staffList = new ArrayList<>();
        while (staffList.size() < count) {
            String line = reader.readLine();
            if (line == null) {
                throw new IllegalArgumentException("Expected " + count + " staff but found " + staffList.size());
            }
            if (line.trim().isEmpty()) {
                continue;
            }
            staffList.add(createStaff(line));
        }

        StringBuilder out = new StringBuilder();
        double total = 0;
        for (Staff staff : staffList) {
            double pay = staff.calculatePay();   // runtime polymorphism
            total += pay;
            out.append(String.format(Locale.US, "%s: %.2f\n", staff.getName(), pay));
        }
        out.append(String.format(Locale.US, "Total Payroll: %.2f\n", total));
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
