package linked_lists.assigment_problems;

import java.io.IOException;
import java.util.Scanner;

/**
 * STEP 12 - Coding Assignment Problem 5: Plate Stack on a Chain
 *
 * Task:
 * In the mess, clean plates are stacked and the top plate is always taken first.
 * Build this stack using a chain of nodes instead of an array, so it never becomes full.
 * Implement push(String plate), pop(), peek() and isEmpty() where the top of the stack
 * is the head of the chain. Write your own node class with data and next fields;
 * do not use library linked lists.
 *
 * Complexity Analysis & Comparison with Array-Based Stack:
 * - push(String plate): O(1) time, inserts a new node at head.
 * - pop(): O(1) time, removes and returns the head node.
 * - peek(): O(1) time, reads head.data without modifying the chain.
 * - isEmpty(): O(1) time, checks whether head == null.
 * - Space Complexity: O(n) for n plates currently in the stack.
 * - Comparison with Array-Based Stack:
 *   Both achieve O(1) time per operation. However, a fixed-size array stack has a
 *   maximum capacity and can overflow (or require O(n) resizing), whereas a chain-based
 *   stack allocates memory dynamically per node and never becomes full (within available
 *   heap memory), using one extra next reference per node.
 */
public class PlateStackOnAChain {

    public static class Node {
        public String data;
        public Node next;

        public Node(String data) {
            this.data = data;
            this.next = null;
        }
    }

    private Node head;
    private int size;

    public PlateStackOnAChain() {
        this.head = null;
        this.size = 0;
    }

    /**
     * Pushes a plate onto the top (head) of the chain stack in O(1) time.
     */
    public void push(String plate) {
        if (plate == null || plate.trim().isEmpty()) {
            throw new IllegalArgumentException("Plate identifier cannot be null or empty.");
        }
        String cleanPlate = plate.trim();
        Node newNode = new Node(cleanPlate);
        newNode.next = head;
        head = newNode;
        size++;
        System.out.println("push " + cleanPlate + " -> top " + head.data + ", size " + size);
    }

    /**
     * Removes and returns the top plate from the head of the chain in O(1) time.
     */
    public String pop() {
        if (head == null) {
            System.out.println("pop -> stack is empty");
            return null;
        }
        String removed = head.data;
        head = head.next;
        size--;
        System.out.println("pop -> " + removed + ", size " + size);
        return removed;
    }

    /**
     * Returns the top plate without removing it in O(1) time.
     */
    public String peek() {
        if (head == null) {
            System.out.println("peek -> stack is empty");
            return null;
        }
        System.out.println("peek -> " + head.data);
        return head.data;
    }

    /**
     * Checks whether the stack is empty in O(1) time and prints the result.
     */
    public boolean isEmpty() {
        boolean empty = (head == null);
        System.out.println("isEmpty -> " + empty);
        return empty;
    }

    public int getSize() {
        return size;
    }

    /**
     * Executes a sequence of stack operations such as "push(P1), peek(), pop(), isEmpty()".
     */
    public void executeOperations(String operationsInput) {
        if (operationsInput == null || operationsInput.trim().isEmpty()) {
            return;
        }
        String[] ops = operationsInput.split("[,\\r\\n]+");
        for (String rawOp : ops) {
            String op = rawOp.trim();
            if (op.isEmpty()) {
                continue;
            }
            if (op.startsWith("push(") && op.endsWith(")")) {
                String plate = op.substring("push(".length(), op.length() - 1).trim();
                push(plate);
            } else if (op.equals("pop()")) {
                pop();
            } else if (op.equals("peek()")) {
                peek();
            } else if (op.equals("isEmpty()")) {
                isEmpty();
            } else {
                throw new IllegalArgumentException("Unknown stack operation: " + op);
            }
        }
    }

    public static void main(String[] args) throws IOException {
        PlateStackOnAChain stack = new PlateStackOnAChain();

        if (System.in.available() > 0) {
            Scanner scanner = new Scanner(System.in);
            StringBuilder sb = new StringBuilder();
            while (scanner.hasNextLine()) {
                sb.append(scanner.nextLine()).append("\n");
            }
            stack.executeOperations(sb.toString());
            return;
        }

        // Default execution with the exact PDF sample operations from Question_5.pdf
        stack.push("P1");
        stack.push("P2");
        stack.peek();
        stack.pop();
        stack.push("P3");
        stack.pop();
        stack.pop();
        stack.pop();
        stack.isEmpty();
    }
}
