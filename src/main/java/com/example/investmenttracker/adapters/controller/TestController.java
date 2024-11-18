package com.example.investmenttracker.adapters.controller;

import com.example.investmenttracker.adapters.stockClient.StockClient;
import com.example.investmenttracker.domain.Stock;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
public class TestController {
    private final StockClient stockClient;

    @GetMapping(value = "/test")
    public String test(){
        Stock stock = new Stock("V60A","DEX","Vanguard LifeStrategy 60% Equity UCITS ETF (EUR) Accumulating","EUR");
        stockClient.getLatestPrice(stock);
        return stock.toString();
    }
}
