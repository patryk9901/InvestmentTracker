package com.example.investmenttracker.adapters.gusclient;

import org.apache.commons.lang3.tuple.Pair;

import java.math.BigDecimal;
import java.util.HashMap;

public class InMemoryCPI implements ConsumerPriceIndex{
    private HashMap<Pair<Integer,Integer>,BigDecimal> cache;

    public InMemoryCPI(HashMap<Pair<Integer,Integer>,BigDecimal> cache) {
        this.cache = cache;
    }
    @Override
    public BigDecimal fromLast12Months(int year, int month) {

        return cache.get(Pair.of(year, month));
    }
}
