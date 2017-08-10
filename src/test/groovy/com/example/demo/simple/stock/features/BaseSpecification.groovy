package com.example.demo.simple.stock.features

import com.example.demo.simple.stock.factory.BeanFactory
import com.example.demo.simple.stock.repository.TradeTransactionFacade
import com.example.demo.simple.stock.service.StockService
import com.example.demo.simple.stock.service.model.StockFactory
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Subject

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


}
