package com.example.investmenttracker.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.util.Objects;


@AllArgsConstructor
@ToString
@Getter
public class Stock {
    private String ticker;
    private String exchange;
    private String name;
    private String currency;

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Stock stock = (Stock) object;
        return ticker.equals(stock.ticker)
                && exchange.equals(stock.exchange)
                && name.equals(stock.name)
                && currency.equals(stock.currency);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ticker, exchange, name, currency);
    }
}
