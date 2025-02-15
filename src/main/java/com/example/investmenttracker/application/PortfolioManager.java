package com.example.investmenttracker.application;

import com.example.investmenttracker.adapters.gusclient.ConsumerPriceIndex;
import com.example.investmenttracker.adapters.persistence.InMemoryPortfolioRepository;
import com.example.investmenttracker.domain.*;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
@AllArgsConstructor
@Service
public class PortfolioManager {
    private PortfolioRepository portfolioRepository;
    private Clock clock;
    private ConsumerPriceIndex cpiCalculator;

    public Portfolio addPortfolio(){
        HashMap<Stock, StockPosition> portfolioPositions = new HashMap<>();
        HashMap<LocalDate,>
        Portfolio portfolio = new Portfolio()
        portfolioRepository.savePortfolio();
    }
    public void addStock(UUID portfolioID, String ticker, String exchange, int i) {

    }

    public void addBond(UUID portfolioID, String edo1130, int i) {

    }

    public PortfolioStatus getCurrentStatus(UUID portfolioID) {
        Portfolio portfolio = portfolioRepository.getPortfolioById(portfolioID);
        HashMap<LocalDate, List<Bond>> portfolioBonds = portfolio.getPortfolioBonds();
        HashMap<Stock, StockPosition> portfolioPositions = portfolio.getPortfolioPositions();




        return new PortfolioStatus(List.of(),List.of(),portfolio.getCurrentPortfolioValue(clock,cpiCalculator));
    }
}
