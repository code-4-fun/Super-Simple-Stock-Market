package com.example.demo.simple.stock.repository;

import com.example.demo.simple.stock.service.model.StockFactory;
import com.example.demo.simple.stock.service.model.Trade;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.HashSet;
import java.util.UUID;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.stream.Stream;

/**
 * @author devendra.nalawade on 8/10/17
 */
public class TradeTransactionRepository implements TradeTransactionFacade {

    private static ConcurrentSkipListSet<Trade> tradeCollection = new ConcurrentSkipListSet<>();

    @Override
    public Trade recordTradeTransaction(StockFactory.Stock stock, Trade.TradeIndicator indicator, int quantity, BigDecimal stockPrice) {
        return recordTradeTransaction(stock, indicator, quantity, stockPrice, Instant.now());
    }

    @Override
    public Trade recordTradeTransaction(StockFactory.Stock stock, Trade.TradeIndicator indicator, int quantity, BigDecimal stockPrice, Instant timestamp) {
        Trade record = new Trade(UUID.randomUUID(), stock, timestamp, quantity, indicator, stockPrice);
        tradeCollection.add(record);
        return record;
    }

    @Override
    public Stream<Trade> getAllRecordedTradeTransactions() {
        // capture state of repository at this instance, doesn't need to be thread-safe here
        final HashSet<Trade> data = new HashSet<>(tradeCollection);
        return data.stream();
    }

}
