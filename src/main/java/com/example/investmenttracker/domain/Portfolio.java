package com.example.investmenttracker.domain;

import com.example.investmenttracker.adapters.gusclient.ConsumerPriceIndex;
import com.example.investmenttracker.adapters.nbpclient.Money;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Currency;
import java.util.HashMap;
import java.util.List;

@Getter
@AllArgsConstructor
public class Portfolio {
    private final HashMap<Stock, StockPosition> portfolioPositions;
    private final HashMap<LocalDate, List<Bond>> portfolioBonds;

    public Money getCurrentPortfolioValue(Clock clock, ConsumerPriceIndex cpiCalculator) {
        Money totalValue = new Money(BigDecimal.valueOf(0), Currency.getInstance("PLN"));

        for (Stock stock : this.portfolioPositions.keySet()) {
            StockPosition value = this.portfolioPositions.get(stock);
            Money stockMoney = value.getTotalValue();
            totalValue = totalValue.add(stockMoney);
        }

        for(List<Bond> bonds : this.portfolioBonds.values()) {
            for(Bond bond : bonds) {
                totalValue = totalValue.add(bond.getCurrentValue(clock, cpiCalculator));
            }
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

        return new Portfolio(newPortfolioPositions, this.portfolioBonds);
    }

    public Portfolio addBond(Bond bond) {
        HashMap<LocalDate, List<Bond>> newPortfolioBonds = new HashMap<>(this.portfolioBonds);
        List<Bond> newBonds = new ArrayList<>();
        if(!newPortfolioBonds.containsKey(bond.getPurchaseDate())) {
            newBonds.add(bond);
            newPortfolioBonds.put(bond.getPurchaseDate(), newBonds);
        }
        else{
            newPortfolioBonds.get(bond.getPurchaseDate()).add(bond);
        }
        return new Portfolio(this.portfolioPositions, newPortfolioBonds);
    }

    public boolean isEmpty(){
        return this.portfolioPositions.isEmpty() && this.portfolioBonds.isEmpty();
    }
}
