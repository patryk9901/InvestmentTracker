package com.example.investmenttracker.domain;

import com.example.investmenttracker.adapters.gusclient.ConsumerPriceIndex;
import com.example.investmenttracker.adapters.nbpclient.Money;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.LocalDate;
import java.util.*;

@Getter
public class Portfolio {
    private final HashMap<Stock, StockPosition> portfolioPositions;
    private final HashMap<LocalDate, Bond> portfolioBonds;
    private final UUID portfolioId;
    private final String name;


    public static Portfolio create(String name){
        HashMap<Stock, StockPosition> portfolioPositions = new HashMap<>();
        HashMap<LocalDate, Bond> portfolioBonds = new HashMap<>();
        return new Portfolio(portfolioPositions, portfolioBonds,name);
    }

    public Portfolio(HashMap<Stock, StockPosition> portfolioPositions, HashMap<LocalDate, Bond> portfolioBonds,String name) {
        this.portfolioPositions = portfolioPositions;
        this.portfolioBonds = portfolioBonds;
        this.portfolioId = UUID.randomUUID();
        this.name = name;
    }

    public Portfolio(HashMap<Stock, StockPosition> portfolioPositions, HashMap<LocalDate, Bond> portfolioBonds, UUID portfolioId, String name) {
        this.portfolioPositions = portfolioPositions;
        this.portfolioBonds = portfolioBonds;
        this.portfolioId = portfolioId;
        this.name = name;
    }

    public Money getCurrentPortfolioValue(Clock clock, ConsumerPriceIndex cpiCalculator) {
        Money totalValue = new Money(BigDecimal.valueOf(0), Currency.getInstance("PLN"));

        for (Stock stock : this.portfolioPositions.keySet()) {
            StockPosition value = this.portfolioPositions.get(stock);
            Money stockMoney = value.getTotalValue();
            totalValue = totalValue.add(stockMoney);
        }

        for (Bond bond : this.portfolioBonds.values()) {
            totalValue = totalValue.add(bond.getCurrentValue(clock, cpiCalculator));
        }

        return totalValue;
    }


    public Portfolio addStockPosition(StockPosition stockPosition) {
        HashMap<Stock, StockPosition> newPortfolioPositions = new HashMap<>(this.portfolioPositions);

        if (newPortfolioPositions.containsKey(stockPosition.getStock())) {
            newPortfolioPositions.compute(stockPosition.getStock(),
                    (k, existingStockPosition) -> new StockPosition(stockPosition.getUnitPrice(), existingStockPosition.getQuantity() + stockPosition.getQuantity(), stockPosition.getStock()));
        } else {
            newPortfolioPositions.put(stockPosition.getStock(), stockPosition);
        }

        return new Portfolio(newPortfolioPositions, this.portfolioBonds, this.portfolioId, this.name);
    }

    public Portfolio addBond(Bond bond) {
        HashMap<LocalDate, Bond> newPortfolioBonds = new HashMap<>(this.portfolioBonds);

        if (!newPortfolioBonds.containsKey(bond.getPurchaseDate())) {
            newPortfolioBonds.put(bond.getPurchaseDate(),bond);
        } else {
            Bond updatedBond = new Bond(bond.getBondSeries(),bond.getPurchaseDate(),bond.getQuantity()+newPortfolioBonds.get(bond.getPurchaseDate()).getQuantity());
            newPortfolioBonds.put(bond.getPurchaseDate(),updatedBond);
        }
        return new Portfolio(this.portfolioPositions, newPortfolioBonds, this.portfolioId, this.name);
    }

    public boolean isEmpty() {
        return this.portfolioPositions.isEmpty() && this.portfolioBonds.isEmpty();
    }
}
