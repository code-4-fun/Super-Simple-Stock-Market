package com.example.demo.simple.stock.service;

import com.example.demo.simple.stock.exception.ExceptionHandler;
import com.example.demo.simple.stock.exception.StockMarketException;
import com.example.demo.simple.stock.repository.TradeTransactionFacade;
import com.example.demo.simple.stock.service.model.StockFactory;
import com.example.demo.simple.stock.service.model.Trade;
import com.example.demo.simple.stock.statistics.GeometricMeanEvaluatorCollector;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;
import java.util.stream.Stream;

/**
 * @author devendra.nalawade on 8/10/17
 */
public class StockServiceImpl implements StockService {

    private TradeTransactionFacade tradeTransactionFacade;

    /**
     * {@link Function} to map calculated BigDecimal price value as currency
     */
    private final Function<BigDecimal, BigDecimal> returnPriceValueAsCurrency = (price) -> price.setScale(2, BigDecimal.ROUND_HALF_EVEN);

    public StockServiceImpl(TradeTransactionFacade tradeTransactionFacade) {
        this.tradeTransactionFacade = tradeTransactionFacade;
    }

    @Override
    public Trade buyStock(StockFactory.Stock stock, Instant timestamp, int quantity, BigDecimal price) {
        return this.tradeTransactionFacade.recordTradeTransaction(stock, Trade.TradeIndicator.BUY, quantity, price, timestamp);
    }

    @Override
    public Trade sellStock(StockFactory.Stock stock, Instant timestamp, int quantity, BigDecimal price) {
        return this.tradeTransactionFacade.recordTradeTransaction(stock, Trade.TradeIndicator.SELL, quantity, price, timestamp);
    }

    @Override
    public BigDecimal volumeWeightedStockPriceInInterval(StockFactory.Stock stock, int intervalInMinutes) {

        if (stock == null || intervalInMinutes < 0) {
            ExceptionHandler.handleExceptions(new StockMarketException("Stock information and time interval (with value greater than 0 are mandatory)"), true);
        }

        // freeze Cut-Off Time
        final Instant intervalCutOffTime = Instant.now().minus(Duration.ofMinutes(intervalInMinutes));

        final Stream<Trade> tradeStream = this.tradeTransactionFacade.getAllRecordedTradeTransactions();

        AtomicInteger quantityTotal = new AtomicInteger(0);

        BigDecimal priceAndQuantitySum = tradeStream.parallel()
                .filter(trade -> trade.getTimestamp().isAfter(intervalCutOffTime))
                .filter(trade -> trade.getStock().getSymbol().equals(stock.getSymbol()))
                .map(trade -> {
                    quantityTotal.addAndGet(trade.getQuantity());
                    return trade.getPrice().multiply(BigDecimal.valueOf(trade.getQuantity()));
                })
                .reduce(BigDecimal::add).orElse(BigDecimal.ZERO);

        if (priceAndQuantitySum.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }

        //int totalQuantity = tradeStream.mapToInt(Trade::getQuantity).sum();
        return returnPriceValueAsCurrency.apply(priceAndQuantitySum.divide(BigDecimal.valueOf(quantityTotal.get()), stock.getPRECISION()));
    }

    @Override
    public BigDecimal getGBCEAllShareIndex() {
        final Stream<Trade> tradeStream = this.tradeTransactionFacade.getAllRecordedTradeTransactions();

        BigDecimal geometricMean = tradeStream.map(Trade::getPrice)
                .map(BigDecimal::doubleValue)
                .filter(price -> (price > 0))
                .collect(new GeometricMeanEvaluatorCollector());

        return returnPriceValueAsCurrency.apply(geometricMean);
    }
}
