package com.example.investmenttracker.domain;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class EDOBondCalculator {
    private static final BigDecimal HUNDRED = new BigDecimal("100.00");
    private static final BigDecimal PENALTY = new BigDecimal("2.00");
    private static final int SCALE = 2;

    private final LocalDate issueDate;
    private final LocalDate calculationDate;
    private final BigDecimal[] interestRates;

    public EDOBondCalculator(LocalDate issueDate, LocalDate calculationDate, BigDecimal[] interestRates) {
        this.issueDate = issueDate;
        this.calculationDate = calculationDate;
        this.interestRates = interestRates;
    }

    public BigDecimal calculateRedemptionValue() {
        // First check if we're in valid redemption period (after 7 days from purchase)
        if (ChronoUnit.DAYS.between(issueDate, calculationDate) <= 7) {
            throw new IllegalArgumentException("Przedterminowy wykup możliwy po 7 dniach od zakupu");
        }

        int currentPeriod = getCurrentPeriod();
        if (currentPeriod < 0 || currentPeriod >= 10) {
            throw new IllegalArgumentException("Data poza okresem ważności obligacji");
        }

        BigDecimal result = calculateValue(currentPeriod);

        // Value cannot be less than nominal value
        if (result.compareTo(HUNDRED) < 0) {
            return HUNDRED;
        }

        // Subtract penalty (max 2 PLN from accrued interest)
        BigDecimal interest = result.subtract(HUNDRED);
        BigDecimal penaltyToApply = interest.compareTo(PENALTY) > 0 ? PENALTY : interest;

        return result.subtract(penaltyToApply).setScale(SCALE, RoundingMode.HALF_UP);
    }

    private int getCurrentPeriod() {
        long daysBetween = ChronoUnit.DAYS.between(issueDate, calculationDate);
        return (int) (daysBetween / 365);
    }

    private BigDecimal calculateValue(int currentPeriod) {
        LocalDate periodStartDate = issueDate.plusYears(currentPeriod);
        long daysInPeriod = getDaysInPeriod(periodStartDate);
        long daysFromPeriodStart = ChronoUnit.DAYS.between(periodStartDate, calculationDate);

        BigDecimal value = HUNDRED;

        // Calculate value for completed periods
        for (int i = 0; i < currentPeriod; i++) {
            value = value.multiply(BigDecimal.ONE.add(interestRates[i]));
        }

        // Calculate partial value for current period
        if (daysFromPeriodStart > 0) {
            BigDecimal partialRate = interestRates[currentPeriod]
                    .multiply(new BigDecimal(daysFromPeriodStart))
                    .divide(new BigDecimal(daysInPeriod), 10, RoundingMode.HALF_UP);
            value = value.multiply(BigDecimal.ONE.add(partialRate));
        }

        return value.setScale(SCALE, RoundingMode.HALF_UP);
    }

    private long getDaysInPeriod(LocalDate periodStartDate) {
        LocalDate periodEndDate = periodStartDate.plusYears(1);
        return ChronoUnit.DAYS.between(periodStartDate, periodEndDate);
    }

    // Example usage
    public static void main(String[] args) {
        LocalDate issueDate = LocalDate.of(2020, 11, 1);
        LocalDate calculationDate = LocalDate.now(); // Current date

        // Example interest rates for all periods (should be provided based on actual rates)
        BigDecimal[] interestRates = {
                new BigDecimal("0.0170"), // First period: 1.70%
                new BigDecimal("0.0700"), // Example rates for subsequent periods
                new BigDecimal("0.0720"),
                new BigDecimal("0.0750"),
                new BigDecimal("0.0780"),
                new BigDecimal("0.0800"),
                new BigDecimal("0.0820"),
                new BigDecimal("0.0840"),
                new BigDecimal("0.0860"),
                new BigDecimal("0.0880")
        };

        EDOBondCalculator calculator = new EDOBondCalculator(issueDate, calculationDate, interestRates);
        try {
            BigDecimal redemptionValue = calculator.calculateRedemptionValue();
            System.out.printf("Wartość obligacji przy wykupie: %.2f PLN%n", redemptionValue);
        } catch (IllegalArgumentException e) {
            System.out.println("Błąd: " + e.getMessage());
        }
    }
}