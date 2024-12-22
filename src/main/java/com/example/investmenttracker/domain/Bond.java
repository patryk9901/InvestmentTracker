package com.example.investmenttracker.domain;

import com.example.investmenttracker.adapters.gusclient.ConsumerPriceIndex;
import com.example.investmenttracker.adapters.nbpclient.Money;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Clock;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;

@AllArgsConstructor
public class Bond {
    private final String series;
    private final Money unitPrice;
    private final BigDecimal amountOfBonds;
    private final LocalDate purchaseDate;
    private final BigDecimal firstYearInterest;
    private final BigDecimal followingYearsInterestMargin;
    private final ConsumerPriceIndex cpiCalculator;
    private final BigDecimal earlyRedemptionPrice;
    private final Clock clock;

    public Money getCurrentValue() {
        BigDecimal currentValue = unitPrice.getAmount();

        currentValue = currentValue.add(currentValue.multiply(firstYearInterest));

        int emissionYear = purchaseDate.getYear();
        int currentYear = LocalDate.now(clock).getYear();

        for (int year = emissionYear + 1; year <= currentYear; year++) {
            BigDecimal cpiValue = cpiCalculator.fromLast12Months(year, purchaseDate.getMonthValue() - 1);
            if (cpiValue == null) {
                break;
            }
            else if(cpiValue.compareTo(BigDecimal.ZERO) < 0) {
                cpiValue = BigDecimal.ZERO;
            }
            BigDecimal interestRate = cpiValue.add(followingYearsInterestMargin);
            BigDecimal interestValue = currentValue.multiply(interestRate);
            currentValue = currentValue.add(interestValue);
        }

        currentValue = calculateForAllBonds(currentValue);

        return new Money(currentValue.setScale(2, RoundingMode.HALF_UP), Currency.getInstance("PLN"));
    }

    public Money earlyRedemptionValue() {
        BigDecimal currentValue;
        BigDecimal n = unitPrice.getAmount();
        BigDecimal a = BigDecimal.valueOf(ChronoUnit.DAYS.between(purchaseDate, LocalDate.now(clock)));
        BigDecimal r = firstYearInterest;


        int emissionYear = purchaseDate.getYear();
        int currentYear = LocalDate.now(clock).getYear();

        BigDecimal act = BigDecimal.valueOf(365);
//        if ((emissionYear % 4 == 0 && emissionYear % 100 != 0) || emissionYear % 400 == 0) {
//            act = BigDecimal.valueOf(366);
//        }

        //TODO rok rozliczenia obligacji = 365dni - wrócić i uwzględnić rok przestępny oraz "ACT"
        if (a.compareTo(BigDecimal.valueOf(365)) <= 0) {
            BigDecimal x = r.multiply(a).divide(act, 6, RoundingMode.HALF_UP).add(BigDecimal.ONE);
            currentValue = n.multiply(x).subtract(earlyRedemptionPrice);
            if (currentValue.compareTo(unitPrice.getAmount()) > 0) {
                BigDecimal profit = currentValue.subtract(unitPrice.getAmount());
                currentValue = deductingBelkaTax(profit);
            } else {
                currentValue = BigDecimal.ZERO;
            }
            currentValue = calculateForAllBonds(currentValue);
        }

        else {
            List<BigDecimal> followingYearsR = new ArrayList<>();
            followingYearsR.add(firstYearInterest);

            for (int year = emissionYear + 1; year <= currentYear; year++) {
                BigDecimal cpiValue = cpiCalculator.fromLast12Months(year, purchaseDate.getMonthValue() - 1);
                if (cpiValue == null) {
                    break;
                }
                BigDecimal interestRate = cpiValue.add(followingYearsInterestMargin);
                followingYearsR.add(interestRate);
            }

            BigDecimal allRsPlus1value = BigDecimal.ONE;

            int i = 0;
            for (int year = emissionYear; year <= currentYear -1; year++) {
                r = followingYearsR.get(i);
                BigDecimal rPlus1value = r.add(BigDecimal.ONE);
                allRsPlus1value = allRsPlus1value.multiply(rPlus1value);
                i++;
            }
            r = followingYearsR.getLast();

            BigDecimal x = r.multiply(a).divide(act, 6, RoundingMode.HALF_UP).add(BigDecimal.ONE);

            currentValue = n.multiply(x).multiply(allRsPlus1value).subtract(earlyRedemptionPrice);

            if (currentValue.compareTo(unitPrice.getAmount()) > 0) {
                BigDecimal profit = currentValue.subtract(unitPrice.getAmount());
                currentValue = deductingBelkaTax(profit);
            } else {
                currentValue = BigDecimal.ZERO;
            }
            currentValue = calculateForAllBonds(currentValue);
        }
        return new Money(currentValue, Currency.getInstance("PLN"));
    }


    public BigDecimal calculateForAllBonds(BigDecimal currentValue) {
        return currentValue.multiply(amountOfBonds);
    }

    public BigDecimal deductingBelkaTax(BigDecimal currentValue) {
        return currentValue.multiply(BigDecimal.valueOf(0.81));
    }

}