package com.example.investmenttracker.domain;

import com.example.investmenttracker.adapters.nbpclient.Money;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.Currency;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
@SpringBootTest

class DefaultBondProviderTest {

    @Autowired
    private DefaultBondProvider defaultBondProvider;



    @Test
    void shouldRetrieveEDO0128BondSeries() {
        // when
        BondSeries result = defaultBondProvider.getBondSeries("EDO0128");

        // then
        assertThat(result).isNotNull();
        assertThat(result.series).isEqualTo("EDO0128");
        assertThat(result.unitPrice).isEqualTo(new Money(new BigDecimal("100.00"), Currency.getInstance("PLN")));
        assertThat(result.firstYearInterest).isEqualTo(new BigDecimal("2.70"));
        assertThat(result.followingYearsInterestMargin).isEqualTo(new BigDecimal("1.50"));
        assertThat(result.earlyRedemptionPrice).isEqualTo(new Money(new BigDecimal("2.00"), Currency.getInstance("PLN")));
    }

    @Test
    void shouldRetrieveEDO1130BondSeries() {
        // when
        BondSeries result = defaultBondProvider.getBondSeries("EDO1130");

        // then
        assertThat(result).isNotNull();
        assertThat(result.series).isEqualTo("EDO1130");
        assertThat(result.unitPrice).isEqualTo(new Money(new BigDecimal("100.00"), Currency.getInstance("PLN")));
        assertThat(result.firstYearInterest).isEqualTo(new BigDecimal("1.70"));
        assertThat(result.followingYearsInterestMargin).isEqualTo(new BigDecimal("1.00"));
        assertThat(result.earlyRedemptionPrice).isEqualTo(new Money(new BigDecimal("2.00"), Currency.getInstance("PLN")));
    }

    @Test
    void shouldRetrieveEDO0235BondSeries() {
        // when
        BondSeries result = defaultBondProvider.getBondSeries("EDO0235");

        // then
        assertThat(result).isNotNull();
        assertThat(result.series).isEqualTo("EDO0235");
        assertThat(result.unitPrice).isEqualTo(new Money(new BigDecimal("100.00"), Currency.getInstance("PLN")));
        assertThat(result.firstYearInterest).isEqualTo(new BigDecimal("6.55"));
        assertThat(result.followingYearsInterestMargin).isEqualTo(new BigDecimal("2.00"));
        assertThat(result.earlyRedemptionPrice).isEqualTo(new Money(new BigDecimal("3.00"), Currency.getInstance("PLN")));
    }

//    @Test
//    void shouldThrowIllegalArgumentExceptionWhenPageContentCannotBeRetrieved() {
//        // given
//        String nonExistentSeries = "EDO9999";
//
//        // when/then
//        assertThatThrownBy(() -> defaultBondProvider.getBondSeries(nonExistentSeries))
//                .isInstanceOf(IllegalArgumentException.class)
//                .hasMessageContaining("Error retrieving page content: HTTP error fetching URL. Status=404, URL=[https://www.obligacjeskarbowe.pl/oferta-obligacji/obligacje-10-letnie-edo/EDO9999/]");
//    }


}