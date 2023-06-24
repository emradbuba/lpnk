package com.gitlab.emradbuba.lpnk.baseapi.reader;

import com.gitlab.emradbuba.lpnk.baseapi.reader.model.PriceListTableHtmlElement;
import com.gitlab.emradbuba.lpnk.baseapi.reader.model.PriceListTableHtmlElements;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Optional;

@Component
@Slf4j
public final class PriceListTableHtmlElementsFinder {

    public static final String PRICE_LIST_DIV_ID = "cennik";
    @Value("${website.pricelist.url}")
    private String priceListUrl;

    public static void main(String[] args) throws IOException {
        new PriceListTableHtmlElementsFinder().findPriceListHtmlElements();
    }

    public PriceListTableHtmlElements findPriceListHtmlElements() throws IOException {
        int rowNumberHousesInfo = 1;
        int rowNumberBoatInfo = 2;
        int rowNumberLakeDistance = 3;
        int rowNumberPricesFirst = 4;
        int rowNumberPricesLast = 11;
        PriceListTableHtmlElements.PriceListTableHtmlElementsBuilder priceListTableHtmlElementsBuilder = PriceListTableHtmlElements.builder();
        log.info("Reading price list table from source url: {}", priceListUrl);
        Connection connection = Jsoup.connect("https://www.**********.pl/cennik.html");
        Document mainHtmlDocument = connection.get();
        Elements tableRowElements = Optional.of(mainHtmlDocument.getElementById(PRICE_LIST_DIV_ID))
                .map(element -> element.getElementsByTag("tr"))
                .orElseThrow(() -> new RuntimeException("Unexpected structure of price list website - no tr elements found in price list div"));
        if(tableRowElements.size() < rowNumberPricesLast + 1) {
            throw new RuntimeException("Unexpected structure of price list website - at least 12 tr elements expected");
        }
        Element trElementHousesInfo = tableRowElements.get(rowNumberHousesInfo);
        readTableRowHouseInfo(trElementHousesInfo, priceListTableHtmlElementsBuilder);
        Element trElementBoatInfo = tableRowElements.get(rowNumberBoatInfo);
        readTableRowBoatInfo(trElementBoatInfo, priceListTableHtmlElementsBuilder);
        Element trElementLakeDistance = tableRowElements.get(rowNumberLakeDistance);
        readTableRowLakeDistance(trElementLakeDistance, priceListTableHtmlElementsBuilder);
        for(int priceRowNumber = rowNumberPricesFirst; priceRowNumber <= rowNumberPricesLast; priceRowNumber++) {
            Element trElementPrice = tableRowElements.get(priceRowNumber);
            readTableRowPriceInfo(trElementPrice, priceRowNumber, priceListTableHtmlElementsBuilder);
        }
        PriceListTableHtmlElements result = priceListTableHtmlElementsBuilder.build();
        return result;
    }

    private void readTableRowPriceInfo(Element trElementPrice, int priceRowNumber, PriceListTableHtmlElements.PriceListTableHtmlElementsBuilder priceListTableHtmlElementsBuilder) {
        Elements columns = trElementPrice.getElementsByTag("td");
        priceListTableHtmlElementsBuilder.rentalPeriodsElement(PriceListTableHtmlElement.builder().rowNumber(priceRowNumber).columnNumber(0).htmlElement(columns.get(0)).build());
        for(int columnIdx = 1; columnIdx < columns.size(); columnIdx++) {
            Element td = columns.get(columnIdx);
            priceListTableHtmlElementsBuilder.priceItemElement(PriceListTableHtmlElement.builder()
                    .rowNumber(priceRowNumber)
                    .columnNumber(columnIdx)
                    .htmlElement(td)
                    .build()
            );
        }
    }

    private void readTableRowLakeDistance(Element trElementLakeDistance, PriceListTableHtmlElements.PriceListTableHtmlElementsBuilder priceListTableHtmlElementsBuilder) {
        Elements columns = trElementLakeDistance.getElementsByTag("td");
        for(int columnIdx = 1; columnIdx < columns.size(); columnIdx++) {
            Element td = columns.get(columnIdx);
            priceListTableHtmlElementsBuilder.lakeDistanceElement(PriceListTableHtmlElement.builder()
                    .rowNumber(2)
                    .columnNumber(columnIdx)
                    .htmlElement(td)
                    .build()
            );
        }
    }

    private void readTableRowBoatInfo(Element trElementBoatInfo, PriceListTableHtmlElements.PriceListTableHtmlElementsBuilder priceListTableHtmlElementsBuilder) {
        Elements columns = trElementBoatInfo.getElementsByTag("td");
        for(int columnIdx = 1; columnIdx < columns.size(); columnIdx++) {
            Element td = columns.get(columnIdx);
            priceListTableHtmlElementsBuilder.boatInfoElement(PriceListTableHtmlElement.builder()
                    .rowNumber(1)
                    .columnNumber(columnIdx)
                    .htmlElement(td)
                    .build()
            );
        }
    }

    private void readTableRowHouseInfo(Element trElementHousesInfo, PriceListTableHtmlElements.PriceListTableHtmlElementsBuilder priceListTableHtmlElementsBuilder) {
        Elements columns = trElementHousesInfo.getElementsByTag("td");
        for(int columnIdx = 0; columnIdx < columns.size(); columnIdx++) {
            Element td = columns.get(columnIdx);
            priceListTableHtmlElementsBuilder.houseHeaderElement(PriceListTableHtmlElement.builder()
                    .rowNumber(0)
                    .columnNumber(columnIdx + 1) // first column empty
                    .htmlElement(td)
                    .build()
            );
        }

    }


}
