package com.example.investmenttracker.domain;

import com.example.investmenttracker.adapters.nbpclient.Money;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
@Getter
@AllArgsConstructor
public class BondSeries {
     final String series;
     final Money unitPrice;
     final BigDecimal firstYearInterest;
     final BigDecimal followingYearsInterestMargin;
     final Money earlyRedemptionPrice;
}
