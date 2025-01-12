package com.example.investmenttracker.adapters.gusclient;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
@AllArgsConstructor
@Getter

class CPIResponse {
    @SerializedName("id-pozycja-2")
    long idPozycja2;
    @SerializedName("id-okres")
    int idOkres;
    @SerializedName("wartosc")
    BigDecimal wartosc;
}
