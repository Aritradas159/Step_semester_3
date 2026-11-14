package linked_lists.assigment_problems;

import java.io.IOException;
import java.util.Scanner;

/**
 * STEP 12 - Coding Assignment Problem 2: Late Comers in the Line
 *
 * Task:
 * Students stand in a line stored as a chain of names. A student with a pass
 * joins at the front; any other late comer joins at the end.
 * Implement Node addFirst(Node head, String name) and Node addLast(Node head, String name),
 * apply the operations in order and print the final line.
 * Write your own node class with data and next fields; do not use library linked lists.
 *
 * Complexity Analysis:
 * - addFirst(Node head, String name):
 *   Time Complexity: O(1) because the new node's next pointer is linked directly to
 *   the current head without traversing the list.
 *   Space Complexity: O(1) to allocate one new Node.
 * - addLast(Node head, String name):
 *   Time Complexity: O(n), where n is the current length of the line, because we must
 *   walk from head to the last node before linking the new node at the end (O(1) if empty).
 *   Space Complexity: O(1) to allocate one new Node.
 */
public class LateComersInTheLine {

    public static class Node {
        public String data;
        public Node next;

        public Node(String data) {
            this.data = data;
            this.next = null;
        }
    }

    /**
     * Inserts a student at the front (head) of the line in O(1) time.
     */
    public static Node addFirst(Node head, String name) {
        validateName(name);
        Node newNode = new Node(name.trim());
        newNode.next = head;
        return newNode;
    }

    /**
     * Inserts a student at the end (tail) of the line in O(n) time.
     */
    public static Node addLast(Node head, String name) {
        validateName(name);
        Node newNode = new Node(name.trim());
        if (head == null) {
            return newNode;
        }
        Node current = head;
        while (current.next != null) {
            current = current.next;
        }
        current.next = newNode;
        return head;
    }

    private static void validateName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Student name cannot be null or empty.");
        }
    }

    /**
     * Builds an initial line from an array of names.
     */
    public static Node buildLine(String[] names) {
        Node head = null;
        if (names == null) {
            return null;
        }
        for (String name : names) {
            if (name != null && !name.trim().isEmpty()) {
                head = addLast(head, name.trim());
            }
        }
        return head;
    }

    /**
     * Formats and prints the line in the form "a -> b -> null".
     */
    public static String formatLine(Node head) {
        StringBuilder sb = new StringBuilder();
        Node current = head;
        while (current != null) {
            sb.append(current.data).append(" -> ");
            current = current.next;
        }
        sb.append("null");
        return sb.toString();
    }

    public static void printLine(Node head) {
        System.out.println(formatLine(head));
    }

    /**
     * Applies a comma-separated sequence of addFirst(...) and addLast(...) operations.
     */
    public static Node applyOperations(Node head, String operationsLine) {
        if (operationsLine == null || operationsLine.trim().isEmpty()) {
            return head;
        }
        String[] ops = operationsLine.split(",");
        for (String rawOp : ops) {
            String op = rawOp.trim();
            if (op.startsWith("addFirst(") && op.endsWith(")")) {
                String name = op.substring("addFirst(".length(), op.length() - 1).trim();
                head = addFirst(head, name);
            } else if (op.startsWith("addLast(") && op.endsWith(")")) {
                String name = op.substring("addLast(".length(), op.length() - 1).trim();
                head = addLast(head, name);
            } else if (!op.isEmpty()) {
                throw new IllegalArgumentException("Unsupported operation: " + op);
            }
        }
        return head;
    }

    public static void main(String[] args) throws IOException {
        if (System.in.available() > 0) {
            Scanner scanner = new Scanner(System.in);
            if (scanner.hasNextLine()) {
                String firstLine = scanner.nextLine().trim();
                firstLine = firstLine.replaceAll("^line\\s*=\\s*", "").trim();
                Node head = null;
                if (!firstLine.equalsIgnoreCase("(empty)") && !firstLine.equalsIgnoreCase("null") && !firstLine.isEmpty()) {
                    String[] initialNames = firstLine.split("\\s*->\\s*");
                    for (String name : initialNames) {
                        if (!name.equalsIgnoreCase("null") && !name.isEmpty()) {
                            head = addLast(head, name);
                        }
                    }
                }
                if (scanner.hasNextLine()) {
                    String opsLine = scanner.nextLine().trim();
                    head = applyOperations(head, opsLine);
                }
                printLine(head);
            }
            return;
        }

        // Sample 1 from Question_2.pdf
        Node line1 = buildLine(new String[] { "Asha", "Ravi" });
        line1 = addFirst(line1, "Neha");
        line1 = addLast(line1, "Kiran");
        line1 = addFirst(line1, "Mohan");
        printLine(line1);

        // Sample 2 from Question_2.pdf
        Node line2 = null;
        line2 = addLast(line2, "Pooja");
        line2 = addFirst(line2, "Arun");
        printLine(line2);
    }
}
