package com.example.demo.simple.stock.factory

import com.example.demo.simple.stock.repository.TradeTransactionRepository
import com.example.demo.simple.stock.service.StockServiceImpl

class BeanFactory {

    private static final BeanFactory INSTANCE = new BeanFactory();

    def static getInstance() {
        INSTANCE
    }

    def getTradeTransactionRepository() {
        new TradeTransactionRepository()
    }

    def getStockService() {
        new StockServiceImpl(getTradeTransactionRepository())
    }

}
