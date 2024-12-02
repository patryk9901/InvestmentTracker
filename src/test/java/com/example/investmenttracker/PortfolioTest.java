package com.example.investmenttracker;

import com.example.investmenttracker.adapters.nbpclient.ClientNbp;
import com.example.investmenttracker.adapters.nbpclient.Money;
import com.example.investmenttracker.domain.Portfolio;
import com.example.investmenttracker.domain.Stock;
import com.example.investmenttracker.domain.XYZ;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Currency;
import java.util.HashMap;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class PortfolioTest {
    @Test
    void shouldCheckCurrentValue() {
        //given
        HashMap<Stock, XYZ> xyz = new HashMap<>();

        Stock appleStock = new Stock("AAPL", "NASDAQ", "Apple Inc.", "USD");
        Stock googleStock = new Stock("GOOG", "NASDAQ", "Google LLC", "USD");

        Money applePrice = new Money(BigDecimal.valueOf(10), Currency.getInstance("USD"));
        Money googlePrice = new Money(BigDecimal.valueOf(20), Currency.getInstance("EUR"));

        XYZ appleXYZ = new XYZ(applePrice, 1, appleStock);
        XYZ googleXYZ = new XYZ(googlePrice, 2, googleStock);

        xyz.put(appleStock, appleXYZ);
        xyz.put(googleStock, googleXYZ);

        ClientNbp mockClientNbp = Mockito.mock(ClientNbp.class);
        Mockito.when(mockClientNbp.convertToPLN(BigDecimal.valueOf(10), Currency.getInstance("USD")))
                .thenReturn(BigDecimal.valueOf(42.00));
        Mockito.when(mockClientNbp.convertToPLN(BigDecimal.valueOf(40), Currency.getInstance("EUR")))
                .thenReturn(BigDecimal.valueOf(84.00));
        Portfolio portfolio = new Portfolio(xyz);

        //when
        Money result = portfolio.getCurrentValue();
        //then
        assertThat(result).isEqualTo(new Money(BigDecimal.valueOf(212.52), Currency.getInstance("PLN")));
    }

    @Test
    void shouldAddNewXYZ() {
        //given
        HashMap<Stock, XYZ> xyz = new HashMap<>();

        Stock appleStock = new Stock("AAPL", "NASDAQ", "Apple Inc.", "USD");
        Money applePrice = new Money(BigDecimal.valueOf(10), Currency.getInstance("USD"));

        XYZ appleXYZ = new XYZ(applePrice, 1, appleStock);

        Portfolio portfolio = new Portfolio(xyz);

        //when
        Portfolio updatedPortfolio = portfolio.addXYZ(appleXYZ);

        //then
        assertThat(updatedPortfolio.isEmpty()).isFalse();
    }

    @Test
    void shouldIncreaseQuantityWhenStockExists() {
        //given
        HashMap<Stock, XYZ> xyz = new HashMap<>();

        Stock appleStock = new Stock("AAPL", "NASDAQ", "Apple Inc.", "USD");
        Money applePrice = new Money(BigDecimal.valueOf(10), Currency.getInstance("USD"));

        XYZ appleXYZ = new XYZ(applePrice, 1, appleStock);
        xyz.put(appleStock, appleXYZ);
        Portfolio portfolio = new Portfolio(xyz);

        //when
        Portfolio updatedPortfolio = portfolio.addXYZ(appleXYZ);

        //then
        XYZ updatedAppleXYZ = updatedPortfolio.getXyz().get(appleStock);
        assertThat(updatedAppleXYZ.getQuantity()).isEqualTo(2);
    }
}
