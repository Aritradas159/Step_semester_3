package linked_lists.class_problems;

import java.io.IOException;
import java.util.Scanner;

/**
 * STEP 12 - Category C Practice Problem 1: Campus Staff Salary
 *
 * Task:
 * The college pays every staff member a base salary. Teachers get a bonus of 5000
 * and lab assistants a bonus of 2000; other staff get no bonus.
 * Create a Staff class with name and baseSalary, and Teacher and LabAssistant classes
 * that extend it and override the bonus. Store everyone in one Staff[] array,
 * print each person's details in a single loop, then the grand total.
 *
 * Complexity Analysis:
 * - Time Complexity: O(n), where n is the number of staff members. We iterate through
 *   the Staff[] array once in a single loop, and each polymorphic call to getBonus()
 *   and getTotalSalary() runs in O(1) time.
 * - Space Complexity: O(n) to store the n Staff objects in the Staff[] array.
 */
public class CampusStaffSalary {

    public static class Staff {
        protected String name;
        protected double baseSalary;

        public Staff(String name, double baseSalary) {
            if (name == null || name.trim().isEmpty()) {
                throw new IllegalArgumentException("Staff name cannot be null or empty.");
            }
            if (baseSalary < 0 || baseSalary > 1_000_000) {
                throw new IllegalArgumentException("Base salary must be between 0 and 1000000.");
            }
            this.name = name.trim();
            this.baseSalary = baseSalary;
        }

        public String getName() {
            return name;
        }

        public double getBaseSalary() {
            return baseSalary;
        }

        public double getBonus() {
            return 0.0;
        }

        public double getTotalSalary() {
            return baseSalary + getBonus();
        }

        public String getRole() {
            return "Staff";
        }

        public String formatDetails() {
            return name + " (" + getRole() + "): base " + baseSalary
                    + ", bonus " + getBonus()
                    + ", total " + getTotalSalary();
        }
    }

    public static class Teacher extends Staff {
        private static final double TEACHER_BONUS = 5000.0;

        public Teacher(String name, double baseSalary) {
            super(name, baseSalary);
        }

        @Override
        public double getBonus() {
            return TEACHER_BONUS;
        }

        @Override
        public String getRole() {
            return "Teacher";
        }
    }

    public static class LabAssistant extends Staff {
        private static final double LAB_ASSISTANT_BONUS = 2000.0;

        public LabAssistant(String name, double baseSalary) {
            super(name, baseSalary);
        }

        @Override
        public double getBonus() {
            return LAB_ASSISTANT_BONUS;
        }

        @Override
        public String getRole() {
            return "LabAssistant";
        }
    }

    /**
     * Factory method to create the appropriate Staff object based on role type.
     */
    public static Staff createStaff(String type, String name, double baseSalary) {
        if (type == null) {
            throw new IllegalArgumentException("Staff type cannot be null.");
        }
        switch (type.trim()) {
            case "Teacher":
                return new Teacher(name, baseSalary);
            case "LabAssistant":
                return new LabAssistant(name, baseSalary);
            case "Staff":
                return new Staff(name, baseSalary);
            default:
                throw new IllegalArgumentException("Unknown staff type: " + type);
        }
    }

    /**
     * Prints each staff member's salary breakdown in a single loop and returns the grand total.
     */
    public static double printAllSalaries(Staff[] staffList) {
        if (staffList == null || staffList.length == 0) {
            System.out.println("Grand Total Salary: 0.0");
            return 0.0;
        }
        double grandTotal = 0.0;
        for (Staff person : staffList) {
            if (person != null) {
                System.out.println(person.formatDetails());
                grandTotal += person.getTotalSalary();
            }
        }
        System.out.println("Grand Total Salary: " + grandTotal);
        return grandTotal;
    }

    /**
     * Reads input from a Scanner and processes the staff salaries.
     */
    public static void processFromScanner(Scanner scanner) {
        if (!scanner.hasNextInt()) {
            return;
        }
        int n = scanner.nextInt();
        if (n < 1 || n > 100) {
            System.out.println("Invalid number of staff: must be between 1 and 100.");
            return;
        }
        Staff[] staffArray = new Staff[n];
        for (int i = 0; i < n; i++) {
            String type = scanner.next();
            String name = scanner.next();
            double baseSalary = scanner.nextDouble();
            staffArray[i] = createStaff(type, name, baseSalary);
        }
        printAllSalaries(staffArray);
    }

    public static void main(String[] args) throws IOException {
        if (System.in.available() > 0) {
            Scanner scanner = new Scanner(System.in);
            processFromScanner(scanner);
            return;
        }

        // Default execution with the exact PDF sample input
        Staff[] sampleStaff = new Staff[] {
            new Teacher("Rahul", 30000),
            new LabAssistant("Priya", 20000),
            new Staff("Amit", 25000),
            new Teacher("Sneha", 35000),
            new LabAssistant("Karan", 18000)
        };

        printAllSalaries(sampleStaff);
    }
}
