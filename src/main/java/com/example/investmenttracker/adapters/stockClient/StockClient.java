package com.example.investmenttracker.adapters.stockClient;

import com.example.investmenttracker.adapters.nbpclient.Money;
import com.example.investmenttracker.domain.Stock;

public interface StockClient {
     Money getLatestPrice(Stock stock);
}
