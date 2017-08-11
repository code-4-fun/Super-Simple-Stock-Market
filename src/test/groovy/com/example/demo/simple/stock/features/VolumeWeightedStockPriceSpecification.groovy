package com.example.demo.simple.stock.features

import com.example.demo.simple.stock.service.model.StockFactory.Stock
import spock.lang.Shared
import spock.lang.Title
import spock.lang.Unroll

import java.time.Duration
import java.time.Instant

@Title("Specifications for Volume Weighted Price Calculations")
class VolumeWeightedStockPriceSpecification extends BaseSpecification {

    @Shared
    private Stock stock;

    def setupSpec() {
        stock = stockFactory.getCommonStock("TEST", BigDecimal.TEN, BigDecimal.valueOf(100))
        generateTradeData(stock)
    }

    @Unroll
    def "[ #caseId ] Calculate Volume Weighted Price for #stock and #interval"(caseId, stockOption, interval, expected) {
        when:
        BigDecimal result = stockService.volumeWeightedStockPriceInInterval(stockOption, interval)

        then:
        expected == result

        where:
        caseId | stockOption | interval | expected
        1      | stock       | 15       | 100
        2      | stock       | 10       | 100
    }

}
