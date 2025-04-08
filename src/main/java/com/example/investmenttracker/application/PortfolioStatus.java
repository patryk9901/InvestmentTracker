package com.example.investmenttracker.application;

import com.example.investmenttracker.adapters.nbpclient.Money;
import lombok.AllArgsConstructor;

import java.util.List;
@AllArgsConstructor
public class PortfolioStatus {
    List<BondStatus> bondStatuses;
    List<StockStatus> stockStatuses;
    Money currentValue;
    public static record BondStatus(String series,Integer quantity) {}
    public static record StockStatus(String ticker, String exchange){}

}
