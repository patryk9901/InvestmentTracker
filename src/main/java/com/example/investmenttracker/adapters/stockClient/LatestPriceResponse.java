package com.example.investmenttracker.adapters.stockClient;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Map;

@ToString
@Getter
@Setter

class LatestPriceResponse {
    @JsonProperty("Global Quote")
    private Map<String,String> globalQuote;


}

