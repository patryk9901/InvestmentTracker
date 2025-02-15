package com.example.investmenttracker.domain;

import java.util.UUID;

public interface PortfolioRepository {
    Portfolio getPortfolioById(UUID portfolioId);
    void savePortfolio(Portfolio portfolio);
}
