package com.example.demo.simple.stock.service.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.Instant;

@Getter
@AllArgsConstructor
public class Trade {

    private final Stock stock;
    private final Instant timestamp;
    private final int quantity;
    private final TradeIndicator tradeIndicator;
    private final BigDecimal price;

    public enum TradeIndicator {
        BUY, SELL
    }

}
