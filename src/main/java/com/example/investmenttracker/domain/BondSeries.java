package com.example.investmenttracker.domain;

import com.example.investmenttracker.adapters.nbpclient.Money;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;

@AllArgsConstructor
public class BondSeries {
     final String series;
     final Money unitPrice;
     final BigDecimal firstYearInterest;
     final BigDecimal followingYearsInterestMargin;
     final Money earlyRedemptionPrice;
}
