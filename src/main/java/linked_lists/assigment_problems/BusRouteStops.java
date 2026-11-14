package linked_lists.assigment_problems;

import java.io.IOException;
import java.util.Scanner;

/**
 * STEP 12 - Coding Assignment Problem 1: Bus Route Stops
 *
 * Task:
 * A college bus route is stored as a chain of stops. Build the chain from the
 * stop names in order, print it, and print the number of stops with the first
 * and last stop.
 * Implement Node build(String[] stops) and void printRoute(Node head).
 * Write your own node class with data and next fields; do not use library linked lists.
 *
 * Complexity Analysis:
 * - Building the chain (build):
 *   Time Complexity: O(n), where n is the number of stops, because we append each stop
 *   in O(1) time using a tail reference.
 *   Space Complexity: O(n) to allocate n Node objects in the chain.
 * - Printing the chain (printRoute):
 *   Time Complexity: O(n) to traverse from head to null once.
 *   Space Complexity: O(1) auxiliary space.
 */
public class BusRouteStops {

    public static class Node {
        public String data;
        public Node next;

        public Node(String data) {
            this.data = data;
            this.next = null;
        }
    }

    /**
     * Builds a singly linked list of stops in the given order.
     */
    public static Node build(String[] stops) {
        if (stops == null || stops.length == 0) {
            return null;
        }
        Node head = null;
        Node tail = null;
        for (String stop : stops) {
            if (stop == null || stop.trim().isEmpty()) {
                throw new IllegalArgumentException("Stop name cannot be null or empty.");
            }
            Node newNode = new Node(stop.trim());
            if (head == null) {
                head = newNode;
                tail = newNode;
            } else {
                tail.next = newNode;
                tail = newNode;
            }
        }
        return head;
    }

    /**
     * Prints the route chain in the form "a -> b -> null", followed by
     * "Stops: count, First: first, Last: last".
     */
    public static void printRoute(Node head) {
        if (head == null) {
            System.out.println("null");
            System.out.println("Stops: 0, First: none, Last: none");
            return;
        }

        StringBuilder routeBuilder = new StringBuilder();
        String firstStop = head.data;
        String lastStop = head.data;
        int count = 0;

        Node current = head;
        while (current != null) {
            routeBuilder.append(current.data).append(" -> ");
            lastStop = current.data;
            count++;
            current = current.next;
        }
        routeBuilder.append("null");

        System.out.println(routeBuilder);
        System.out.println("Stops: " + count + ", First: " + firstStop + ", Last: " + lastStop);
    }

    public static void main(String[] args) throws IOException {
        if (System.in.available() > 0) {
            Scanner scanner = new Scanner(System.in);
            if (scanner.hasNextLine()) {
                String line = scanner.nextLine().trim();
                line = line.replaceAll("^stops\\s*=\\s*", "")
                           .replace("[", "")
                           .replace("]", "")
                           .trim();
                if (line.isEmpty()) {
                    printRoute(null);
                } else {
                    String[] stops = line.split("\\s*,\\s*|\\s+");
                    Node head = build(stops);
                    printRoute(head);
                }
            }
            return;
        }

        // Sample 1 from Question_1.pdf
        String[] sample1 = { "Tambaram", "Guindy", "TNagar", "Central" };
        Node route1 = build(sample1);
        printRoute(route1);

        // Sample 2 from Question_1.pdf
        String[] sample2 = { "Potheri" };
        Node route2 = build(sample2);
        printRoute(route2);
    }
}
