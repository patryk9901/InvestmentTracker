package com.example.investmenttracker.domain;

import com.example.investmenttracker.adapters.nbpclient.ClientNbp;
import com.example.investmenttracker.adapters.nbpclient.DefaultNbpClient;
import com.example.investmenttracker.adapters.nbpclient.Money;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Currency;
import java.util.HashMap;
import java.util.List;

@Getter
@AllArgsConstructor
public class Portfolio {
    private final HashMap<Stock, XYZ> xyz;


    public Money getCurrentValue() {
        Money totalValue = new Money(BigDecimal.valueOf(0), Currency.getInstance("PLN"));
        DefaultNbpClient defaultNbpClient = new DefaultNbpClient();
        ClientNbp clientNbp = new ClientNbp(defaultNbpClient);

        for (Stock stock : this.xyz.keySet()) {
            XYZ value = this.xyz.get(stock);
            Money stockMoney = value.getTotalValue();
            if(stockMoney.getCurrency().equals(Currency.getInstance("PLN"))){
                totalValue = totalValue.add(stockMoney);
            }else{
                BigDecimal convertedToPLN = clientNbp.convertToPLN(stockMoney.getAmount(),stockMoney.getCurrency());
                Money moneyToPLN = new Money(convertedToPLN, Currency.getInstance("PLN"));
                totalValue = totalValue.add(moneyToPLN);
            }
        }
        return totalValue;
    }

    public Portfolio addXYZ(XYZ xyz) {
        HashMap<Stock, XYZ> newXYZ = new HashMap<>(this.xyz);

        if (newXYZ.containsKey(xyz.getStock())) {
            XYZ existingXYZ = newXYZ.get(xyz.getStock());
            newXYZ.put(xyz.getStock(),
                    new XYZ(xyz.getUnitPrice(), existingXYZ.getQuantity() + xyz.getQuantity(), xyz.getStock()));
        } else {
            newXYZ.put(xyz.getStock(), xyz);
        }

        return new Portfolio(newXYZ);
    }

    public boolean isEmpty(){
        return this.xyz.isEmpty();
    }
}
