package com.example.investmenttracker.domain;

import com.example.investmenttracker.adapters.gusclient.ConsumerPriceIndex;
import com.example.investmenttracker.adapters.nbpclient.Money;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Clock;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;

@AllArgsConstructor
public class Bond {
    private BondSeries bondSeries;
    private final LocalDate purchaseDate;

    public Money getCurrentValue(Clock clock, ConsumerPriceIndex cpiCalculator) {
        if (ChronoUnit.DAYS.between(purchaseDate, LocalDate.now(clock)) <= 7) {
            throw new IllegalArgumentException("Przedterminowy wykup możliwy po 7 dniach od zakupu");
        }
        int currentPeriod = getCurrentPeriod(clock);
        if (currentPeriod < 0 || currentPeriod >= 10) {
            throw new IllegalArgumentException("Data poza okresem ważności obligacji");
        }

        BigDecimal result = calculateValue(currentPeriod, cpiCalculator, clock);

        if (result.compareTo(bondSeries.unitPrice.getAmount()) < 0) {
            return bondSeries.unitPrice;
        }
        return new Money(result, Currency.getInstance("PLN"));
    }

    public Money earlyRedemptionValue(Clock clock, ConsumerPriceIndex cpiCalculator) {
        Money valueOfBond = getCurrentValue(clock, cpiCalculator);
        Money interest = valueOfBond.subtract(bondSeries.unitPrice);


        if(interest.compareTo(bondSeries.earlyRedemptionPrice) < 0) {
            return bondSeries.unitPrice;
        }
        interest = interest.subtract(bondSeries.earlyRedemptionPrice);
        return bondSeries.unitPrice.add(interest);
    }

    private BigDecimal calculateValue(int currentPeriod, ConsumerPriceIndex cpiCalculator, Clock clock) {
        LocalDate periodStartDate = purchaseDate.plusYears(currentPeriod);
        long daysInPeriod = getDaysInPeriod(periodStartDate);
        long daysFromPeriodStart = ChronoUnit.DAYS.between(periodStartDate, LocalDate.now(clock));

        List<BigDecimal> interestRates = new ArrayList<>();
        interestRates.add(bondSeries.firstYearInterest);

        for (int year = purchaseDate.getYear() + 1; year <= LocalDate.now(clock).getYear(); year++) {
            BigDecimal cpiValue = cpiCalculator.fromLast12Months(year, purchaseDate.getMonthValue() - 1);
            if (cpiValue == null) {
                break;
            }
            BigDecimal interestRate = cpiValue.add(bondSeries.followingYearsInterestMargin);
            interestRates.add(interestRate);
        }

        BigDecimal value = bondSeries.unitPrice.getAmount();

        for (int i = 0; i < currentPeriod; i++) {
            value = value.multiply(BigDecimal.ONE.add(interestRates.get(i)));
        }

        if (daysFromPeriodStart > 0) {
            BigDecimal partialRate = interestRates.get(currentPeriod)
                    .multiply(new BigDecimal(daysFromPeriodStart))
                    .divide(new BigDecimal(daysInPeriod), 6, RoundingMode.HALF_UP);
            value = value.multiply(BigDecimal.ONE.add(partialRate));
        }

        return value.setScale(6, RoundingMode.HALF_UP);
    }

    private int getCurrentPeriod(Clock clock) {
        long daysBetween = ChronoUnit.DAYS.between(purchaseDate, LocalDate.now(clock));
        return (int) (daysBetween / 365);
    }

    private long getDaysInPeriod(LocalDate periodStartDate) {
        LocalDate periodEndDate = periodStartDate.plusYears(1);
        return ChronoUnit.DAYS.between(periodStartDate, periodEndDate);
    }
}