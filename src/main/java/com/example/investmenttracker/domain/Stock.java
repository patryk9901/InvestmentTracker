package com.example.investmenttracker.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;


@AllArgsConstructor
@ToString
@Getter
public class Stock {
    private String ticker;
    private String exchange;
    private String name;
    private String currency;

}
