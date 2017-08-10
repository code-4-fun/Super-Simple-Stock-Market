package com.example.demo.simple.stock.exception;

/**
 * Dedicated {@link ExceptionHandler} which knows how to treat Exceptions with Respect!
 *
 * @author devendra.nalawade on 8/10/17
 */
public final class ExceptionHandler {

    public static void handleExceptions(StockMarketException exception, boolean reThrowException) throws StockMarketException {
        // log exceptions and re-throw them as - StockMarketException
        System.err.println("Exception Occurred:");
        exception.printStackTrace();
        if (reThrowException) {
            throw new StockMarketException(exception);
        }
    }

}
