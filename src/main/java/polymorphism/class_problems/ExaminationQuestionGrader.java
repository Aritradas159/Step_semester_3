package polymorphism.class_problems;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

/**
 * Class (Practice) Problem 4: Examination Question Grader.
 */
public class ExaminationQuestionGrader {

    abstract static class Question {
        protected final String text, correct, student;
        protected final BigDecimal points;

        Question(String text, String correct, String student, BigDecimal points) {
            this.text = text;
            this.correct = correct;
            this.student = student;
            this.points = points;
        }

        abstract String type();

        abstract BigDecimal grade();
    }

    /** Shared exact-match rule used by MCQ and TF. */
    abstract static class ExactMatchQuestion extends Question {
        ExactMatchQuestion(String q, String c, String s, BigDecimal p) { super(q, c, s, p); }

        @Override BigDecimal grade() { return student.equals(correct) ? points : BigDecimal.ZERO; }
    }

    static class McqQuestion extends ExactMatchQuestion {
        McqQuestion(String q, String c, String s, BigDecimal p) { super(q, c, s, p); }
        @Override String type() { return "MCQ"; }
    }

    static class TrueFalseQuestion extends ExactMatchQuestion {
        TrueFalseQuestion(String q, String c, String s, BigDecimal p) { super(q, c, s, p); }
        @Override String type() { return "TF"; }
    }

    static class EssayQuestion extends Question {
        EssayQuestion(String q, String c, String s, BigDecimal p) { super(q, c, s, p); }
        @Override String type() { return "ESSAY"; }

        @Override BigDecimal grade() {
            String answer = student.toLowerCase(Locale.ROOT);
            Set<String> seen = new HashSet<>();
            int matched = 0;
            for (String k : correct.split(",")) {
                String kw = k.trim().toLowerCase(Locale.ROOT);
                if (!kw.isEmpty() && seen.add(kw) && answer.contains(kw)) matched++;
            }
            if (matched >= 2) return points.multiply(new BigDecimal("0.75"));
            if (matched == 1) return points.multiply(new BigDecimal("0.50"));
            return BigDecimal.ZERO;
        }
    }

    static Question create(List<String> t) {
        if (t.size() < 4 || t.size() > 5) {
            throw new IllegalArgumentException("Expected: Type \"Text\" \"Correct\" \"Student\" [Points]");
        }
        BigDecimal pts = t.size() == 5 ? num(t.get(4)) : BigDecimal.ONE;   // Points optional: default 1
        switch (t.get(0).toUpperCase(Locale.ROOT)) {
            case "MCQ": return new McqQuestion(t.get(1), t.get(2), t.get(3), pts);
            case "TF": return new TrueFalseQuestion(t.get(1), t.get(2), t.get(3), pts);
            case "ESSAY": return new EssayQuestion(t.get(1), t.get(2), t.get(3), pts);
            default: throw new IllegalArgumentException("Unknown question type: " + t.get(0));
        }
    }

    static String fmt(BigDecimal v) {
        return v.setScale(2, RoundingMode.HALF_UP).toPlainString();
    }

    static BigDecimal num(String s) {
        BigDecimal v = new BigDecimal(s);
        if (v.signum() < 0) throw new IllegalArgumentException("Negative value not allowed: " + s);
        return v;
    }

    /** Splits on whitespace but keeps "quoted strings" together (quotes removed). */
    static List<String> tokenize(String line) {
        List<String> out = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        boolean inQuote = false;
        for (char c : line.toCharArray()) {
            if (c == '"') {
                if (inQuote) { out.add(sb.toString()); sb.setLength(0); inQuote = false; }
                else { inQuote = true; }
            } else if (Character.isWhitespace(c) && !inQuote) {
                if (sb.length() > 0) { out.add(sb.toString()); sb.setLength(0); }
            } else {
                sb.append(c);
            }
        }
        if (inQuote) throw new IllegalArgumentException("Unterminated quote: " + line);
        if (sb.length() > 0) out.add(sb.toString());
        return out;
    }

    static List<String> readLines() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        List<String> lines = new ArrayList<>();
        String s;
        while ((s = br.readLine()) != null) {
            if (!s.trim().isEmpty()) lines.add(s.trim());
        }
        return lines;
    }

    public static void main(String[] args) throws IOException {
        try {
            List<String> lines = readLines();
            if (lines.isEmpty()) throw new IllegalArgumentException("No input");
            int n = Integer.parseInt(lines.get(0));
            if (n < 1 || lines.size() - 1 < n) {
                throw new IllegalArgumentException("Invalid N or missing input lines");
            }
            List<Question> items = new ArrayList<>();
            for (int i = 1; i <= n; i++) items.add(create(tokenize(lines.get(i))));

            StringBuilder out = new StringBuilder();
            BigDecimal total = BigDecimal.ZERO;
            for (Question it : items) {                       // runtime polymorphism
                BigDecimal v = it.grade().setScale(2, RoundingMode.HALF_UP);
                total = total.add(v);
                out.append(it.type()).append(": ").append(fmt(v)).append('\n');
            }
            out.append("Total Score: ").append(fmt(total)).append('\n');
            System.out.print(out);
        } catch (RuntimeException e) {
            System.err.println("Error: " + e.getMessage());
            System.exit(1);
        }
    }
}
