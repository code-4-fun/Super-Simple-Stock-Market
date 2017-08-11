package com.example.demo.simple.stock.features

import com.example.demo.simple.stock.exception.StockMarketException
import com.example.demo.simple.stock.service.model.StockFactory.Stock
import com.example.demo.simple.stock.service.model.Trade
import spock.lang.Title
import spock.lang.Unroll

import java.time.Instant

import static com.example.demo.simple.stock.service.model.StockFactory.StockClassification.COMMON
import static com.example.demo.simple.stock.service.model.StockFactory.StockClassification.PREFERRED
import static com.example.demo.simple.stock.service.model.Trade.TradeIndicator.BUY
import static com.example.demo.simple.stock.service.model.Trade.TradeIndicator.SELL
import static java.math.BigDecimal.valueOf

@Title("Specification for Stock Buy / Sell operations")
class StockServiceBuySellSpecification extends BaseSpecification {

    @Unroll
    def "[ #caseId ] When #desc"(caseId, desc, stockType, tradeIndicator, symbol, lastDividend, parValue, fixedDividend) {
        given:
        Stock stock = prepStockData(stockType, symbol, lastDividend, parValue, fixedDividend)

        when:
        Trade trade = execute(tradeIndicator, stock)

        then:
        noExceptionThrown()
        trade
        tradeTransactionFacade.getAllRecordedTradeTransactions().anyMatch { t -> trade.equals(t) }

        where:
        caseId | desc                                | stockType | tradeIndicator | symbol | lastDividend | parValue | fixedDividend
        1      | 'BUY Common Stock is performed'     | COMMON    | BUY            | 'TEST' | 10           | 60       | _
        2      | 'SELL Common Stock is performed'    | COMMON    | SELL           | 'TEST' | 10           | 60       | _
        3      | 'BUY Preferred Stock is performed'  | PREFERRED | BUY            | 'TEST' | 10           | 60       | 7
        4      | 'SELL Preferred Stock is performed' | PREFERRED | SELL           | 'TEST' | 10           | 60       | 7
    }

    @Unroll
    def "[ #caseId ] When #desc "(caseId, desc, stockType, tradeIndicator, symbol, lastDividend, parValue, fixedDividend) {
        when:
        Stock stock = prepStockData(stockType, symbol, lastDividend, parValue, fixedDividend)

        then:
        thrown(StockMarketException)

        where:
        caseId | desc                                                         | stockType | tradeIndicator | symbol | lastDividend | parValue | fixedDividend
        5      | 'BUY Common Stock is performed but lastDividend is Null'     | COMMON    | BUY            | 'TEST' | null         | 60       | _
        6      | 'SELL Common Stock is performed but lastDividend is 0'       | COMMON    | SELL           | 'TEST' | 0            | 60       | _
        7      | 'BUY Common Stock is performed but parVal is Null'           | COMMON    | BUY            | 'TEST' | 10           | null     | _
        8      | 'SELL Common Stock is performed but parVal is 0'             | COMMON    | SELL           | 'TEST' | 10           | 0        | _
        9      | 'BUY Preferred Stock is performed but lastDividend is Null'  | PREFERRED | BUY            | 'TEST' | null         | 60       | 7
        10     | 'SELL Preferred Stock is performed but lastDividend is 0'    | PREFERRED | SELL           | 'TEST' | 0            | 60       | 7
        11     | 'BUY Preferred Stock is performed but parVal is Null'        | PREFERRED | BUY            | 'TEST' | 10           | null     | 7
        12     | 'SELL Preferred Stock is performed but parVal is 0'          | PREFERRED | SELL           | 'TEST' | 10           | 0        | 7
        13     | 'BUY Preferred Stock is performed but fixedDividend is Null' | PREFERRED | BUY            | 'TEST' | 10           | 60       | null
        14     | 'SELL Preferred Stock is performed but fixedDividend is 0'   | PREFERRED | SELL           | 'TEST' | 10           | 60       | 0
    }


    private Trade execute(tradeIndicator, stock) {
        Trade trade = null
        switch (tradeIndicator) {
            case BUY:
                trade = stockService.buyStock(stock, Instant.now(), 4, valueOf(100))
                break
            case SELL:
                trade = stockService.sellStock(stock, Instant.now(), 4, valueOf(100))
                break
        }
        return trade
    }

}
