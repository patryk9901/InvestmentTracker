package com.example.investmenttracker.domain;

import com.example.investmenttracker.application.PortfolioManager;
import com.example.investmenttracker.application.PortfolioStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.*;
import java.rmi.server.UID;
import java.util.UUID;

@SpringBootTest
class PortfolioManagerTest {

    @Autowired
    private PortfolioManager portfolioManager;

    @Test
    void shouldAddItemsToPortfolio() {
        //given
        UUID portfolioID = UUID.randomUUID();


        //when
        portfolioManager.addStock(portfolioID,"ticker","exchange",1);
        portfolioManager.addBond(portfolioID,"EDO1130",1);
        PortfolioStatus result = portfolioManager.getCurrentStatus(portfolioID);
        //then
        assertNotNull(result);
    }



    @Test
    void shouldRemoveItemsFromPortfolio() {
        //given

        //when

        //then
    }
}