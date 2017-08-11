package com.example.demo.simple.stock.statistics;

import org.apache.commons.math3.stat.StatUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumSet;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

/**
 * {@link Collector} which calculates Geometric Mean of records received from Stream
 *
 * @author devendra.nalawade on 8/10/17
 */
public class GeometricMeanEvaluatorCollector implements Collector<Double, ArrayList<Double>, BigDecimal> {

    private static final Set<Collector.Characteristics> CH_ID = Collections.unmodifiableSet(EnumSet.of(Collector.Characteristics.UNORDERED));

    @Override
    public Supplier<ArrayList<Double>> supplier() {
        return ArrayList::new;
    }

    @Override
    public BiConsumer<ArrayList<Double>, Double> accumulator() {
        return ArrayList::add;
    }

    @Override
    public BinaryOperator<ArrayList<Double>> combiner() {
        return (left, right) -> {
            left.addAll(right);
            return left;
        };
    }

    @Override
    public Function<ArrayList<Double>, BigDecimal> finisher() {
        return this::calculateGeometricMean;
    }

    @Override
    public Set<Characteristics> characteristics() {
        return CH_ID;
    }

    private BigDecimal calculateGeometricMean(ArrayList<Double> priceList) {
        double data[] = new double[priceList.size()];
        int index = 0;
        for (double price : priceList) {
            data[index] = price;
            index++;
        }
        return BigDecimal.valueOf(StatUtils.geometricMean(data));
    }
}
