//TODO NAPRAWIC TESTY BOND
package com.example.investmenttracker.domain;

import com.example.investmenttracker.adapters.gusclient.ConsumerPriceIndex;
import com.example.investmenttracker.adapters.gusclient.InMemoryCPI;
import com.example.investmenttracker.adapters.nbpclient.Money;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.*;
import java.util.Currency;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class BondTest {

    @Test
    void shouldCalculateCurrentValue(){
        BondProvider inMemoryBondProvider = InMemoryBondProvider.defaultBondProvider();
        ConsumerPriceIndex inMemoryCPI = InMemoryCPI.defaultInMemoryCPI();

        LocalDate purchaseDate = LocalDate.of(2020, 11, 9);

        ZonedDateTime zdt = ZonedDateTime.of(2025, 1, 8, 15, 30, 0, 0, ZoneOffset.UTC);
        Clock clock = Clock.fixed(zdt.toInstant(),zdt.getZone());

        Bond bond = new Bond(inMemoryBondProvider.getBondSeries("EDO1130"), purchaseDate,3);

        //when
        Money result = bond.getCurrentValue(clock,inMemoryCPI);


        //then
        Money expected = new Money(BigDecimal.valueOf(425.06), Currency.getInstance("PLN"));
        assertEquals(expected, result);
    }

    @Test
    void shouldThrowExceptionWhenCurrentPeriodIsOutOfBorders(){
        BondProvider inMemoryBondProvider = InMemoryBondProvider.defaultBondProvider();
        ConsumerPriceIndex inMemoryCPI = InMemoryCPI.defaultInMemoryCPI();

        LocalDate purchaseDate = LocalDate.of(2020, 11, 9);

        ZonedDateTime zdt = ZonedDateTime.of(2031, 11, 9, 15, 30, 0, 0, ZoneOffset.UTC);
        Clock clock = Clock.fixed(zdt.toInstant(),zdt.getZone());

        Bond bond = new Bond(inMemoryBondProvider.getBondSeries("EDO1130"), purchaseDate,1);

        // When & Then
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            bond.getCurrentValue(clock, inMemoryCPI);
        });
        assertEquals("Data poza okresem ważności obligacji", exception.getMessage());
    }

    @Test
    void shouldNotAllowPurchaseBeforeOneWeekHasPassed(){
        BondProvider inMemoryBondProvider = InMemoryBondProvider.defaultBondProvider();
        ConsumerPriceIndex inMemoryCPI = InMemoryCPI.defaultInMemoryCPI();

        LocalDate purchaseDate = LocalDate.of(2020, 11, 9);

        ZonedDateTime zdt = ZonedDateTime.of(2020, 11, 11, 15, 30, 0, 0, ZoneOffset.UTC);
        Clock clock = Clock.fixed(zdt.toInstant(),zdt.getZone());

        Bond bond = new Bond(inMemoryBondProvider.getBondSeries("EDO1130"), purchaseDate,1);

        // When & Then
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            bond.getCurrentValue(clock, inMemoryCPI);
        });

        assertEquals("Przedterminowy wykup możliwy po 7 dniach od zakupu", exception.getMessage());
    }

    @Test
    void shouldReturnUnitPriceWhenResultIsLowerThanUnitPrice(){
        BondProvider inMemoryBondProvider = InMemoryBondProvider.defaultBondProvider();
        ConsumerPriceIndex inMemoryCPI = InMemoryCPI.defaultInMemoryCPI();

        LocalDate purchaseDate = LocalDate.of(2020, 11, 9);

        ZonedDateTime zdt = ZonedDateTime.of(2020, 11, 17, 15, 30, 0, 0, ZoneOffset.UTC);
        Clock clock = Clock.fixed(zdt.toInstant(),zdt.getZone());

        Bond bond = new Bond(inMemoryBondProvider.getBondSeries("EDO1130EditedInterest"), purchaseDate,5);

        //when
        Money result = bond.getCurrentValue(clock,inMemoryCPI);


        //then
        Money expected = new Money(BigDecimal.valueOf(500.0), Currency.getInstance("PLN"));
        assertEquals(expected, result);
    }


    @Test
    void shouldCalculateEarlyRedemptionValueForBondBought4YearsAgo() {
        //given
        BondProvider inMemoryBondProvider = InMemoryBondProvider.defaultBondProvider();
        ConsumerPriceIndex inMemoryCPI = InMemoryCPI.defaultInMemoryCPI();

        LocalDate purchaseDate = LocalDate.of(2020, 11, 9);

        ZonedDateTime zdt = ZonedDateTime.of(2024, 11, 9, 15, 30, 0, 0, ZoneOffset.UTC);
        Clock clock = Clock.fixed(zdt.toInstant(),zdt.getZone());

        Bond bond = new Bond(inMemoryBondProvider.getBondSeries("EDO1130"), purchaseDate,10);

        //when
        Money result = bond.earlyRedemptionValue(clock,inMemoryCPI);


        //then
        Money expected = new Money(BigDecimal.valueOf(1373.26), Currency.getInstance("PLN"));
        assertEquals(expected, result);
    }

    @Test
    void shouldCalculateEarlyRedemptionValueForBondBoughtAfterOneYear() {
        //given
        BondProvider inMemoryBondProvider = InMemoryBondProvider.defaultBondProvider();
        HashMap<Pair<Integer, Integer>, BigDecimal> cache = new HashMap<>();
        ConsumerPriceIndex inMemoryCPI = new InMemoryCPI(cache);


        LocalDate purchaseDate = LocalDate.of(2020, 11, 9);

        ZonedDateTime zdt = ZonedDateTime.of(2021, 11, 9, 15, 30, 0, 0, ZoneOffset.UTC);
        Clock clock = Clock.fixed(zdt.toInstant(),zdt.getZone());

        Bond bond = new Bond(inMemoryBondProvider.getBondSeries("EDO1130"), purchaseDate,3);
        //when
        Money result = bond.earlyRedemptionValue(clock,inMemoryCPI);


        //then
        Money expected = new Money(BigDecimal.valueOf(300.0), Currency.getInstance("PLN"));
        assertEquals(expected, result);
    }
}