package com.gitlab.emradbuba.lpnk.baseapi.reader.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Singular;

import java.util.List;

@Builder
@Getter
public class PriceListTableHtmlElements {
    @Singular
    private List<PriceListTableHtmlElement> houseHeaderElements;
    @Singular
    private List<PriceListTableHtmlElement> boatInfoElements;
    @Singular
    private List<PriceListTableHtmlElement> lakeDistanceElements;
    @Singular
    private List<PriceListTableHtmlElement> priceItemElements;
    @Singular
    private List<PriceListTableHtmlElement> rentalPeriodsElements;

    // Jak ma wygladac struktura danych. Mongo page?
    // Etapy parsowania tej klasy z listami, do kolejnej (kolejnych?) aż do postaci do bazy
}
