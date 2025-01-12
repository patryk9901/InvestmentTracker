package com.example.investmenttracker;

import com.example.investmenttracker.adapters.gusclient.ConsumerPriceIndex;
import com.example.investmenttracker.adapters.gusclient.InMemoryCPI;
import com.example.investmenttracker.adapters.nbpclient.Money;
import com.example.investmenttracker.domain.*;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Currency;
import java.util.HashMap;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class PortfolioTest {


    @Test
    void shouldCheckCurrentValueWithOnlyStocksAvailable() {
        //given
        HashMap<Stock, StockPosition> portfolioElements = new HashMap<>();
        HashMap<LocalDate, List<Bond>> bonds = new HashMap<>();

        Stock appleStock = new Stock("AAPL", "NASDAQ", "Apple Inc.", "PLN");
        Stock googleStock = new Stock("GOOG", "NASDAQ", "Google LLC", "PLN");

        Money applePrice = new Money(BigDecimal.valueOf(10), Currency.getInstance("PLN"));
        Money googlePrice = new Money(BigDecimal.valueOf(20), Currency.getInstance("PLN"));

        StockPosition appleStockPosition = new StockPosition(applePrice, 1, appleStock);
        StockPosition googleStockPosition = new StockPosition(googlePrice, 2, googleStock);

        portfolioElements.put(appleStock, appleStockPosition);
        portfolioElements.put(googleStock, googleStockPosition);

        ConsumerPriceIndex inMemoryCPI = InMemoryCPI.defaultInMemoryCPI();

        ZonedDateTime zdt = ZonedDateTime.of(2025, 1, 8, 15, 30, 0, 0, ZoneOffset.UTC);
        Clock clock = Clock.fixed(zdt.toInstant(), zdt.getZone());

        Portfolio portfolio = new Portfolio(portfolioElements, bonds);

        //when
        Money result = portfolio.getCurrentPortfolioValue(clock, inMemoryCPI);
        //then
        assertThat(result).isEqualTo(new Money(BigDecimal.valueOf(50.00), Currency.getInstance("PLN")));
    }

    @Test
    void shouldCheckCurrentValue() {
        //given
        HashMap<Stock, StockPosition> portfolioElements = new HashMap<>();
        HashMap<LocalDate, List<Bond>> bondsHashMap = new HashMap<>();
        List<Bond> bondList = new ArrayList<>();

        Stock appleStock = new Stock("AAPL", "NASDAQ", "Apple Inc.", "PLN");
        Stock googleStock = new Stock("GOOG", "NASDAQ", "Google LLC", "PLN");

        Money applePrice = new Money(BigDecimal.valueOf(10), Currency.getInstance("PLN"));
        Money googlePrice = new Money(BigDecimal.valueOf(20), Currency.getInstance("PLN"));

        StockPosition appleStockPosition = new StockPosition(applePrice, 1, appleStock);
        StockPosition googleStockPosition = new StockPosition(googlePrice, 2, googleStock);

        portfolioElements.put(appleStock, appleStockPosition);
        portfolioElements.put(googleStock, googleStockPosition);

        BondProvider inMemoryBondProvider = InMemoryBondProvider.defaultBondProvider();
        ConsumerPriceIndex inMemoryCPI = InMemoryCPI.defaultInMemoryCPI();

        LocalDate purchaseDate = LocalDate.of(2020, 11, 9);

        ZonedDateTime zdt = ZonedDateTime.of(2025, 1, 8, 15, 30, 0, 0, ZoneOffset.UTC);
        Clock clock = Clock.fixed(zdt.toInstant(),zdt.getZone());

        Bond bond = new Bond(inMemoryBondProvider.getBondSeries("EDO1130"), purchaseDate);
        bondList.add(bond);
        bondsHashMap.put(purchaseDate, bondList);

        Portfolio portfolio = new Portfolio(portfolioElements, bondsHashMap);

        //when
        Money result = portfolio.getCurrentPortfolioValue(clock, inMemoryCPI);
        //then
        assertThat(result).isEqualTo(new Money(BigDecimal.valueOf(191.69), Currency.getInstance("PLN")));
    }

    @Test
    void shouldCheckCurrentValueWithOnlyBonds() {
        //given
        HashMap<Stock, StockPosition> portfolioElements = new HashMap<>();
        HashMap<LocalDate, List<Bond>> bondsHashMap = new HashMap<>();
        List<Bond> bondList = new ArrayList<>();

        BondProvider inMemoryBondProvider = InMemoryBondProvider.defaultBondProvider();
        ConsumerPriceIndex inMemoryCPI = InMemoryCPI.defaultInMemoryCPI();

        LocalDate purchaseDate = LocalDate.of(2020, 11, 9);

        ZonedDateTime zdt = ZonedDateTime.of(2025, 1, 8, 15, 30, 0, 0, ZoneOffset.UTC);
        Clock clock = Clock.fixed(zdt.toInstant(),zdt.getZone());

        Bond bond = new Bond(inMemoryBondProvider.getBondSeries("EDO1130"), purchaseDate);
        Bond bond2 = new Bond(inMemoryBondProvider.getBondSeries("EDO1130"), purchaseDate);
        Bond bond3 = new Bond(inMemoryBondProvider.getBondSeries("EDO1130"), purchaseDate);
        bondList.add(bond);
        bondList.add(bond2);
        bondList.add(bond3);
        bondsHashMap.put(purchaseDate, bondList);

        Portfolio portfolio = new Portfolio(portfolioElements, bondsHashMap);


        //when
        Money result = portfolio.getCurrentPortfolioValue(clock, inMemoryCPI);

        //then
        assertThat(result).isEqualTo(new Money(BigDecimal.valueOf(425.07), Currency.getInstance("PLN")));
    }

    @Test
    void shouldAddNewStockPosition() {
        //given
        HashMap<Stock, StockPosition> portfolioElements = new HashMap<>();
        HashMap<LocalDate, List<Bond>> bonds = new HashMap<>();

        Stock appleStock = new Stock("AAPL", "NASDAQ", "Apple Inc.", "PLN");
        Money applePrice = new Money(BigDecimal.valueOf(10), Currency.getInstance("PLN"));

        StockPosition appleStockPosition = new StockPosition(applePrice, 1, appleStock);

        Portfolio portfolio = new Portfolio(portfolioElements, bonds);

        //when
        Portfolio updatedPortfolio = portfolio.addStockPosition(appleStockPosition);

        //then
        assertThat(updatedPortfolio.isEmpty()).isFalse();
    }

    @Test
    void shouldIncreaseQuantityWhenStockExists() {
        //given
        HashMap<Stock, StockPosition> portfolioPositions = new HashMap<>();
        HashMap<LocalDate, List<Bond>> bonds = new HashMap<>();

        Stock appleStock = new Stock("AAPL", "NASDAQ", "Apple Inc.", "PLN");
        Money applePrice = new Money(BigDecimal.valueOf(10), Currency.getInstance("PLN"));

        StockPosition appleStockPosition = new StockPosition(applePrice, 1, appleStock);
        portfolioPositions.put(appleStock, appleStockPosition);
        Portfolio portfolio = new Portfolio(portfolioPositions, bonds);

        //when
        Portfolio updatedPortfolio = portfolio.addStockPosition(appleStockPosition);

        //then
        StockPosition updatedAppleStockPosition = updatedPortfolio.getPortfolioPositions().get(appleStock);
        assertThat(updatedAppleStockPosition.getQuantity()).isEqualTo(2);
    }

    @Test
    void shouldAddNewBondWhenEmpty() {
        //given
        HashMap<Stock, StockPosition> portfolioElements = new HashMap<>();
        HashMap<LocalDate, List<Bond>> bondsHashMap = new HashMap<>();

        BondProvider inMemoryBondProvider = InMemoryBondProvider.defaultBondProvider();
        LocalDate purchaseDate = LocalDate.of(2020, 11, 9);
        Bond bond = new Bond(inMemoryBondProvider.getBondSeries("EDO1130"), purchaseDate);

        Portfolio portfolio = new Portfolio(portfolioElements, bondsHashMap);

        //when
        Portfolio updatedPortfolio = portfolio.addBond(bond);

        //then
        assertThat(updatedPortfolio.isEmpty()).isFalse();
    }

    @Test
    void shouldAddNewBondWhenAlreadyExistsAndIncreaseQuantity() {
        //given
        HashMap<Stock, StockPosition> portfolioElements = new HashMap<>();
        HashMap<LocalDate, List<Bond>> bondsHashMap = new HashMap<>();
        List<Bond> bondList = new ArrayList<>();

        BondProvider inMemoryBondProvider = InMemoryBondProvider.defaultBondProvider();
        LocalDate purchaseDate = LocalDate.of(2020, 11, 9);
        Bond bond = new Bond(inMemoryBondProvider.getBondSeries("EDO1130"), purchaseDate);
        bondList.add(bond);
        bondsHashMap.put(purchaseDate, bondList);

        Portfolio portfolio = new Portfolio(portfolioElements, bondsHashMap);

        //when
        Portfolio updatedPortfolio = portfolio.addBond(bond);
        HashMap<LocalDate, List<Bond>> bondsNewHashMap = portfolio.getPortfolioBonds();
        List<Bond> bondListNew =bondsNewHashMap.get(purchaseDate);

        //then
        assertThat(updatedPortfolio.isEmpty()).isFalse();
        assertThat(bondListNew.size()).isEqualTo(2);
    }
}
