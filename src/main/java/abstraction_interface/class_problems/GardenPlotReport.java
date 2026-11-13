package abstraction_interface.class_problems;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Problem 1: Garden Plot Area Report.
 * Plot is abstract: every plot has an owner, but each shape calculates its own area.
 */
public class GardenPlotReport {

    /** Common base type for all plots. A generic Plot can never be created. */
    abstract static class Plot {
        private final String owner;

        Plot(String owner) {
            this.owner = owner;
        }

        String getOwner() {
            return owner;
        }

        abstract String getShapeName();

        abstract double calculateArea();
    }

    static class CirclePlot extends Plot {
        private final double radius;

        CirclePlot(String owner, double radius) {
            super(owner);
            this.radius = radius;
        }

        @Override
        String getShapeName() {
            return "CIRCLE";
        }

        @Override
        double calculateArea() {
            return Math.PI * radius * radius;
        }
    }

    static class RectanglePlot extends Plot {
        private final double length;
        private final double width;

        RectanglePlot(String owner, double length, double width) {
            super(owner);
            this.length = length;
            this.width = width;
        }

        @Override
        String getShapeName() {
            return "RECTANGLE";
        }

        @Override
        double calculateArea() {
            return length * width;
        }
    }

    static class TrianglePlot extends Plot {
        private final double base;
        private final double height;

        TrianglePlot(String owner, double base, double height) {
            super(owner);
            this.base = base;
            this.height = height;
        }

        @Override
        String getShapeName() {
            return "TRIANGLE";
        }

        @Override
        double calculateArea() {
            return 0.5 * base * height;
        }
    }

    /** Builds a plot from one input line (the only place that looks at the shape keyword). */
    private static Plot createPlot(String line) {
        String[] parts = line.trim().split("\\s+");
        String shape = parts[0].toUpperCase();
        switch (shape) {
            case "CIRCLE":
                requireFields(parts, 3, line);
                return new CirclePlot(parts[1], number(parts[2]));
            case "RECTANGLE":
                requireFields(parts, 4, line);
                return new RectanglePlot(parts[1], number(parts[2]), number(parts[3]));
            case "TRIANGLE":
                requireFields(parts, 4, line);
                return new TrianglePlot(parts[1], number(parts[2]), number(parts[3]));
            default:
                throw new IllegalArgumentException("Unknown plot shape: " + parts[0]);
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
            throw new IllegalArgumentException("Measurement must be a non-negative number: " + text);
        }
        return value;
    }

    private static String run(BufferedReader reader) throws IOException {
        String first = reader.readLine();
        if (first == null || first.trim().isEmpty()) {
            throw new IllegalArgumentException("Missing number of plots");
        }
        int count;
        try {
            count = Integer.parseInt(first.trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid number of plots: " + first.trim());
        }
        if (count < 1) {
            throw new IllegalArgumentException("Number of plots must be at least 1");
        }

        List<Plot> plots = new ArrayList<>();
        while (plots.size() < count) {
            String line = reader.readLine();
            if (line == null) {
                throw new IllegalArgumentException("Expected " + count + " plots but found " + plots.size());
            }
            if (line.trim().isEmpty()) {
                continue;
            }
            plots.add(createPlot(line));
        }

        StringBuilder out = new StringBuilder();
        double total = 0;
        for (Plot plot : plots) {
            double area = plot.calculateArea();   // runtime polymorphism
            total += area;
            out.append(String.format(Locale.US, "%s (%s): %.2f\n", plot.getOwner(), plot.getShapeName(), area));
        }
        out.append(String.format(Locale.US, "Total Area: %.2f\n", total));
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
