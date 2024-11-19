package com.example.investmenttracker;


import com.example.investmenttracker.adapters.nbpclient.NbpHttpClient;

import java.math.BigDecimal;
import java.util.Currency;

public class FakeNbpClient implements NbpHttpClient {
    @Override
    public BigDecimal getExchangeRateFromNBP(Currency currency) {
        return BigDecimal.valueOf(4.20);
    }
}
