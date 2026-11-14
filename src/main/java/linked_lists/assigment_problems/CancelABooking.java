package linked_lists.assigment_problems;

import java.io.IOException;
import java.util.Scanner;

/**
 * STEP 12 - Coding Assignment Problem 4: Cancel a Booking
 *
 * Task:
 * Room bookings are kept as a chain of booking numbers. When a booking is cancelled,
 * remove its first appearance from the chain. If the booking is not in the chain,
 * print a message and leave the chain unchanged.
 * Implement Node removeFirst(Node head, String booking) and return the (possibly new) head.
 * Write your own node class with data and next fields; do not use library linked lists.
 *
 * Complexity Analysis:
 * - Time Complexity when the booking is at the head: O(1), since head.data matches
 *   immediately and we simply return head.next.
 * - Time Complexity when the booking is at the end (or not found): O(n), where n is
 *   the number of nodes in the chain, as we traverse up to the last node.
 * - Space Complexity: O(1) auxiliary space.
 */
public class CancelABooking {

    public static class Node {
        public String data;
        public Node next;

        public Node(String data) {
            this.data = data;
            this.next = null;
        }
    }

    /**
     * Removes the first occurrence of {@code booking} from the chain and returns the
     * (possibly updated) head. If not found, prints "<booking> not found" and returns
     * the unchanged head.
     */
    public static Node removeFirst(Node head, String booking) {
        if (booking == null || booking.trim().isEmpty()) {
            throw new IllegalArgumentException("Booking number cannot be null or empty.");
        }
        String target = booking.trim();

        if (head == null) {
            System.out.println(target + " not found");
            return null;
        }

        if (target.equals(head.data)) {
            return head.next;
        }

        Node current = head;
        while (current.next != null) {
            if (target.equals(current.next.data)) {
                current.next = current.next.next;
                return head;
            }
            current = current.next;
        }

        System.out.println(target + " not found");
        return head;
    }

    /**
     * Builds a chain of bookings from an array of booking numbers.
     */
    public static Node buildBookings(String[] bookings) {
        if (bookings == null || bookings.length == 0) {
            return null;
        }
        Node head = new Node(bookings[0].trim());
        Node tail = head;
        for (int i = 1; i < bookings.length; i++) {
            tail.next = new Node(bookings[i].trim());
            tail = tail.next;
        }
        return head;
    }

    /**
     * Formats the chain in the form "a -> b -> null".
     */
    public static String formatChain(Node head) {
        StringBuilder sb = new StringBuilder();
        Node current = head;
        while (current != null) {
            sb.append(current.data).append(" -> ");
            current = current.next;
        }
        sb.append("null");
        return sb.toString();
    }

    public static void printChain(Node head) {
        System.out.println(formatChain(head));
    }

    public static void main(String[] args) throws IOException {
        if (System.in.available() > 0) {
            Scanner scanner = new Scanner(System.in);
            if (scanner.hasNextLine()) {
                String line = scanner.nextLine().trim();
                String[] parts = line.split(",\\s*cancel\\s*=\\s*");
                if (parts.length == 2) {
                    String chainPart = parts[0].replaceAll("^bookings\\s*=\\s*", "").trim();
                    String cancelTarget = parts[1].trim();
                    Node head = null;
                    if (!chainPart.isEmpty() && !chainPart.equalsIgnoreCase("null") && !chainPart.equalsIgnoreCase("(empty)")) {
                        String[] items = chainPart.split("\\s*->\\s*");
                        head = buildBookings(items);
                    }
                    head = removeFirst(head, cancelTarget);
                    printChain(head);
                }
            }
            return;
        }

        // Sample 1 from Question_4.pdf
        Node sample1 = buildBookings(new String[] { "104", "101", "107", "101" });
        sample1 = removeFirst(sample1, "101");
        printChain(sample1);

        // Sample 2 from Question_4.pdf
        Node sample2 = buildBookings(new String[] { "104", "101", "107" });
        sample2 = removeFirst(sample2, "104");
        printChain(sample2);

        // Sample 3 from Question_4.pdf
        Node sample3 = buildBookings(new String[] { "104", "101" });
        sample3 = removeFirst(sample3, "200");
        printChain(sample3);
    }
}
