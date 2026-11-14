package linked_lists.class_problems;

import java.io.IOException;
import java.util.Scanner;

/**
 * STEP 12 - Category C Practice Problem 3: Library Return Desk
 *
 * Task:
 * At the library desk, returned books go on a small pile and the top book is always
 * shelved first. Students who want to borrow wait in a line and are served in the
 * order they arrived.
 * Build the pile as a stack on an array of size 3 (RETURN adds a book, SHELVE removes
 * the top book) and the line as a queue on an array of size 3 that reuses free places
 * (WAIT adds a student, SERVE removes the front student). Print a clear message for
 * every command, including when the pile or the line is empty or full.
 *
 * Complexity Analysis:
 * - RETURN (Stack push): O(1) time
 * - SHELVE (Stack pop): O(1) time
 * - WAIT (Circular Queue enqueue): O(1) time
 * - SERVE (Circular Queue dequeue): O(1) time
 * - Space Complexity: O(1) auxiliary space (fixed arrays of capacity 3 for both stack and queue).
 */
public class LibraryReturnDesk {

    private static final int CAPACITY = 3;

    // Array-based stack for book pile
    private final String[] pile;
    private int top;

    // Circular array-based queue for student line
    private final String[] line;
    private int front;
    private int rear;
    private int size;

    public LibraryReturnDesk() {
        this.pile = new String[CAPACITY];
        this.top = -1;

        this.line = new String[CAPACITY];
        this.front = 0;
        this.rear = -1;
        this.size = 0;
    }

    /**
     * Pushes a returned book onto the pile stack.
     */
    public String returnBook(String book) {
        if (book == null || book.trim().isEmpty()) {
            throw new IllegalArgumentException("Book ID cannot be null or empty.");
        }
        String cleanBook = book.trim();
        String msg;
        if (top == CAPACITY - 1) {
            msg = cleanBook + " rejected: pile is full";
        } else {
            pile[++top] = cleanBook;
            msg = cleanBook + " placed on pile";
        }
        System.out.println(msg);
        return msg;
    }

    /**
     * Pops and shelves the top book from the pile stack.
     */
    public String shelve() {
        String msg;
        if (top == -1) {
            msg = "pile is empty";
        } else {
            String book = pile[top];
            pile[top--] = null;
            msg = "shelved " + book;
        }
        System.out.println(msg);
        return msg;
    }

    /**
     * Enqueues a student into the circular waiting line.
     */
    public String waitInLine(String student) {
        if (student == null || student.trim().isEmpty()) {
            throw new IllegalArgumentException("Student name cannot be null or empty.");
        }
        String cleanStudent = student.trim();
        String msg;
        if (size == CAPACITY) {
            msg = cleanStudent + " rejected: line is full";
        } else {
            rear = (rear + 1) % CAPACITY;
            line[rear] = cleanStudent;
            size++;
            msg = cleanStudent + " joined the line";
        }
        System.out.println(msg);
        return msg;
    }

    /**
     * Dequeues and serves the front student from the circular waiting line.
     */
    public String serve() {
        String msg;
        if (size == 0) {
            msg = "no one waiting";
        } else {
            String student = line[front];
            line[front] = null;
            front = (front + 1) % CAPACITY;
            size--;
            msg = "served " + student;
        }
        System.out.println(msg);
        return msg;
    }

    public int getPileSize() {
        return top + 1;
    }

    public int getLineSize() {
        return size;
    }

    /**
     * Executes a single command string such as "RETURN B1", "WAIT Asha", "SHELVE", or "SERVE".
     */
    public String executeCommand(String rawCommand) {
        if (rawCommand == null || rawCommand.trim().isEmpty()) {
            return "";
        }
        String cmd = rawCommand.trim();
        if (cmd.startsWith("RETURN ")) {
            return returnBook(cmd.substring("RETURN ".length()).trim());
        } else if (cmd.equals("SHELVE")) {
            return shelve();
        } else if (cmd.startsWith("WAIT ")) {
            return waitInLine(cmd.substring("WAIT ".length()).trim());
        } else if (cmd.equals("SERVE")) {
            return serve();
        } else {
            throw new IllegalArgumentException("Unknown command: " + cmd);
        }
    }

    /**
     * Executes a sequence of commands separated by commas or newlines.
     */
    public void executeCommandSequence(String input) {
        if (input == null || input.trim().isEmpty()) {
            return;
        }
        String[] commands = input.split("[,\\r\\n]+");
        for (String command : commands) {
            if (!command.trim().isEmpty()) {
                executeCommand(command.trim());
            }
        }
    }

    public static void main(String[] args) throws IOException {
        LibraryReturnDesk desk = new LibraryReturnDesk();

        if (System.in.available() > 0) {
            Scanner scanner = new Scanner(System.in);
            StringBuilder sb = new StringBuilder();
            while (scanner.hasNextLine()) {
                sb.append(scanner.nextLine()).append("\n");
            }
            desk.executeCommandSequence(sb.toString());
            return;
        }

        // Default execution with the exact PDF sample input
        String sampleCommands =
                "RETURN B1, RETURN B2, RETURN B3, RETURN B4,\n"
                + "WAIT Asha, WAIT Ravi, SHELVE, SERVE,\n"
                + "SHELVE, SHELVE, SHELVE, SERVE, SERVE";

        desk.executeCommandSequence(sampleCommands);
    }
}
