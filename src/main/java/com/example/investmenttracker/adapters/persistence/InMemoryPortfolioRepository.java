package com.example.investmenttracker.adapters.persistence;

import com.example.investmenttracker.domain.Portfolio;
import com.example.investmenttracker.domain.PortfolioRepository;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.UUID;
@Repository
public class InMemoryPortfolioRepository implements PortfolioRepository {

    private HashMap<UUID, Portfolio> portfolios;

    @Override
    public Portfolio getPortfolioById(UUID portfolioId) {
        if (portfolios.containsKey(portfolioId)) {
            return portfolios.get(portfolioId);
        }
        else{
            throw new RuntimeException("Portfolio not found");
        }
    }

    @Override
    public void savePortfolio(Portfolio portfolio) {
        portfolios.put(portfolio);
    }
}
