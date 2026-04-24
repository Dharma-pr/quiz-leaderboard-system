import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;
import com.fasterxml.jackson.databind.*;

public class QuizLeaderboard {

    static final String BASE_URL = "https://devapigw.vidalhealthtpa.com/srm-quiz-task";
    static final String REG_NO = "RA2311003020348";

    public static void main(String[] args) throws Exception {

        HttpClient client = HttpClient.newHttpClient();
        ObjectMapper mapper = new ObjectMapper();

        Set<String> uniqueSet = new HashSet<>();
        Map<String, Integer> scoreMap = new HashMap<>();

        for (int i = 0; i < 10; i++) {

            String url = BASE_URL + "/quiz/messages?regNo=" + REG_NO + "&poll=" + i;

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            JsonNode root = mapper.readTree(response.body());
            JsonNode events = root.get("events");

            for (JsonNode event : events) {

                String roundId = event.get("roundId").asText();
                String participant = event.get("participant").asText();
                int score = event.get("score").asInt();

                String key = roundId + "_" + participant;

                if (!uniqueSet.contains(key)) {
                    uniqueSet.add(key);
                    scoreMap.put(participant,
                            scoreMap.getOrDefault(participant, 0) + score);
                }
            }

            Thread.sleep(5000);
        }

        List<Map.Entry<String, Integer>> list = new ArrayList<>(scoreMap.entrySet());
        list.sort((a, b) -> b.getValue() - a.getValue());

        List<Map<String, Object>> leaderboard = new ArrayList<>();
        int total = 0;

        for (Map.Entry<String, Integer> e : list) {
            Map<String, Object> obj = new HashMap<>();
            obj.put("participant", e.getKey());
            obj.put("totalScore", e.getValue());

            leaderboard.add(obj);
            total += e.getValue();
        }

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("regNo", REG_NO);
        requestBody.put("leaderboard", leaderboard);

        String json = mapper.writeValueAsString(requestBody);

        HttpRequest postRequest = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/quiz/submit"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> postResponse = client.send(postRequest, HttpResponse.BodyHandlers.ofString());

        System.out.println(postResponse.body());
        System.out.println("Total Score: " + total);
    }
}
