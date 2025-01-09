package com.example.investmenttracker.adapters.gusclient;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.util.HashMap;

class CPITest {

    @Test
    void shouldFetchCPIDataFromAPI() {
        CpiResponse cpiResponse = new CpiResponse();
        HashMap<Integer, BigDecimal> cpiData = cpiResponse.fetchCPIData(2020);


        assertNotNull(cpiData, "Dane z API nie powinny być null");


        assertFalse(cpiData.isEmpty(), "Dane z API nie powinny być puste");


        assertTrue(cpiData.containsKey(247), "Brak danych dla okresu 247 (styczeń)");
        assertTrue(cpiData.containsKey(248), "Brak danych dla okresu 248 (luty)");


        BigDecimal januaryCPI = cpiData.get(247);
        assertNotNull(januaryCPI, "Wartość CPI dla okresu 247 (styczeń) nie powinna być null");
        assertTrue(januaryCPI.compareTo(BigDecimal.ZERO) > 0, "Wartość CPI dla okresu 247 (styczeń) powinna być dodatnia");


        BigDecimal februaryCPI = cpiData.get(248);
        assertNotNull(februaryCPI, "Wartość CPI dla okresu 248 (luty) nie powinna być null");
        assertTrue(februaryCPI.compareTo(BigDecimal.ZERO) > 0, "Wartość CPI dla okresu 248 (luty) powinna być dodatnia");


        assertTrue(cpiData.containsKey(249), "Brak danych dla okresu 249 (marzec)");
        BigDecimal marchCPI = cpiData.get(249);
        assertNotNull(marchCPI, "Wartość CPI dla okresu 249 (marzec) nie powinna być null");
        assertTrue(marchCPI.compareTo(BigDecimal.ZERO) > 0, "Wartość CPI dla okresu 249 (marzec) powinna być dodatnia");


        BigDecimal expectedJanuaryCPI = new BigDecimal("100.90000");
        BigDecimal expectedFebruaryCPI = new BigDecimal("100.70000");
        BigDecimal expectedMarchCPI = new BigDecimal("100.20000");

        assertEquals(expectedJanuaryCPI, januaryCPI, "Wartość CPI dla okresu 247 (styczeń) jest niepoprawna");
        assertEquals(expectedFebruaryCPI, februaryCPI, "Wartość CPI dla okresu 248 (luty) jest niepoprawna");
        assertEquals(expectedMarchCPI, marchCPI, "Wartość CPI dla okresu 249 (marzec) jest niepoprawna");

    }
}