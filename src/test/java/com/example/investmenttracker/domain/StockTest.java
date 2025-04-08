package com.example.investmenttracker.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class StockTest {

    @Test
    void shouldCreateOnlyPLNStock() {
        //given
        String validCurrency = "PLN";
        String invalidCurrency = "USD";

        //when
        assertDoesNotThrow(() -> new Stock("ABC", "EFGH", "Random Stock", validCurrency));

        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> new Stock("AAPL", "NASDAQ", "Apple Inc.", invalidCurrency));

        //then
        assertThat(exception.getMessage()).isEqualTo("Incorrect currency. Only acceptable is PLN");
    }
}