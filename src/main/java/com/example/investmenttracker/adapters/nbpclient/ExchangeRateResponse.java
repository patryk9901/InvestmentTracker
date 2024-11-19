package com.example.investmenttracker.adapters.nbpclient;

public class ExchangeRateResponse {
    private String currency;
    private String code;
    private Rate[] rates;

    public Rate[] getRates(){
        return rates;
    }

}
