package com.example.investmenttracker.adapters.nbpclient;


import com.google.gson.Gson;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Currency;
import java.util.HashMap;
import java.util.Map;

public class ClientNbp {

    private final Map<String, BigDecimal> exchangeRates;
    private final NbpHttpClient client;

    public ClientNbp(NbpHttpClient nbpHttpClient) {
        this.exchangeRates = new HashMap<>();
        this.client = nbpHttpClient;
        initializeExchangeRates();
    }

    private void initializeExchangeRates() {
        addCurrencyToMap("EUR");
        addCurrencyToMap("USD");
        addCurrencyToMap("GBP");
    }


    private void addCurrencyToMap(String currencyCode) {
        BigDecimal rate = getExchangeRateFromNBP(Currency.getInstance(currencyCode));
        if (rate != null) {
            exchangeRates.put(currencyCode, rate);
        }
    }

    public BigDecimal getExchangeRateFromNBP(Currency currency) {

        return client.getExchangeRateFromNBP(currency);
    }


    public BigDecimal getExchangeRate(Currency currency) {
        String currencyCode = currency.getCurrencyCode();

        if (exchangeRates.containsKey(currencyCode)) {
            return exchangeRates.get(currencyCode);
        } else {
            throw new IllegalArgumentException("Kurs dla waluty " + currencyCode + " nie jest dostępny.");
        }
    }

    public BigDecimal convertFromPLN(BigDecimal amountInPLN, Currency targetCurrency) {
        BigDecimal exchangeRate = getExchangeRate(targetCurrency);
        return amountInPLN.divide(exchangeRate, 2, RoundingMode.HALF_UP);
    }

    public BigDecimal convertToPLN(BigDecimal amount, Currency currency) {
        BigDecimal exchangeRate = getExchangeRate(currency);
        return amount.multiply(exchangeRate).setScale(2, RoundingMode.HALF_UP);
    }
}