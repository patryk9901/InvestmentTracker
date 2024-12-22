package com.example.investmenttracker.domain;

import com.example.investmenttracker.adapters.nbpclient.Money;
import lombok.Getter;
import lombok.RequiredArgsConstructor;


@Getter
@RequiredArgsConstructor
public class StockPosition {
    private final Money unitPrice;
    private final int quantity;
    private final Stock stock;

    public Money getTotalValue() {
        return unitPrice.multiply(quantity);
    }
}
