package com.example.investmenttracker.domain;

import com.example.investmenttracker.adapters.nbpclient.ClientNbp;
import com.example.investmenttracker.adapters.nbpclient.DefaultNbpClient;
import com.example.investmenttracker.adapters.nbpclient.Money;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.HashMap;

@Getter
@AllArgsConstructor
public class Portfolio {
    private final HashMap<Stock, StockPosition> portfolioPositions;

    public Money getCurrentValue() {
        Money totalValue = new Money(BigDecimal.valueOf(0), Currency.getInstance("PLN"));
        DefaultNbpClient defaultNbpClient = new DefaultNbpClient();
        ClientNbp clientNbp = new ClientNbp(defaultNbpClient);

        for (Stock stock : this.portfolioPositions.keySet()) {
            StockPosition value = this.portfolioPositions.get(stock);
            Money stockMoney = value.getTotalValue();
            if(stockMoney.getCurrency().equals(Currency.getInstance("PLN"))){
                totalValue = totalValue.add(stockMoney);
            }else{
                BigDecimal convertedToPLN = clientNbp.convertToPLN(stockMoney.getAmount(),stockMoney.getCurrency());
                Money newMoneyInPLN = new Money(convertedToPLN, Currency.getInstance("PLN"));
                totalValue = totalValue.add(newMoneyInPLN);
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

        return new Portfolio(newPortfolioPositions);
    }

    public boolean isEmpty(){
        return this.portfolioPositions.isEmpty();
    }
}
