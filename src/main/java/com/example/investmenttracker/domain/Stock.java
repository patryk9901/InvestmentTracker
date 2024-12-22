package com.example.investmenttracker.domain;

import lombok.Getter;
import lombok.ToString;

import java.util.Currency;
import java.util.Objects;


@ToString
@Getter
public class Stock {
    private final String ticker;
    private final String exchange;
    private final String name;
    private final String currency;

    public Stock(String ticker, String exchange, String name, String currency) {
        this.ticker = ticker;
        this.exchange = exchange;
        this.name = name;
        this.currency = currencyCheck(currency);
    }

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

    public String currencyCheck(String currencyCode) {
        if(!currencyCode.equals("PLN")){
            throw new IllegalArgumentException("Incorrect currency. Only acceptable is PLN");
        }else{
            return currencyCode;
        }
    }

}
