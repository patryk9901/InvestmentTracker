package com.example.investmenttracker.adapters.gusclient;

import java.math.BigDecimal;

public interface ConsumerPriceIndex {
    BigDecimal fromLast12Months(int year, int month);
}
