package com.example.demo.simple.stock.exception;

/**
 * @author devendra.nalawade on 8/10/17
 */
public class StockMarketException extends AssertionError {
    public StockMarketException() {
    }

    public StockMarketException(String message) {
        super(message);
    }

    public StockMarketException(String message, Throwable cause) {
        super(message, cause);
    }

    public StockMarketException(Throwable cause) {
        super(cause);
    }
}
