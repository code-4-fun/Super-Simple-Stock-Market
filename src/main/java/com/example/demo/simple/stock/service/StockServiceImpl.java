package com.example.demo.simple.stock.service;

import com.example.demo.simple.stock.repository.TradeTransactionFacade;
import com.example.demo.simple.stock.service.model.StockFactory;
import com.example.demo.simple.stock.service.model.Trade;
import com.example.demo.simple.stock.statistics.GeometricMeanEvaluatorCollector;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.Instant;
import java.util.function.Function;
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
        // freeze Cut-Off Time
        final Instant intervalCutOffTime = Instant.now().minus(Duration.ofMinutes(intervalInMinutes));

        final Stream<Trade> tradeStream = this.tradeTransactionFacade.getAllRecordedTradeTransactions();

        BigDecimal priceAndQuantitySum = tradeStream.parallel()
                .filter(trade -> trade.getTimestamp().isAfter(intervalCutOffTime))
                .filter(trade -> trade.getStock().getSymbol().equals(stock.getSymbol()))
                .map(trade -> trade.getPrice().multiply(BigDecimal.valueOf(trade.getQuantity())))
                .reduce(BigDecimal::add).orElse(BigDecimal.ZERO);

        if (priceAndQuantitySum.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }

        int totalQuantity = tradeStream.mapToInt(Trade::getQuantity).sum();

        return returnPriceValueAsCurrency.apply(priceAndQuantitySum.divide(BigDecimal.valueOf(totalQuantity), stock.getPRECISION()));
    }

    @Override
    public BigDecimal getGBCEAllShareIndex() {
        final Stream<Trade> tradeStream = this.tradeTransactionFacade.getAllRecordedTradeTransactions();

        BigDecimal geometricMean = tradeStream.parallel()
                .map(Trade::getPrice)
                .map(BigDecimal::doubleValue)
                .filter(price -> (price > 0))
                .collect(new GeometricMeanEvaluatorCollector());

        return returnPriceValueAsCurrency.apply(geometricMean);
    }
}
