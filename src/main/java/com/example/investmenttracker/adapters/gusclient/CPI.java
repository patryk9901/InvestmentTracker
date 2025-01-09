package com.example.investmenttracker.adapters.gusclient;

import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;


@AllArgsConstructor
public class CPI {
    private final CpiResponse cpiResponse;



    public BigDecimal averageYearCPI(int year) {
        HashMap<Integer, BigDecimal> daneCPI = cpiResponse.fetchCPIData(year);

        BigDecimal sumCPI = BigDecimal.ZERO;

        for (int i = 0; i < 12; i++) {
            int period = 247 + i;
            BigDecimal valueCPI = daneCPI.get(period);

            if (valueCPI != null) {
                sumCPI = sumCPI.add(valueCPI);
            } else {
                System.out.println("Brak danych CPI");
            }
        }
        return sumCPI.divide(BigDecimal.valueOf(12), 2, RoundingMode.HALF_UP);
    }

}