package com.example.investmenttracker.adapters.gusclient;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.URI;
import java.math.BigDecimal;
import java.util.HashMap;

public class CpiResponse {
    private final HashMap<Integer, BigDecimal> cpiData = new HashMap<>();

    public HashMap<Integer, BigDecimal> fetchCPIData(int year) {
        String url = "https://api-sdp.stat.gov.pl/api/1.0.0/indicators/indicator-data-indicator?id-wskaznik=639&id-rok=" + year + "&lang=pl";

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                String jsonResponse = response.body();

                Gson gson = new Gson();
                JsonElement rootElement = gson.fromJson(jsonResponse, JsonElement.class);

                if (rootElement.isJsonArray()) {
                    for (JsonElement element : rootElement.getAsJsonArray()) {
                        if (element.isJsonObject()) {
                            JsonObject dataObject = element.getAsJsonObject();

                            if (dataObject.has("id-okres") && dataObject.has("wartosc")) {
                                int idOkres = dataObject.get("id-okres").getAsInt();
                                BigDecimal wartosc = dataObject.get("wartosc").getAsBigDecimal();

                                cpiData.put(idOkres, wartosc);
                            }
                        }
                    }
                } else {
                    System.out.println("Odpowiedź API nie jest tablicą JSON.");
                }
            } else {
                System.out.println("Błąd: Kod HTTP " + response.statusCode());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return cpiData;
    }
}