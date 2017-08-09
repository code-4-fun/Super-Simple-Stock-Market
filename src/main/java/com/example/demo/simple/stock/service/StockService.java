package com.example.demo.simple.stock.service;

import com.example.demo.simple.stock.service.model.Stock;
import com.example.demo.simple.stock.service.model.Trade;

import java.math.BigDecimal;

public interface StockService {

    BigDecimal dividendYield(BigDecimal price);

    BigDecimal priceToEarningRatio(BigDecimal price);

    Trade buyStock(Trade trade);

    Trade sellStock(Stock stock);

    BigDecimal volumeWeightedStockPrice(final Stock stock);

    BigDecimal gbceAllShareIndex();

}