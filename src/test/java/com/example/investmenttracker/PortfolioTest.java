package com.example.investmenttracker;

import com.example.investmenttracker.adapters.nbpclient.Money;
import com.example.investmenttracker.domain.Bond;
import com.example.investmenttracker.domain.Portfolio;
import com.example.investmenttracker.domain.Stock;
import com.example.investmenttracker.domain.StockPosition;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Currency;
import java.util.HashMap;

import static org.assertj.core.api.Assertions.assertThat;

public class PortfolioTest {



    @Test
    void shouldCheckCurrentValue() {
        //given
        HashMap<Stock, StockPosition> portfolioElements = new HashMap<>();
        HashMap<LocalDate, Bond> bonds = new HashMap<>();

        Stock appleStock = new Stock("AAPL", "NASDAQ", "Apple Inc.", "PLN");
        Stock googleStock = new Stock("GOOG", "NASDAQ", "Google LLC", "PLN");

        Money applePrice = new Money(BigDecimal.valueOf(10), Currency.getInstance("PLN"));
        Money googlePrice = new Money(BigDecimal.valueOf(20), Currency.getInstance("PLN"));

        StockPosition appleStockPosition = new StockPosition(applePrice, 1, appleStock);
        StockPosition googleStockPosition = new StockPosition(googlePrice, 2, googleStock);

        portfolioElements.put(appleStock, appleStockPosition);
        portfolioElements.put(googleStock, googleStockPosition);



        Portfolio portfolio = new Portfolio(portfolioElements);

        //when
        Money result = portfolio.getCurrentValue();
        //then
        assertThat(result).isEqualTo(new Money(BigDecimal.valueOf(50.00), Currency.getInstance("PLN")));
    }

    @Test
    void shouldAddNewStockPosition() {
        //given
        HashMap<Stock, StockPosition> portfolioElements = new HashMap<>();

        Stock appleStock = new Stock("AAPL", "NASDAQ", "Apple Inc.", "PLN");
        Money applePrice = new Money(BigDecimal.valueOf(10), Currency.getInstance("PLN"));

        StockPosition appleStockPosition = new StockPosition(applePrice, 1, appleStock);

        Portfolio portfolio = new Portfolio(portfolioElements);

        //when
        Portfolio updatedPortfolio = portfolio.addStockPosition(appleStockPosition);

        //then
        assertThat(updatedPortfolio.isEmpty()).isFalse();
    }

    @Test
    void shouldIncreaseQuantityWhenStockExists() {
        //given
        HashMap<Stock, StockPosition> portfolioPositions = new HashMap<>();

        Stock appleStock = new Stock("AAPL", "NASDAQ", "Apple Inc.", "PLN");
        Money applePrice = new Money(BigDecimal.valueOf(10), Currency.getInstance("PLN"));

        StockPosition appleStockPosition = new StockPosition(applePrice, 1, appleStock);
        portfolioPositions.put(appleStock, appleStockPosition);
        Portfolio portfolio = new Portfolio(portfolioPositions);

        //when
        Portfolio updatedPortfolio = portfolio.addStockPosition(appleStockPosition);

        //then
        StockPosition updatedAppleStockPosition = updatedPortfolio.getPortfolioPositions().get(appleStock);
        assertThat(updatedAppleStockPosition.getQuantity()).isEqualTo(2);
    }
}
