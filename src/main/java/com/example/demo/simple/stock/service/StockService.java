package com.example.demo.simple.stock.service;

import com.example.demo.simple.stock.service.model.StockFactory;
import com.example.demo.simple.stock.service.model.Trade;

import java.math.BigDecimal;
import java.time.Instant;

/**
 * Service interface to be used to perform Stock Market operations
 */
public interface StockService {

    /**
     * Routine to buy stocks. Creates a record of BUY trade in repository
     *
     * @param stock     - stock on which the transaction is being performed
     * @param timestamp - time of transaction
     * @param quantity  - quantity in which the stocks are bought
     * @param price     - price per stock
     * @return Complete Details of trade operation performed
     */
    Trade buyStock(StockFactory.Stock stock, Instant timestamp, int quantity, BigDecimal price);

    /**
     * Routine to sell stocks. Creates a record of SELL trade in repository
     *
     * @param stock     - stock on which the transaction is being performed
     * @param timestamp - time of transaction
     * @param quantity  - quantity in which the stocks are bought
     * @param price     - price per stock
     * @return Complete Details of trade operation performed
     */
    Trade sellStock(StockFactory.Stock stock, Instant timestamp, int quantity, BigDecimal price);

    /**
     * Calculates Volume Weighted Stock Price calculations within the given fixed interval of time
     *
     * @param stock             - stocks of which trade records should be considered
     * @param intervalInMinutes - interval in seconds
     * @return Calculated value of Volume Weighted Stock Price
     */
    BigDecimal volumeWeightedStockPriceInInterval(final StockFactory.Stock stock, int intervalInMinutes);

    /**
     * Calculates GBCEAllShareIndex price considering all trade transaction records in {@link com.example.demo.simple.stock.repository.TradeTransactionRepository}
     *
     * @return Calculated value of GBCEAllShareIndex price
     */
    BigDecimal getGBCEAllShareIndex();

}