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
        HashMap<Pair<Integer, Integer>, BigDecimal> cache = new HashMap<>();
        cache.put(Pair.of(2021, 10), BigDecimal.valueOf(0.059));
        cache.put(Pair.of(2022, 10), BigDecimal.valueOf(0.172));
        cache.put(Pair.of(2023, 10), BigDecimal.valueOf(0.082));
        cache.put(Pair.of(2024, 10), BigDecimal.valueOf(0.049));

        CpiResponse cpiResponse = new CpiResponse();
        ConsumerPriceIndex inMemoryCPI = new InMemoryCPI(cache);

        Money unitPrice = new Money(BigDecimal.valueOf(100), Currency.getInstance("PLN"));
        BigDecimal amountOfBonds = BigDecimal.valueOf(10);

        LocalDate purchaseDate = LocalDate.of(2020, 11, 1);

        BigDecimal firstYearInterest = BigDecimal.valueOf(0.017);
        BigDecimal followingYearsInterestMargin = BigDecimal.valueOf(0.01);
        ZonedDateTime zdt = ZonedDateTime.of(2024, 12, 20, 15, 30, 0, 0, ZoneOffset.UTC);
        Clock clock = Clock.fixed(zdt.toInstant(),zdt.getZone());

        Bond bond = new Bond("EDO1130", unitPrice, amountOfBonds, purchaseDate, firstYearInterest, followingYearsInterestMargin, inMemoryCPI, BigDecimal.valueOf(3),clock);


        //when
        Money result = bond.getCurrentValue();


        //then
        Money expected = new Money(BigDecimal.valueOf(1486.05), Currency.getInstance("PLN"));
        assertEquals(expected, result);
    }

    @Test
    void shouldCalculateEarlyRedemptionValueForBondBought4YearsAgo() {
        //given
        HashMap<Pair<Integer, Integer>, BigDecimal> cache = new HashMap<>();
        cache.put(Pair.of(2021, 10), BigDecimal.valueOf(0.059));
        cache.put(Pair.of(2022, 10), BigDecimal.valueOf(0.172));
        cache.put(Pair.of(2023, 10), BigDecimal.valueOf(0.082));
        cache.put(Pair.of(2024, 10), BigDecimal.valueOf(0.049));

        CpiResponse cpiResponse = new CpiResponse();
        ConsumerPriceIndex inMemoryCPI = new InMemoryCPI(cache);

        Money unitPrice = new Money(BigDecimal.valueOf(100), Currency.getInstance("PLN"));
        BigDecimal amountOfBonds = BigDecimal.valueOf(10);

        LocalDate purchaseDate = LocalDate.of(2020, 11, 1);

        BigDecimal firstYearInterest = BigDecimal.valueOf(0.017);
        BigDecimal followingYearsInterestMargin = BigDecimal.valueOf(0.01);
        ZonedDateTime zdt = ZonedDateTime.of(2024, 12, 20, 15, 30, 0, 0, ZoneOffset.UTC);
        Clock clock = Clock.fixed(zdt.toInstant(),zdt.getZone());

        Bond bond = new Bond("EDO1130", unitPrice, amountOfBonds, purchaseDate, firstYearInterest, followingYearsInterestMargin, inMemoryCPI, BigDecimal.valueOf(3),clock);

        //when
        Money result = bond.earlyRedemptionValue();


        //then
        Money expected = new Money(BigDecimal.valueOf(0), Currency.getInstance("PLN"));
        assertEquals(expected, result);
    }

    @Test
    void shouldCalculateEarlyRedemptionValueForBondBoughtAfterOneYear() {
        //given
        HashMap<Pair<Integer, Integer>, BigDecimal> cache = new HashMap<>();
        ConsumerPriceIndex inMemoryCPI = new InMemoryCPI(cache);

        Money unitPrice = new Money(BigDecimal.valueOf(100), Currency.getInstance("PLN"));
        BigDecimal amountOfBonds = BigDecimal.valueOf(10);

        LocalDate purchaseDate = LocalDate.of(2020, 11, 1);

        BigDecimal firstYearInterest = BigDecimal.valueOf(0.0655);
        BigDecimal followingYearsInterestMargin = BigDecimal.valueOf(0.01);
        ZonedDateTime zdt = ZonedDateTime.of(2021, 5, 1, 15, 30, 0, 0, ZoneOffset.UTC);
        Clock clock = Clock.fixed(zdt.toInstant(),zdt.getZone());

        Bond bond = new Bond("EDO1130", unitPrice, amountOfBonds, purchaseDate, firstYearInterest, followingYearsInterestMargin, inMemoryCPI, BigDecimal.valueOf(3),clock);

        //when
        Money result = bond.earlyRedemptionValue();


        //then
        Money expected = new Money(BigDecimal.valueOf(2.01), Currency.getInstance("PLN"));
        assertEquals(expected, result);
    }
}