package com.example.demo.simple.stock.exception;

/**
 * Dedicated {@link ExceptionHandler} which knows how to treat Exceptions with Respect!
 *
 * @author devendra.nalawade on 8/10/17
 */
public final class ExceptionHandler {

    public static void handleExceptions(Throwable throwable, boolean reThrowException) throws AssertionError {
        // log exceptions and re-throw them as - StockMarketException
        System.err.println("Exception Occurred:");
        throwable.printStackTrace();
        if (reThrowException) {
            throw new AssertionError(throwable);
        }
    }

}
