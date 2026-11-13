package abstraction_interface.assigment_problems;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Problem 3: College Fee Counter.
 * Using the college bus is an optional capability (the BusUser interface).
 * The counter asks "is this student a BusUser?" instead of checking the student type by name.
 */
public class CollegeFeeCounter {

    /** Optional capability: students who use the college bus. */
    interface BusUser {
        /** The one and only place where the transport fee is written. */
        double TRANSPORT_FEE = 12000;

        default double getTransportFee() {
            return TRANSPORT_FEE;
        }
    }

    abstract static class Student {
        /** Normal yearly tuition, shared by all student types. */
        protected static final double NORMAL_TUITION = 40000;

        private final String name;

        Student(String name) {
            this.name = name;
        }

        String getName() {
            return name;
        }

        abstract double calculateTuition();

        /** Tuition plus transport fee for students who use the bus. */
        final double calculateTotalFee() {
            double fee = calculateTuition();
            if (this instanceof BusUser) {
                fee += ((BusUser) this).getTransportFee();
            }
            return fee;
        }
    }

    static class DayScholar extends Student implements BusUser {
        DayScholar(String name) {
            super(name);
        }

        @Override
        double calculateTuition() {
            return NORMAL_TUITION;
        }
    }

    /** Hostellers live on campus, so they do not implement BusUser. */
    static class Hosteller extends Student {
        private static final double HOSTEL_FEE = 60000;

        Hosteller(String name) {
            super(name);
        }

        @Override
        double calculateTuition() {
            return NORMAL_TUITION + HOSTEL_FEE;
        }
    }

    static class ScholarshipStudent extends Student implements BusUser {
        ScholarshipStudent(String name) {
            super(name);
        }

        @Override
        double calculateTuition() {
            return NORMAL_TUITION / 2;
        }
    }

    private static Student createStudent(String line) {
        String[] parts = line.trim().split("\\s+");
        if (parts.length != 2) {
            throw new IllegalArgumentException("Wrong number of fields: " + line);
        }
        switch (parts[0].toUpperCase()) {
            case "DAY_SCHOLAR":
                return new DayScholar(parts[1]);
            case "HOSTELLER":
                return new Hosteller(parts[1]);
            case "SCHOLAR":
                return new ScholarshipStudent(parts[1]);
            default:
                throw new IllegalArgumentException("Unknown student type: " + parts[0]);
        }
    }

    private static String run(BufferedReader reader) throws IOException {
        String first = reader.readLine();
        if (first == null || first.trim().isEmpty()) {
            throw new IllegalArgumentException("Missing number of students");
        }
        int count;
        try {
            count = Integer.parseInt(first.trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid number of students: " + first.trim());
        }
        if (count < 1) {
            throw new IllegalArgumentException("Number of students must be at least 1");
        }

        List<Student> students = new ArrayList<>();
        while (students.size() < count) {
            String line = reader.readLine();
            if (line == null) {
                throw new IllegalArgumentException("Expected " + count + " students but found " + students.size());
            }
            if (line.trim().isEmpty()) {
                continue;
            }
            students.add(createStudent(line));
        }

        StringBuilder out = new StringBuilder();
        double total = 0;
        for (Student student : students) {
            double fee = student.calculateTotalFee();
            total += fee;
            out.append(String.format(Locale.US, "%s: %.2f\n", student.getName(), fee));
        }
        out.append(String.format(Locale.US, "Total Collected: %.2f\n", total));
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
