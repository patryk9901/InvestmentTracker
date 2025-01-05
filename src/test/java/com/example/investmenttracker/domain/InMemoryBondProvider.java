package com.example.investmenttracker.domain;

import com.example.investmenttracker.adapters.nbpclient.Money;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.HashMap;

public class InMemoryBondProvider implements BondProvider {
    private HashMap<String, BondSeries> bonds;

    public InMemoryBondProvider(HashMap<String, BondSeries> bonds) {
        this.bonds = bonds;
    }

    @Override
    public BondSeries getBondSeries(String serie) {
        return bonds.get(serie);
    }

    public static BondProvider defaultBondProvider() {
        HashMap<String, BondSeries> bonds = new HashMap<>();
        Money money = new Money(BigDecimal.valueOf(100), Currency.getInstance("PLN"));
        BondSeries bondSeries = new BondSeries("EDO1130", money, BigDecimal.valueOf(0.017), BigDecimal.valueOf(0.01), BigDecimal.valueOf(3));
        bonds.put("EDO1130", bondSeries);
        return new InMemoryBondProvider(bonds);
    }
}
