package com.example.investmenttracker.domain;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import lombok.AllArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
@Component
public class GeminiAPIRequest {
    private static final String MODEL_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash-exp:generateContent";
    @Value("${API_KEY_GEMINI}")
    @Setter
    private String apiKey;
    private final HttpClient client;

    public GeminiAPIRequest() {
        this.client = HttpClient.newHttpClient();
    }
    public String generateContent(String userPrompt) {
        // Create JSON payload
        JsonObject payload = createPayload(userPrompt);
        String jsonPayload = new Gson().toJson(payload);


        URI uri = UriComponentsBuilder.fromHttpUrl(MODEL_URL)
                .queryParam("key", apiKey)
                .build()
                .toUri();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonPayload))
                .build();

        // Send request and get response
        HttpResponse<String> response = null;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException e) {

        } catch (InterruptedException e) {

        }
        return response.body();
    }

    private static JsonObject createPayload(String userPrompt) {
        JsonObject payload = new JsonObject();

        // Create contents array
        JsonArray contentsArray = new JsonArray();
        JsonObject contentItem = new JsonObject();
        contentItem.addProperty("role", "user");

        JsonObject parts = new JsonObject();
        parts.addProperty("text", userPrompt);
        JsonArray partsArray = new JsonArray();
        partsArray.add(parts);
        contentItem.add("parts", partsArray);

        contentsArray.add(contentItem);
        payload.add("contents", contentsArray);

        // Add generation config
        JsonObject generationConfig = new JsonObject();
        generationConfig.addProperty("temperature", 1.0);
        generationConfig.addProperty("topK", 40);
        generationConfig.addProperty("topP", 0.95);
        generationConfig.addProperty("maxOutputTokens", 8192);
        generationConfig.addProperty("responseMimeType", "application/json");

        // Add response schema
        JsonObject schema = createResponseSchema();
        generationConfig.add("responseSchema", schema);

        payload.add("generationConfig", generationConfig);

        return payload;
    }

    private static JsonObject createResponseSchema() {
        JsonObject schema = new JsonObject();
        schema.addProperty("type", "object");

        JsonObject properties = new JsonObject();
        addPropertyToSchema(properties, "series", "string");
        addPropertyToSchema(properties, "unitPrice", "number");
        addPropertyToSchema(properties, "firstYearInterest", "number");
        addPropertyToSchema(properties, "followingYearsInterestMargin", "number");
        addPropertyToSchema(properties, "earlyRedemptionPrice", "number");

        schema.add("properties", properties);

        JsonArray required = new JsonArray();
        required.add("series");
        required.add("unitPrice");
        required.add("firstYearInterest");
        required.add("followingYearsInterestMargin");
        required.add("earlyRedemptionPrice");
        schema.add("required", required);

        return schema;
    }

    private static void addPropertyToSchema(JsonObject properties, String name, String type) {
        JsonObject prop = new JsonObject();
        prop.addProperty("type", type);
        properties.add(name, prop);
    }
}