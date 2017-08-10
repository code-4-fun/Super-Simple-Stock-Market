package com.example.demo.simple.stock.service.model;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@AllArgsConstructor
@EqualsAndHashCode
public class Trade {

    private final UUID tradeTransactionRef;
    private final StockFactory.Stock stock;
    private final Instant timestamp;
    private final int quantity;
    private final TradeIndicator tradeIndicator;
    private final BigDecimal price;

    public enum TradeIndicator {
        BUY, SELL
    }

}
