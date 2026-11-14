package linked_lists.assigment_problems;

import java.io.IOException;
import java.util.Scanner;

/**
 * STEP 12 - Coding Assignment Problem 3: Book Copy Finder
 *
 * Task:
 * Book IDs on a returns trolley are stored as a chain. Find the position of the
 * first copy of a given book (counting from 1) and how many copies are on the trolley.
 * Implement String find(Node head, String id) with one walk through the chain.
 * Compare IDs with equals. Write your own node class with data and next fields;
 * do not use library linked lists.
 *
 * Complexity Analysis:
 * - Time Complexity: O(n), where n is the length of the chain, because we perform
 *   a single traversal from head to null inspecting each node once.
 * - Space Complexity: O(1) auxiliary space (only integer counters for position,
 *   firstPosition, and copy count).
 * - Why you cannot jump straight to the middle of a chain like in an array:
 *   An array stores elements in contiguous memory, enabling O(1) random access via
 *   base_address + index * element_size. A linked list stores nodes in arbitrary
 *   memory locations connected only by next references, so reaching the middle node
 *   requires sequentially following next pointers from the head in O(n) time.
 */
public class BookCopyFinder {

    public static class Node {
        public String data;
        public Node next;

        public Node(String data) {
            this.data = data;
            this.next = null;
        }
    }

    /**
     * Walks the chain once to find the 1-based first position and total copy count of {@code id}.
     */
    public static String find(Node head, String id) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("Book ID to search cannot be null or empty.");
        }
        String target = id.trim();
        int position = 1;
        int firstPosition = -1;
        int copies = 0;

        Node current = head;
        while (current != null) {
            if (target.equals(current.data)) {
                if (firstPosition == -1) {
                    firstPosition = position;
                }
                copies++;
            }
            position++;
            current = current.next;
        }

        if (copies == 0) {
            return target + " not found";
        }
        return target + " first at position " + firstPosition + ", copies: " + copies;
    }

    /**
     * Helper method to build a chain of book IDs from an array.
     */
    public static Node buildTrolley(String[] bookIds) {
        if (bookIds == null || bookIds.length == 0) {
            return null;
        }
        Node head = new Node(bookIds[0].trim());
        Node tail = head;
        for (int i = 1; i < bookIds.length; i++) {
            tail.next = new Node(bookIds[i].trim());
            tail = tail.next;
        }
        return head;
    }

    public static void main(String[] args) throws IOException {
        if (System.in.available() > 0) {
            Scanner scanner = new Scanner(System.in);
            if (scanner.hasNextLine()) {
                String line = scanner.nextLine().trim();
                String[] parts = line.split(",\\s*id\\s*=\\s*");
                if (parts.length == 2) {
                    String chainPart = parts[0].replaceAll("^trolley\\s*=\\s*", "").trim();
                    String targetId = parts[1].trim();
                    String[] ids = chainPart.split("\\s*->\\s*");
                    Node trolley = buildTrolley(ids);
                    System.out.println(find(trolley, targetId));
                }
            }
            return;
        }

        // Sample 1 from Question_3.pdf
        String[] sampleIds = { "B12", "B07", "B30", "B07", "B45", "B07" };
        Node trolley1 = buildTrolley(sampleIds);
        System.out.println(find(trolley1, "B07"));

        // Sample 2 from Question_3.pdf
        Node trolley2 = buildTrolley(sampleIds);
        System.out.println(find(trolley2, "B99"));
    }
}
