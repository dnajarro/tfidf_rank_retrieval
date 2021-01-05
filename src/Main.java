import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        TextProcessor textProcessor = new TextProcessor();
        int totalDocs = 322;
        Map<String, List<StatTuple>> indexTable = new HashMap<>();
        List<TokenStats> documentTable = new ArrayList<>();

        for (int i = 1; i < totalDocs + 1; i++) {
            String filename = "wiki/Doc (" + i + ").txt";
            documentTable = textProcessor.processDocument(filename, i);
            for (TokenStats tokenStats : documentTable) {
                if (!indexTable.containsKey(tokenStats.getToken())) {
                    indexTable.put(tokenStats.getToken(), tokenStats.getStatTuples());
                } else {
                    indexTable.get(tokenStats.getToken()).addAll(tokenStats.getStatTuples());
                }
            }
        }

        List<String> testQueries = new ArrayList<String>();
        testQueries.add("killing incident");
        testQueries.add("suspect charged with murder");
        testQueries.add("court");
        testQueries.add("jury sentenced murderer to prison");
        testQueries.add("movie");
        testQueries.add("entertainment films");
        testQueries.add("court appeal");
        testQueries.add("action film producer");
        testQueries.add("drunk driving accusations");
        testQueries.add("actor appeared in movie premiere");

        try {
            PrintWriter printWriter = new PrintWriter("proj1results.txt");
            int queryNum = 0;
            for (String query : testQueries) {
                queryNum++;
                List<String> keywords = textProcessor.processQuery(query);
                Ranker ranker = new Ranker();
                List<RankScore> rankScores = ranker.rankQuery(indexTable, totalDocs, keywords);
                int numOfTopRankedDocs = 10;
                SnippetExtractor snippetExtractor = new SnippetExtractor();
                printWriter.write("Query " + queryNum + ": " + query + "\n\r");
                for (int i = 0; i < 10; i++) {
                    RankScore rankScore = rankScores.get(i);
                    double score = rankScore.getScore();
                    int docID = rankScore.getDocNum();
                    String snippet = snippetExtractor.extractSnippet(docID);
                    printWriter.write((i + 1) + ": (Doc #" + docID + ") " + snippet + " Score: " + score + "\n\r");
                }
                printWriter.write("\n\r");
            }
            printWriter.close();
        } catch (FileNotFoundException e) {
            System.out.println(e.getStackTrace());
        }
    }
}
