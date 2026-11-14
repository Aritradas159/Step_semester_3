package linked_lists.class_problems;

import java.io.IOException;
import java.util.Locale;
import java.util.Scanner;

/**
 * STEP 12 - Category C Practice Problem 2: Class Report Card
 *
 * Task:
 * A class teacher wants report cards: total, average and grade for each student,
 * listed from the highest total to the lowest. Marks outside 0 to 100 are typing
 * mistakes and that student is left out.
 * Write a Student class with private name and three marks, set only through
 * boolean setMarks(int a, int b, int c) which accepts marks from 0 to 100.
 * Grade by average: 80 and above A, 70 B, 60 C, 40 D, below 40 F.
 * Give Student a readable text form. Sort accepted students with bubble sort
 * that stops early when a pass makes no swap, and print the number of passes.
 *
 * Complexity Analysis:
 * - Best-Case Time Complexity of Sort: O(n) when the accepted students are already
 *   sorted in descending order of total marks (stops after 1 pass with no swaps).
 * - Worst-Case Time Complexity of Sort: O(n^2) when the accepted students are in
 *   reverse (ascending) order.
 * - Space Complexity: O(n) to store the accepted Student objects, and O(1) auxiliary
 *   space for the in-place bubble sort.
 */
public class ClassReportCard {

    public static class Student {
        private String name;
        private int mark1;
        private int mark2;
        private int mark3;

        public Student(String name) {
            if (name == null || name.trim().isEmpty()) {
                throw new IllegalArgumentException("Student name cannot be null or empty.");
            }
            this.name = name.trim();
        }

        public boolean setMarks(int a, int b, int c) {
            if (a < 0 || a > 100 || b < 0 || b > 100 || c < 0 || c > 100) {
                return false;
            }
            this.mark1 = a;
            this.mark2 = b;
            this.mark3 = c;
            return true;
        }

        public String getName() {
            return name;
        }

        public int getTotal() {
            return mark1 + mark2 + mark3;
        }

        public double getAverage() {
            return getTotal() / 3.0;
        }

        public char getGrade() {
            double avg = getAverage();
            if (avg >= 80.0) {
                return 'A';
            } else if (avg >= 70.0) {
                return 'B';
            } else if (avg >= 60.0) {
                return 'C';
            } else if (avg >= 40.0) {
                return 'D';
            } else {
                return 'F';
            }
        }

        @Override
        public String toString() {
            return String.format(Locale.US, "%s total=%d avg=%.2f grade=%c",
                    name, getTotal(), getAverage(), getGrade());
        }
    }

    /**
     * Sorts the first {@code count} elements of {@code students} in descending order
     * of total marks using early-stopping bubble sort.
     *
     * @return the number of passes executed
     */
    public static int bubbleSortDescending(Student[] students, int count) {
        if (students == null || count <= 1) {
            return 0;
        }
        int passes = 0;
        for (int i = 0; i < count - 1; i++) {
            passes++;
            boolean swapped = false;
            for (int j = 0; j < count - 1 - i; j++) {
                if (students[j].getTotal() < students[j + 1].getTotal()) {
                    Student temp = students[j];
                    students[j] = students[j + 1];
                    students[j + 1] = temp;
                    swapped = true;
                }
            }
            if (!swapped) {
                break;
            }
        }
        return passes;
    }

    /**
     * Processes student records, prints skipped students, sorts accepted students,
     * and prints the ranked report card and pass count.
     */
    public static void generateReportCard(String[] names, int[][] marks) {
        if (names == null || marks == null || names.length != marks.length) {
            throw new IllegalArgumentException("Names and marks arrays must be non-null and of equal length.");
        }
        int n = names.length;
        Student[] accepted = new Student[n];
        int acceptedCount = 0;

        for (int i = 0; i < n; i++) {
            Student student = new Student(names[i]);
            if (student.setMarks(marks[i][0], marks[i][1], marks[i][2])) {
                accepted[acceptedCount++] = student;
            } else {
                System.out.println(student.getName() + " skipped: marks must be 0 to 100");
            }
        }

        int passes = bubbleSortDescending(accepted, acceptedCount);
        for (int i = 0; i < acceptedCount; i++) {
            System.out.println((i + 1) + ". " + accepted[i]);
        }
        System.out.println("Sorted in " + passes + " passes");
    }

    public static void processFromScanner(Scanner scanner) {
        if (!scanner.hasNextInt()) {
            return;
        }
        int n = scanner.nextInt();
        if (n < 1 || n > 50) {
            System.out.println("Invalid number of students: must be between 1 and 50.");
            return;
        }
        String[] names = new String[n];
        int[][] marks = new int[n][3];
        for (int i = 0; i < n; i++) {
            names[i] = scanner.next();
            marks[i][0] = scanner.nextInt();
            marks[i][1] = scanner.nextInt();
            marks[i][2] = scanner.nextInt();
        }
        generateReportCard(names, marks);
    }

    public static void main(String[] args) throws IOException {
        if (System.in.available() > 0) {
            Scanner scanner = new Scanner(System.in);
            processFromScanner(scanner);
            return;
        }

        // Default execution with the exact PDF sample input
        String[] sampleNames = { "Asha", "Ravi", "Neha", "Kiran", "Pooja" };
        int[][] sampleMarks = {
            { 80, 90, 70 },
            { 60, 65, 70 },
            { 95, 85, 105 },
            { 45, 50, 40 },
            { 88, 72, 81 }
        };

        generateReportCard(sampleNames, sampleMarks);
    }
}
