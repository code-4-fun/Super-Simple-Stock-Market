package com.example.demo.simple.stock.features

import com.example.demo.simple.stock.exception.StockMarketException
import com.example.demo.simple.stock.service.model.StockFactory
import spock.lang.Title
import spock.lang.Unroll

import static com.example.demo.simple.stock.service.model.StockFactory.StockClassification.COMMON
import static com.example.demo.simple.stock.service.model.StockFactory.StockClassification.PREFERRED

@Title("Specifications for calculating Stock Dividend Yield and P/E Ratio")
class StockStatisticsSpecification extends BaseSpecification {

    @Unroll
    def "[ #caseId ] Calculate Last Dividend for #desc lastDividend = #lastDividend and price = #price"(
            caseId, desc, stockType, symbol, lastDividend, parValue, fixedDividend, price, dividendYield
    ) {
        when:
        Class thrown = null
        StockFactory.Stock stock = null
        BigDecimal answerDividendYield = null
        try {
            stock = prepStockData(stockType, symbol, lastDividend, parValue, fixedDividend)
            answerDividendYield = stock.dividendYield(price)
        } catch (StockMarketException e) {
            thrown = e.class
        }

        then:
        if (dividendYield instanceof Class) {
            assert dividendYield.equals(thrown)
        } else {
            assert answerDividendYield.doubleValue() == BigDecimal.valueOf(dividendYield).doubleValue()
        }

        where:
        caseId | desc                | stockType | symbol | lastDividend | parValue | fixedDividend | price | dividendYield
        1      | "Common Stock: "    | COMMON    | "TEST" | 10           | 60       | 7             | 5     | 2.00000
        2      | "Common Stock: "    | COMMON    | "TEST" | 10           | 60       | 7             | 0     | StockMarketException
        3      | "Common Stock: "    | COMMON    | "TEST" | 10           | 60       | 7             | -2    | StockMarketException
        4      | "Common Stock: "    | COMMON    | "TEST" | 0            | 60       | 7             | 5     | StockMarketException
        5      | "Common Stock: "    | COMMON    | "TEST" | -10          | 60       | 7             | 5     | StockMarketException
        6      | "Preferred Stock: " | PREFERRED | "TEST" | 10           | 60       | 6             | 30    | 12
        7      | "Preferred Stock: " | PREFERRED | "TEST" | 10           | 60       | 6             | 0     | StockMarketException
        8      | "Preferred Stock: " | PREFERRED | "TEST" | 10           | 60       | 6             | -10   | StockMarketException
        9      | "Preferred Stock: " | PREFERRED | "TEST" | 10           | 0        | 6             | 30    | StockMarketException
        10     | "Preferred Stock: " | PREFERRED | "TEST" | 10           | -60      | 6             | 30    | StockMarketException
    }

    @Unroll
    def "[ #caseId ] Calculate P/E Ratio for #desc lastDividend = #lastDividend and price = #price"(
            caseId, desc, stockType, symbol, lastDividend, parValue, fixedDividend, price, pToERatio
    ) {
        when:
        Class exception = null
        StockFactory.Stock stock = null;
        BigDecimal answerPToERatio = null
        try {
            stock = prepStockData(stockType, symbol, lastDividend, parValue, fixedDividend)
            answerPToERatio = stock.pToERatio(price)
        } catch (StockMarketException e) {
            exception = e.class
        }

        then:
        if (pToERatio instanceof Class) {
            assert pToERatio.equals(exception)
        } else {
            assert BigDecimal.valueOf(pToERatio).doubleValue() == answerPToERatio.doubleValue()
        }

        where:
        caseId | desc                | stockType | symbol | lastDividend | parValue | fixedDividend | price | pToERatio
        11     | "Common Stock: "    | COMMON    | "TEST" | 10           | 60       | 7             | 5     | 0.5
        12     | "Common Stock: "    | COMMON    | "TEST" | 10           | 60       | 7             | 0     | StockMarketException
        13     | "Common Stock: "    | COMMON    | "TEST" | 10           | 60       | 7             | -2    | StockMarketException
        14     | "Common Stock: "    | COMMON    | "TEST" | 0            | 60       | 7             | 5     | StockMarketException
        15     | "Common Stock: "    | COMMON    | "TEST" | -10          | 60       | 7             | 5     | StockMarketException
        16     | "Preferred Stock: " | PREFERRED | "TEST" | 10           | 60       | 6             | 30    | 3
        17     | "Preferred Stock: " | PREFERRED | "TEST" | 10           | 60       | 6             | 0     | StockMarketException
        18     | "Preferred Stock: " | PREFERRED | "TEST" | 10           | 60       | 6             | -10   | StockMarketException
        19     | "Preferred Stock: " | PREFERRED | "TEST" | 10           | 0        | 6             | 30    | StockMarketException
        20     | "Preferred Stock: " | PREFERRED | "TEST" | 10           | -60      | 6             | 30    | StockMarketException
    }

}
