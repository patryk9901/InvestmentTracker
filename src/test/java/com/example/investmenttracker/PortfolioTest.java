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
import java.util.Currency;
import java.util.HashMap;

import static org.assertj.core.api.Assertions.assertThat;

public class PortfolioTest {


    @Test
    void shouldCheckCurrentValueWithOnlyStocksAvailable() {
        //given
        Portfolio portfolio = Portfolio.create("");

        Stock appleStock = new Stock("AAPL", "NASDAQ", "Apple Inc.", "PLN");
        Stock googleStock = new Stock("GOOG", "NASDAQ", "Google LLC", "PLN");

        Money applePrice = new Money(BigDecimal.valueOf(10), Currency.getInstance("PLN"));
        Money googlePrice = new Money(BigDecimal.valueOf(20), Currency.getInstance("PLN"));

        StockPosition appleStockPosition = new StockPosition(applePrice, 3, appleStock);
        StockPosition googleStockPosition = new StockPosition(googlePrice, 5, googleStock);

        portfolio.getPortfolioPositions().put(appleStock, appleStockPosition);
        portfolio.getPortfolioPositions().put(googleStock, googleStockPosition);

        ConsumerPriceIndex inMemoryCPI = InMemoryCPI.defaultInMemoryCPI();

        ZonedDateTime zdt = ZonedDateTime.of(2025, 1, 8, 15, 30, 0, 0, ZoneOffset.UTC);
        Clock clock = Clock.fixed(zdt.toInstant(), zdt.getZone());

        //when
        Money result = portfolio.getCurrentPortfolioValue(clock, inMemoryCPI);
        //then
        assertThat(result).isEqualTo(new Money(BigDecimal.valueOf(130.00), Currency.getInstance("PLN")));
    }

    @Test
    void shouldCheckCurrentValue() {
        //given
        Portfolio portfolio = Portfolio.create("");


        Stock appleStock = new Stock("AAPL", "NASDAQ", "Apple Inc.", "PLN");
        Stock googleStock = new Stock("GOOG", "NASDAQ", "Google LLC", "PLN");

        Money applePrice = new Money(BigDecimal.valueOf(10), Currency.getInstance("PLN"));
        Money googlePrice = new Money(BigDecimal.valueOf(20), Currency.getInstance("PLN"));

        StockPosition appleStockPosition = new StockPosition(applePrice, 1, appleStock);
        StockPosition googleStockPosition = new StockPosition(googlePrice, 2, googleStock);

        portfolio.getPortfolioPositions().put(appleStock, appleStockPosition);
        portfolio.getPortfolioPositions().put(googleStock, googleStockPosition);

        BondProvider inMemoryBondProvider = InMemoryBondProvider.defaultBondProvider();
        ConsumerPriceIndex inMemoryCPI = InMemoryCPI.defaultInMemoryCPI();

        LocalDate purchaseDate = LocalDate.of(2020, 11, 9);

        ZonedDateTime zdt = ZonedDateTime.of(2025, 1, 8, 15, 30, 0, 0, ZoneOffset.UTC);
        Clock clock = Clock.fixed(zdt.toInstant(),zdt.getZone());

        Bond bond = new Bond(inMemoryBondProvider.getBondSeries("EDO1130"), purchaseDate,1);
        portfolio.getPortfolioBonds().put(purchaseDate, bond);

        //when
        Money result = portfolio.getCurrentPortfolioValue(clock, inMemoryCPI);

        //then
        assertThat(result).isEqualTo(new Money(BigDecimal.valueOf(191.69), Currency.getInstance("PLN")));
    }

    @Test
    void shouldCheckCurrentValueWithOnlyBonds() {
        //given
        Portfolio portfolio = Portfolio.create("");

        BondProvider inMemoryBondProvider = InMemoryBondProvider.defaultBondProvider();
        ConsumerPriceIndex inMemoryCPI = InMemoryCPI.defaultInMemoryCPI();

        LocalDate purchaseDate = LocalDate.of(2020, 11, 9);
        LocalDate purchaseDate2 = LocalDate.of(2020, 11, 10);
        LocalDate purchaseDate3 = LocalDate.of(2020, 11, 11);

        ZonedDateTime zdt = ZonedDateTime.of(2025, 1, 8, 15, 30, 0, 0, ZoneOffset.UTC);
        Clock clock = Clock.fixed(zdt.toInstant(),zdt.getZone());

        Bond bond = new Bond(inMemoryBondProvider.getBondSeries("EDO1130"), purchaseDate,8);
        Bond bond2 = new Bond(inMemoryBondProvider.getBondSeries("EDO1130"), purchaseDate,2);
        Bond bond3 = new Bond(inMemoryBondProvider.getBondSeries("EDO1130"), purchaseDate,3);

        portfolio.getPortfolioBonds().put(purchaseDate, bond);
        portfolio.getPortfolioBonds().put(purchaseDate2, bond2);
        portfolio.getPortfolioBonds().put(purchaseDate3, bond3);

        //when
        Money result = portfolio.getCurrentPortfolioValue(clock, inMemoryCPI);

        //then
        assertThat(result).isEqualTo(new Money(BigDecimal.valueOf(1841.93), Currency.getInstance("PLN")));
    }

    @Test
    void shouldAddNewStockPosition() {
        //given
        Portfolio portfolio = Portfolio.create("");

        Stock appleStock = new Stock("AAPL", "NASDAQ", "Apple Inc.", "PLN");
        Money applePrice = new Money(BigDecimal.valueOf(10), Currency.getInstance("PLN"));

        StockPosition appleStockPosition = new StockPosition(applePrice, 1, appleStock);



        //when
        Portfolio updatedPortfolio = portfolio.addStockPosition(appleStockPosition);

        //then
        assertThat(updatedPortfolio.isEmpty()).isFalse();
        assertThat(updatedPortfolio.getPortfolioPositions().get(appleStockPosition.getStock()).getQuantity()).isEqualTo(1);
    }

    @Test
    void shouldIncreaseQuantityWhenStockExists() {
        //given
        Portfolio portfolio = Portfolio.create("");

        Stock appleStock = new Stock("AAPL", "NASDAQ", "Apple Inc.", "PLN");
        Money applePrice = new Money(BigDecimal.valueOf(10), Currency.getInstance("PLN"));

        StockPosition appleStockPosition = new StockPosition(applePrice, 1, appleStock);

        portfolio = portfolio.addStockPosition(appleStockPosition);

        //when
        Portfolio updatedPortfolio = portfolio.addStockPosition(appleStockPosition);

        //then
        StockPosition updatedAppleStockPosition = updatedPortfolio.getPortfolioPositions().get(appleStock);
        assertThat(updatedAppleStockPosition.getQuantity()).isEqualTo(2);
    }

    @Test
    void shouldAddNewBondWhenEmpty() {
        //given
        Portfolio portfolio = Portfolio.create("");

        BondProvider inMemoryBondProvider = InMemoryBondProvider.defaultBondProvider();
        LocalDate purchaseDate = LocalDate.of(2020, 11, 9);
        Bond bond = new Bond(inMemoryBondProvider.getBondSeries("EDO1130"), purchaseDate,1);

        //when
        Portfolio updatedPortfolio = portfolio.addBond(bond);

        //then
        assertThat(updatedPortfolio.isEmpty()).isFalse();
    }

    @Test
    void shouldAddNewBondWhenAlreadyExistsAndIncreaseQuantity() {
        //given
        Portfolio portfolio = Portfolio.create("");


        BondProvider inMemoryBondProvider = InMemoryBondProvider.defaultBondProvider();
        LocalDate purchaseDate = LocalDate.of(2020, 11, 9);
        Bond bond = new Bond(inMemoryBondProvider.getBondSeries("EDO1130"), purchaseDate,1);

        portfolio.getPortfolioBonds().put(purchaseDate, bond);

        //when
        Portfolio updatedPortfolio = portfolio.addBond(bond);
        HashMap<LocalDate, Bond> bondsNewHashMap = updatedPortfolio.getPortfolioBonds();

        //then
        assertThat(updatedPortfolio.isEmpty()).isFalse();
        assertThat(bondsNewHashMap.size()).isEqualTo(1);
        assertThat(bondsNewHashMap.get(purchaseDate).getQuantity()).isEqualTo(2);
    }
}
