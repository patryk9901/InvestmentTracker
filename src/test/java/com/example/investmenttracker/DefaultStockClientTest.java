package com.example.investmenttracker;

import com.example.investmenttracker.adapters.nbpclient.Money;
import com.example.investmenttracker.adapters.stockClient.DefaultStockClient;
import com.example.investmenttracker.domain.Stock;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestClient;

import java.math.BigDecimal;
import java.util.Currency;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

class DefaultStockClientTest {

    @Test
    void shouldReturnLatestPriceForStock() {


        var wireMockServer = new WireMockServer(WireMockConfiguration.wireMockConfig().port(7070));
        wireMockServer.start();
        WireMock.configureFor("localhost", wireMockServer.port());

        stubFor(get(urlEqualTo("/xd"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withBody("Hello, WireMock!")));

        var restClientBuilder = RestClient.builder();
        DefaultStockClient client = new DefaultStockClient(restClientBuilder.build());

        String mockResponse = """
        {
            "Global Quote": {
                "01. symbol": "V60A.DEX",
                "05. price": "123.45"
            }
        }
        """;

        MockRestServiceServer mockServer =
                MockRestServiceServer.bindTo(restClientBuilder).build();
        mockServer.expect(requestTo("https://www.alphavantage.co/query?function=GLOBAL_QUOTE&symbol={symbol}&apikey={apiKey}"))
                .andRespond(withSuccess(mockResponse, MediaType.APPLICATION_JSON));

        Stock stock = new Stock("V60A", "DEX", "Vanguard LifeStrategy 60%", "EUR");

        Money money = client.getLatestPrice(stock);


        assertThat(money).isEqualTo(new Money(BigDecimal.valueOf(123.45), Currency.getInstance("EUR")));


//        verify(restClientMock).get();
//        verify(requestHeadersUriSpecMock).uri(anyString(), any(), any());
//        verify(requestHeadersSpecMock).retrieve();
//        verify(responseSpecMock).body(String.class);
    }
}