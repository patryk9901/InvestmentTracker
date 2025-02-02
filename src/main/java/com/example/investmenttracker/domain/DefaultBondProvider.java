package com.example.investmenttracker.domain;

import lombok.AllArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Component;

import java.io.IOException;
@Component
@AllArgsConstructor
public class DefaultBondProvider implements BondProvider {
    GeminiAPIRequest geminiAPIRequest;

    @Override
    public BondSeries getBondSeries(String serie) {

        String pageContent = getPageContent("https://www.obligacjeskarbowe.pl/oferta-obligacji/obligacje-10-letnie-edo/"+ serie +"/");
        String generatedAnswer = geminiAPIRequest.generateContent("Mam klasę: public class BondSeries {\n" +
                "final String series;\n" +
                "final Money unitPrice;\n" +
                "final BigDecimal firstYearInterest;\n" +
                "final BigDecimal followingYearsInterestMargin;\n" +
                "final Money earlyRedemptionPrice;\n" +
                "}\n" +
                "znajdź w tekscie podane wartości gdzie: series = Seria, unitPrice = Cena sprzedaży jednej obligacji, firstYearInterest = Oprocentowaniew pierwszym rocznym okresie odsetkowym, followingYearsInterestMargin =  Oprocentowanie w kolejnych rocznych okresach odsetkowych, earlyRedemptionPrice = opłata za przedterminowy wykup\n" +
                "\n" +
                "      \n" +
                pageContent
                +
                "\n" +
                "    ");
            return BondSeriesParser.parse(generatedAnswer);
    }

    private String getPageContent(String url){
        try {
            // Connect to the URL and retrieve the document
            Document doc = Jsoup.connect(url)
                    .userAgent("Mozilla/5.0")  // Optional: set user agent to avoid blocking
                    .timeout(5000)  // Set connection timeout
                    .get();

            // Return the full text content of the page
            return doc.body().text();
        } catch (IOException e) {
            System.err.println("Error retrieving page content: " + e.getMessage());
            return "";
        }
    }
}
