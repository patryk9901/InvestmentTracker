package com.example.investmenttracker.domain;

import com.example.investmenttracker.adapters.gusclient.CPI;
import com.example.investmenttracker.adapters.gusclient.ConsumerPriceIndex;
import com.example.investmenttracker.adapters.gusclient.CpiResponse;
import com.example.investmenttracker.adapters.gusclient.InMemoryCPI;
import com.example.investmenttracker.adapters.nbpclient.Money;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.*;
import java.util.Calendar;
import java.util.Currency;
import java.util.Date;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class BondTest {

    @Test
    void shouldCalculateCurrentValue() {
        //given
        BondProvider inMemoryBondProvider = InMemoryBondProvider.defaultBondProvider();
        ConsumerPriceIndex inMemoryCPI = InMemoryCPI.defaultInMemoryCPI();
        BigDecimal amountOfBonds = BigDecimal.valueOf(10);

        LocalDate purchaseDate = LocalDate.of(2020, 11, 1);

        ZonedDateTime zdt = ZonedDateTime.of(2024, 12, 20, 15, 30, 0, 0, ZoneOffset.UTC);
        Clock clock = Clock.fixed(zdt.toInstant(),zdt.getZone());

        Bond bond = new Bond(inMemoryBondProvider.getBondSeries("EDO1130"),amountOfBonds, purchaseDate);

        //when
        Money result = bond.getCurrentValue(clock,inMemoryCPI);


        //then
        Money expected = new Money(BigDecimal.valueOf(1486.05), Currency.getInstance("PLN"));
        assertEquals(expected, result);
    }

    @Test
    void shouldCalculateEarlyRedemptionValueForBondBought4YearsAgo() {
        //given
        BondProvider inMemoryBondProvider = InMemoryBondProvider.defaultBondProvider();
        ConsumerPriceIndex inMemoryCPI = InMemoryCPI.defaultInMemoryCPI();

        BigDecimal amountOfBonds = BigDecimal.valueOf(10);

        LocalDate purchaseDate = LocalDate.of(2020, 11, 1);

        ZonedDateTime zdt = ZonedDateTime.of(2024, 12, 20, 15, 30, 0, 0, ZoneOffset.UTC);
        Clock clock = Clock.fixed(zdt.toInstant(),zdt.getZone());

        Bond bond = new Bond(inMemoryBondProvider.getBondSeries("EDO1130"),amountOfBonds, purchaseDate);

        //when
        Money result = bond.earlyRedemptionValue(clock,inMemoryCPI);


        //then
        Money expected = new Money(BigDecimal.valueOf(0), Currency.getInstance("PLN"));
        assertEquals(expected, result);
    }

    @Test
    void shouldCalculateEarlyRedemptionValueForBondBoughtAfterOneYear() {
        //given
        BondProvider inMemoryBondProvider = InMemoryBondProvider.defaultBondProvider();
        HashMap<Pair<Integer, Integer>, BigDecimal> cache = new HashMap<>();
        ConsumerPriceIndex inMemoryCPI = new InMemoryCPI(cache);

        BigDecimal amountOfBonds = BigDecimal.valueOf(10);

        LocalDate purchaseDate = LocalDate.of(2020, 11, 1);

        ZonedDateTime zdt = ZonedDateTime.of(2021, 5, 1, 15, 30, 0, 0, ZoneOffset.UTC);
        Clock clock = Clock.fixed(zdt.toInstant(),zdt.getZone());

        Bond bond = new Bond(inMemoryBondProvider.getBondSeries("EDO1130"),amountOfBonds, purchaseDate);
        //when
        Money result = bond.earlyRedemptionValue(clock,inMemoryCPI);


        //then
        Money expected = new Money(BigDecimal.valueOf(2.01), Currency.getInstance("PLN"));
        assertEquals(expected, result);
    }
}