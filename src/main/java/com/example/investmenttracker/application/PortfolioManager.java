package com.example.investmenttracker.application;

import com.example.investmenttracker.adapters.gusclient.ConsumerPriceIndex;
import com.example.investmenttracker.adapters.nbpclient.Money;
import com.example.investmenttracker.adapters.persistence.InMemoryPortfolioRepository;
import com.example.investmenttracker.domain.*;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.LocalDate;
import java.util.*;

@AllArgsConstructor
@Service
public class PortfolioManager {
    private PortfolioRepository portfolioRepository;
    private Clock clock;
    private ConsumerPriceIndex cpiCalculator;

    public Portfolio addPortfolio() {
        HashMap<Stock, StockPosition> portfolioPositions = new HashMap<>();
        HashMap<LocalDate, Bond> portfolioBonds = new HashMap<>();
        Portfolio portfolio = new Portfolio(portfolioPositions, portfolioBonds, "");
        portfolioRepository.savePortfolio(portfolio);
        return portfolio;
    }

    public void addStock(UUID portfolioID, String ticker, String exchange, int quantity) {
        Portfolio portfolio = portfolioRepository.getPortfolioById(portfolioID);
        HashMap<Stock, StockPosition> portfolioPositions = portfolio.getPortfolioPositions();

        for(StockPosition stockPosition : portfolioPositions.values()) {
            if(stockPosition.getStock().getTicker().equals(ticker)) {
                int updatedQuantity = stockPosition.getQuantity()+quantity;
                StockPosition updatedStockPosition = new StockPosition(stockPosition.getUnitPrice(),updatedQuantity,stockPosition.getStock());
                portfolioPositions.put(stockPosition.getStock(), updatedStockPosition);
                //todo zwrocic portfolioPositions do portfolio
            }
            else{
                //todo dodac nowy stock do portfolio
            }
        }


    }

    public void addBond(UUID portfolioID, String series, int quantity) {
        Portfolio portfolio = portfolioRepository.getPortfolioById(portfolioID);
        HashMap<LocalDate,Bond> portfolioBonds = portfolio.getPortfolioBonds();


        for(Bond bond : portfolioBonds.values()) {
            if(bond.getBondSeries().getSeries().equals(series)) {
                int updatedQuantity = bond.getQuantity() + quantity;
                Bond updatedBond = new Bond(bond.getBondSeries(), bond.getPurchaseDate(), updatedQuantity);
                portfolioBonds.put(bond.getPurchaseDate(), updatedBond);
                //todo zworcic portfolioBonds do portfolio
            }
            else{
                //todo
            }
        }
       
    }

    public PortfolioStatus getCurrentStatus(UUID portfolioID) {
        Portfolio portfolio = portfolioRepository.getPortfolioById(portfolioID);
        HashMap<LocalDate, Bond> portfolioBonds = portfolio.getPortfolioBonds();
        HashMap<Stock, StockPosition> portfolioPositions = portfolio.getPortfolioPositions();

        List<PortfolioStatus.BondStatus> bondStatuses = new ArrayList<>();
        List<PortfolioStatus.StockStatus> stockStatuses = new ArrayList<>();

        HashMap<String, Integer> bondsCounts = new HashMap<>();
        for (Bond bond : portfolioBonds.values()) {
            String series = bond.getBondSeries().getSeries();
            int quantity = bond.getQuantity();
            bondsCounts.put(series, bondsCounts.getOrDefault(series, 0) + quantity);
        }
        for (Map.Entry<String, Integer> entry : bondsCounts.entrySet()) {
            bondStatuses.add(new PortfolioStatus.BondStatus(entry.getKey(), entry.getValue()));
        }

        for (StockPosition stockPosition : portfolioPositions.values()) {
            String ticker = stockPosition.getStock().getTicker();
            String exchange = stockPosition.getStock().getExchange();
            stockStatuses.add(new PortfolioStatus.StockStatus(ticker, exchange));
        }

        Money currentValue = portfolio.getCurrentPortfolioValue(clock, cpiCalculator);

        return new PortfolioStatus(bondStatuses, stockStatuses, currentValue);
    }
}
