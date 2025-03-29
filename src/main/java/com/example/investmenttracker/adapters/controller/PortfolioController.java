package com.example.investmenttracker.adapters.controller;

import com.example.investmenttracker.adapters.persistence.InMemoryPortfolioRepository;
import com.example.investmenttracker.domain.Bond;
import com.example.investmenttracker.domain.BondSeries;
import com.example.investmenttracker.domain.Portfolio;
import com.example.investmenttracker.domain.PortfolioRepository;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@RestController
public class PortfolioController {

    private PortfolioRepository inMemoryPortfolioRepository;
    public record bondResponse(String serie, LocalDate endDate){}
    public record portfolioResponse(List<bondResponse> bonds){
        static portfolioResponse from(Portfolio portfolio) {
            List<bondResponse> bonds = new ArrayList<>();
            HashMap<LocalDate,Bond> bonds1 = portfolio.getPortfolioBonds();

            for(Bond bond: bonds1.values()) {
               BondSeries bondSeries = bond.getBondSeries();
               String bondName = bondSeries.getSeries();
                LocalDate purchaseDate = bond.getPurchaseDate();
                bondResponse bondResponse = new bondResponse(bondName, purchaseDate);
                bonds.add(bondResponse);
            }

            return new portfolioResponse(bonds);
        }
    }
    public record createPortfolioRequest(String portfolioName){}
    public record createPortfolioResponse(String portfolioID){}

    @GetMapping(value = "/portfolio/{id}")
    public portfolioResponse getPortfolio(@PathVariable("id") UUID id) {
        Portfolio portfolioById = inMemoryPortfolioRepository.getPortfolioById(id);
        return portfolioResponse.from(portfolioById);
    }

    @PostMapping(value = "/portfolio")
    //uzytkownik podaje imie(nazwa portfolio)
    //tworzymy obiekt portfolio i zapisujemy go do hashmapy(imie znajduje sie w portfolio)
    //zwracamy http 201 i id portfolio ktore utworzyl

    public createPortfolioResponse createPortfolio(@RequestBody createPortfolioRequest createPortfolioRequest) {
        Portfolio newPortfolio = Portfolio.create(createPortfolioRequest.portfolioName);
        inMemoryPortfolioRepository.savePortfolio(newPortfolio);
        return new createPortfolioResponse(newPortfolio.getPortfolioId().toString());
    }

}
