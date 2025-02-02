package com.example.investmenttracker.domain;

import com.example.investmenttracker.adapters.nbpclient.Money;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Currency;

public class BondSeriesParser {
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final int SCALE = 2;  // Set scale to 2 decimal places

    public static BondSeries parse(String generatedAnswer) {
        try {
            JsonNode root = objectMapper.readTree(generatedAnswer);
            JsonNode content = root.path("candidates").get(0).path("content").path("parts").get(0).path("text");

            JsonNode bondData = objectMapper.readTree(content.asText());

            return new BondSeries(
                    bondData.get("series").asText(),
                    new Money(
                            new BigDecimal(bondData.get("unitPrice").asText()).setScale(SCALE, RoundingMode.HALF_UP),
                            Currency.getInstance("PLN")
                    ),
                    new BigDecimal(bondData.get("firstYearInterest").asText()).setScale(SCALE, RoundingMode.HALF_UP),
                    new BigDecimal(bondData.get("followingYearsInterestMargin").asText()).setScale(SCALE, RoundingMode.HALF_UP),
                    new Money(
                            new BigDecimal(bondData.get("earlyRedemptionPrice").asText()).setScale(SCALE, RoundingMode.HALF_UP),
                            Currency.getInstance("PLN")
                    )
            );
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to parse bond series data: " + e.getMessage(), e);
        }
    }
}