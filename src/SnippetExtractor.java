import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class SnippetExtractor {
    public String extractSnippet(int docID) {
        StringBuilder sb = new StringBuilder();
        try {
            File file = new File("wiki/Doc (" + docID + ").txt");
            Scanner scanner = new Scanner(file);
            int totalLines = 0;
            while (scanner.hasNextLine()) {
                scanner.nextLine();
                totalLines++;
            }

            scanner = new Scanner(file);
            boolean isSentenceEnded = false;
            int curLine = 1;
            while (scanner.hasNextLine() && !isSentenceEnded) {
                String line = scanner.nextLine();
                int endIndex = findEndPunctuation(line, totalLines, curLine);
                if (endIndex != -1) {
                    sb.append(line, 0, endIndex);
                    isSentenceEnded = true;
                } else {
                    sb.append(line);
                }
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("File not found.");
            e.printStackTrace();
        }

        return sb.toString();
    }

    private int findEndPunctuation(String s, int totalLines, int curLine) {
        if (totalLines == curLine) {
            return s.length();
        }
        if (s.contains(".") || s.contains("!") || s.contains("?")) {
            int period = s.indexOf(".");
            if (period == -1)
                period = Integer.MAX_VALUE;
            int exclamation = s.indexOf("!");
            if (exclamation == -1)
                exclamation = Integer.MAX_VALUE;
            int question = s.indexOf("?");
                if (question == -1)
                    question = Integer.MAX_VALUE;
            int index = Math.min(Math.min(period, exclamation), question);
            if (index == Integer.MAX_VALUE)
                return -1;
            return index + 1;
        }
        return -1;
    }
}
