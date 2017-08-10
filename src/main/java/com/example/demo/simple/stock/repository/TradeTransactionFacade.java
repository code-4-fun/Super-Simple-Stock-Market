package com.example.demo.simple.stock.repository;

import com.example.demo.simple.stock.service.model.StockFactory;
import com.example.demo.simple.stock.service.model.Trade;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.stream.Stream;

/**
 * Facade which represents various repository specific transactions. Underlying implementation may vary from In-Memory collection, File based data to DB entities.
 *
 * @author devendra.nalawade on 8/10/17
 */
public interface TradeTransactionFacade {

    /**
     * Records a trade transaction in repository.
     * <i>(Note: Timestamp of this transactions will be recorded as current time)</i>
     *
     * @param stock      - stock in consideration
     * @param indicator  - {@link com.example.demo.simple.stock.service.model.Trade.TradeIndicator} to denote BUY / SELL transactions
     * @param quantity   - quantity of stocks being operated
     * @param stockPrice - price per stock
     * @return - Trade record after storing in repository
     */
    Trade recordTradeTransaction(StockFactory.Stock stock, Trade.TradeIndicator indicator, int quantity, BigDecimal stockPrice);

    /**
     * Records a trade transaction in repository.
     *
     * @param stock      - stock in consideration
     * @param indicator  - {@link com.example.demo.simple.stock.service.model.Trade.TradeIndicator} to denote BUY / SELL transactions
     * @param quantity   - quantity of stocks being operated
     * @param stockPrice - price per stock
     * @param timestamp  - timestamp of transaction
     * @return - Trade record after storing in repository
     */
    Trade recordTradeTransaction(StockFactory.Stock stock, Trade.TradeIndicator indicator, int quantity, BigDecimal stockPrice, Instant timestamp);

    /**
     * Get All transaction records stored in repository in the form of stream.
     *
     * @return Stream of Trade transaction records
     */
    Stream<Trade> getAllRecordedTradeTransactions();

}
