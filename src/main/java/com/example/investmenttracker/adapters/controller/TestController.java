package com.example.investmenttracker.adapters.controller;

import com.example.investmenttracker.adapters.nbpclient.DefaultNbpClient;
import com.example.investmenttracker.adapters.nbpclient.Money;
import com.example.investmenttracker.adapters.nbpclient.NbpHttpClient;
import com.example.investmenttracker.adapters.stockClient.StockClient;
import com.example.investmenttracker.domain.Stock;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Currency;


@AllArgsConstructor
@RestController
public class TestController {
    private final StockClient stockClient;
    private final NbpHttpClient nbpHttpClient;


    @GetMapping(value = "/test")
    public String test(){
        Stock stock = new Stock("V60A","DEX","Vanguard LifeStrategy 60% Equity UCITS ETF (EUR) Accumulating","EUR");
        Money latestPrice = stockClient.getLatestPrice(stock);

        Money eur = latestPrice.multiply(nbpHttpClient.getExchangeRateFromNBP(Currency.getInstance("EUR")));

        return eur.toString();
    }
}
