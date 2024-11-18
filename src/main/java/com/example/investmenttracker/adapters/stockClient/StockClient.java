package com.example.investmenttracker.adapters.stockClient;

import com.example.investmenttracker.domain.Stock;

public interface StockClient {
    public double getLatestPrice(Stock stock);
}
