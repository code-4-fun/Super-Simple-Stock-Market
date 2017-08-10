package com.example.demo.simple.stock.service.model;

import com.example.demo.simple.stock.exception.ExceptionHandler;
import com.example.demo.simple.stock.exception.StockMarketException;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.function.BiPredicate;

import lombok.Getter;

/**
 * @author devendra.nalawade on 8/10/17
 */
public final class StockFactory {

    private static final StockFactory INSTANCE = new StockFactory();

    private StockFactory() {
    }

    public static StockFactory getInstance() {
        return INSTANCE;
    }

    public CommonStock getCommonStock(String symbol, BigDecimal lastDividend, BigDecimal parValue) {
        return new CommonStock(symbol, lastDividend, parValue);
    }

    public PreferredStock getPreferredStock(String symbol, BigDecimal lastDividend, BigDecimal parValue, BigDecimal fixedDividend) {
        return new PreferredStock(symbol, lastDividend, parValue, fixedDividend);
    }

    @Getter
    public class PreferredStock extends Stock {
        private final BigDecimal fixedDividend;

        public PreferredStock(String symbol, BigDecimal lastDividend, BigDecimal parValue, BigDecimal fixedDividend) {
            super(symbol, StockClassification.PREFERRED, lastDividend, parValue);
            this.fixedDividend = fixedDividend;
            isValueGreaterThanOrEqualTo.test(fixedDividend, BigDecimal.ZERO);
        }

        @Override
        public BigDecimal dividendYield(BigDecimal price) {
            assertValueNotNullAndGreaterThanZero(price, "Stock Price");
            return formatToPercentageScale(
                    this.getFixedDividend()
                            .multiply(this.getParValue(), PRECISION)
                            .divide(price, PRECISION)
            ).stripTrailingZeros();
        }
    }

    @Getter
    public class CommonStock extends Stock {
        public CommonStock(String symbol, BigDecimal lastDividend, BigDecimal parValue) {
            super(symbol, StockClassification.COMMON, lastDividend, parValue);
        }

        @Override
        public BigDecimal dividendYield(BigDecimal price) {
            assertValueNotNullAndGreaterThanZero(price, "Stock Price");
            return formatToPercentageScale(this.getLastDividend().divide(price, PRECISION).stripTrailingZeros());
        }
    }

    @Getter
    public abstract class Stock {

        public final MathContext PRECISION = new MathContext(30, RoundingMode.HALF_EVEN);

        private final String symbol;
        private final StockClassification stockType;
        private final BigDecimal lastDividend;
        private final BigDecimal parValue;

        // validator
        protected final BiPredicate<BigDecimal, BigDecimal> isValueGreaterThanOrEqualTo = (value, number) -> ((value != null) && value.compareTo(number) >= 0);

        protected Stock(String symbol, StockClassification stockType, BigDecimal lastDividend, BigDecimal parValue) {
            this.symbol = symbol;
            this.stockType = stockType;
            this.lastDividend = lastDividend;
            this.parValue = parValue;

            assertStockParams(this);
        }

        /**
         * Calculate dividend yield on the stock for given price
         *
         * @param price - price of stock
         * @return calculated dividend yield value for the given price of the stock
         */
        public abstract BigDecimal dividendYield(BigDecimal price);

        /**
         * Calculate P/E ratio for the given price of the stock
         *
         * @param price - price of stock
         * @return calculated P/E ratio for the given stock
         */
        public BigDecimal pToERatio(BigDecimal price) {
            return formatToPercentageScale(
                    price.divide(this.getLastDividend(), PRECISION)
            ).stripTrailingZeros();
        }

        /**
         * Assert stock attributes required for calculations are greater than 0 to avoid divide by 0 exceptions
         *
         * @param stock - stock data
         */
        protected void assertStockParams(Stock stock) {
            if (isValueGreaterThanOrEqualTo.test(stock.getLastDividend(), BigDecimal.ZERO)) {
                ExceptionHandler.handleExceptions(new StockMarketException("Last Dividend should be greater than 0"), true);
            }
            if (isValueGreaterThanOrEqualTo.test(stock.getParValue(), BigDecimal.ZERO)) {
                ExceptionHandler.handleExceptions(new StockMarketException("Par Value should be greater than 0"), true);
            }
        }

        /**
         * Assert if the given value is greater than 0 else raise {@link StockMarketException}
         *
         * @param value      - value to be checked
         * @param valueLabel - Value Label to be put in error message
         */
        protected void assertValueNotNullAndGreaterThanZero(BigDecimal value, String valueLabel) {
            if (isValueGreaterThanOrEqualTo.test(value, BigDecimal.ZERO)) {
                ExceptionHandler.handleExceptions(new StockMarketException(valueLabel + " should be greater than 0"), true);
            }
        }

        /**
         * Formats the given value into Percentage scale
         *
         * @param value
         * @return
         */
        protected BigDecimal formatToPercentageScale(BigDecimal value) {
            return value.setScale(5, BigDecimal.ROUND_HALF_EVEN);
        }
    }

    public enum StockClassification {
        COMMON, PREFERRED
    }

}
