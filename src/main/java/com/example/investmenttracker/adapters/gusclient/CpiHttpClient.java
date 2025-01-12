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
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class XYZ {
    private static final long CPI_TOTAL_ID = 6656078;
    private final HashMap<Integer, BigDecimal> cpiData = new HashMap<>();


    public HashMap<Integer, BigDecimal> fetchCPIData(int year) {
        String url = "https://api-sdp.stat.gov.pl/api/1.0.0/indicators/indicator-data-indicator?id-wskaznik=339&id-rok=" + year + "&lang=pl";

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
                CPIResponse[] cpiResponseArray = gson.fromJson(jsonResponse, CPIResponse[].class);
                cpiData.putAll(
                        Stream.of(cpiResponseArray)
                                .filter(cpiResponse -> cpiResponse.idPozycja2 == CPI_TOTAL_ID)
                                .collect(Collectors.toMap(
                                        CPIResponse::getIdOkres,
                                        CPIResponse::getWartosc
                                ))
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return cpiData;
    }
}