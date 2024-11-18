package com.example.investmenttracker.adapters.stockClient;

import com.example.investmenttracker.domain.Stock;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;




@Component
public class DefaultStockClient implements StockClient {
    Logger logger = LoggerFactory.getLogger(DefaultStockClient.class);
    private  RestClient restClient;
    @Value("${API_KEY_STOCK_CLIENT}")
    private String apiKey;

    public DefaultStockClient() {
        this.restClient = RestClient.create();
    }


    @Override
    public double getLatestPrice(Stock stock) {

        String symbol = stock.getTicker() + "." +stock.getExchange();
        String result = restClient.get()
                .uri("https://www.alphavantage.co/query?function=GLOBAL_QUOTE&symbol={symbol}&apikey={apiKey}",symbol,apiKey)
                .retrieve()
                .body(String.class);
        ObjectMapper objectMapper = new ObjectMapper();
        LatestPriceResponse latestPriceResponse = null;
        try {
            latestPriceResponse = objectMapper.readValue(result, LatestPriceResponse.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        logger.info(latestPriceResponse.toString());

        return 0;

    }
}
