package com.example.demo.simple.stock.features

import com.example.demo.simple.stock.factory.BeanFactory
import com.example.demo.simple.stock.repository.TradeTransactionFacade
import com.example.demo.simple.stock.service.StockService
import com.example.demo.simple.stock.service.model.StockFactory
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Subject

import java.time.Duration
import java.time.Instant

import static com.example.demo.simple.stock.service.model.StockFactory.StockClassification.COMMON
import static com.example.demo.simple.stock.service.model.StockFactory.StockClassification.PREFERRED

class BaseSpecification extends Specification {

    @Subject
    @Shared
    protected StockService stockService
    @Shared
    protected TradeTransactionFacade tradeTransactionFacade
    @Shared
    protected StockFactory stockFactory;

    def setupSpec() {
        def beanFactory = BeanFactory.getInstance()
        stockService = beanFactory.getStockService()
        tradeTransactionFacade = beanFactory.getTradeTransactionRepository()
        stockFactory = StockFactory.getInstance()
    }

    protected StockFactory.Stock prepStockData(stockType, symbol, lastDividend, parValue, fixedDividend) {
        StockFactory.Stock stock = null
        switch (stockType) {
            case COMMON:
                stock = stockFactory.getCommonStock(symbol, getValue(lastDividend), getValue(parValue))
                break;
            case PREFERRED:
                stock = stockFactory.getPreferredStock(symbol, getValue(lastDividend), getValue(parValue), getValue(fixedDividend))
                break;
        }
        return stock
    }

    protected getValue(data) {
        if (data == null) {
            return null
        } else {
            return BigDecimal.valueOf(data)
        }
    }

    protected void generateTradeData(stock) {
        // lets buy some stocks within the interval of 15 minutes
        stockService.buyStock(stock, Instant.now(), 10, BigDecimal.valueOf(100))
        stockService.buyStock(stock, Instant.now().minus(Duration.ofMinutes(12)), 10, BigDecimal.valueOf(100))
        stockService.buyStock(stock, Instant.now().minus(Duration.ofMinutes(1)), 10, BigDecimal.valueOf(100))
        stockService.buyStock(stock, Instant.now().minus(Duration.ofMinutes(3)), 10, BigDecimal.valueOf(100))
        stockService.buyStock(stock, Instant.now().minus(Duration.ofMinutes(14)), 10, BigDecimal.valueOf(100))
        stockService.buyStock(stock, Instant.now().minus(Duration.ofMinutes(8)), 10, BigDecimal.valueOf(100))

        // and sell a few within the interval of 15 minutes
        stockService.sellStock(stock, Instant.now(), 10, BigDecimal.valueOf(100))
        stockService.sellStock(stock, Instant.now().minus(Duration.ofMinutes(2)), 10, BigDecimal.valueOf(100))
        stockService.sellStock(stock, Instant.now().minus(Duration.ofMinutes(10)), 10, BigDecimal.valueOf(100))
        stockService.sellStock(stock, Instant.now().minus(Duration.ofMinutes(13)), 10, BigDecimal.valueOf(100))
        stockService.sellStock(stock, Instant.now().minus(Duration.ofMinutes(4)), 10, BigDecimal.valueOf(100))
        stockService.sellStock(stock, Instant.now().minus(Duration.ofMinutes(8)), 10, BigDecimal.valueOf(100))

        // these transactions will be outside 15 minutes window
        stockService.buyStock(stock, Instant.now().minus(Duration.ofMinutes(30)), 10, BigDecimal.valueOf(100))
        stockService.buyStock(stock, Instant.now().minus(Duration.ofMinutes(16)), 10, BigDecimal.valueOf(100))
        stockService.sellStock(stock, Instant.now().minus(Duration.ofMinutes(17)), 10, BigDecimal.valueOf(100))
        stockService.sellStock(stock, Instant.now().minus(Duration.ofMinutes(20)), 10, BigDecimal.valueOf(100))

        println "Number of Transactions recorded are - ${tradeTransactionFacade.getAllRecordedTradeTransactions().count()}"
    }


}
