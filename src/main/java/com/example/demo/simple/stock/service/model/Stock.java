package com.example.demo.simple.stock.service.model;

import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class Stock {
    private String symbol;
    private StockType stockType;
    private BigDecimal lastDividend;
    private BigDecimal parValue;

    protected Stock(String symbol, StockType stockType, BigDecimal lastDividend, BigDecimal parValue) {
        this.symbol = symbol;
        this.stockType = stockType;
        this.lastDividend = lastDividend;
        this.parValue = parValue;
    }

    @Getter
    public static class PrefferedStock extends Stock {
        private BigDecimal fixedDividend;

        public PrefferedStock(String symbol, BigDecimal lastDividend, BigDecimal parValue, BigDecimal fixedDividend) {
            super(symbol, StockType.PREFFERED, lastDividend, parValue);
            this.fixedDividend = fixedDividend;
        }
    }

    @Getter
    public static class CommonStock extends Stock {
        public CommonStock(String symbol, BigDecimal lastDividend, BigDecimal parValue) {
            super(symbol, StockType.COMMON, lastDividend, parValue);
        }
    }

    public enum StockType {
        COMMON, PREFFERED
    }

}
