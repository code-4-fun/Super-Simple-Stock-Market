package com.example.demo.simple.stock.features

import com.example.demo.simple.stock.service.model.StockFactory
import spock.lang.Shared
import spock.lang.Title

@Title("Specification for calculating GBCE All Share Index using Geometric Mean")
class GBCEAllShareIndexCalculationSpecification extends BaseSpecification {

    @Shared
    private StockFactory.Stock stock;

    def setupSpec() {
        stock = stockFactory.getCommonStock("TEST", BigDecimal.TEN, BigDecimal.valueOf(100))
        generateTradeData(stock)
    }

    def "[ 1 ] When All Share Index is calculated"() {
        when:
        BigDecimal allShareIndex = stockService.getGBCEAllShareIndex()

        then:
        allShareIndex == 100
        println "Result: ${allShareIndex}"
    }

}
