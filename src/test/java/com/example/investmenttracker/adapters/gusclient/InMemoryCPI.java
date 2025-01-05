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

    public static ConsumerPriceIndex defaultInMemoryCPI() {
        HashMap<Pair<Integer, Integer>, BigDecimal> cache = new HashMap<>();
        cache.put(Pair.of(2021, 10), BigDecimal.valueOf(0.059));
        cache.put(Pair.of(2022, 10), BigDecimal.valueOf(0.172));
        cache.put(Pair.of(2023, 10), BigDecimal.valueOf(0.082));
        cache.put(Pair.of(2024, 10), BigDecimal.valueOf(0.049));
        return new InMemoryCPI(cache);
    }
}
