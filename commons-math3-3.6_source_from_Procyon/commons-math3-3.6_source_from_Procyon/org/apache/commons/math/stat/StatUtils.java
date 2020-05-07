// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.math.stat;

import org.apache.commons.math.stat.univariate.moment.Variance;
import org.apache.commons.math.stat.univariate.moment.Mean;
import org.apache.commons.math.stat.univariate.rank.Max;
import org.apache.commons.math.stat.univariate.rank.Min;
import org.apache.commons.math.stat.univariate.summary.SumOfLogs;
import org.apache.commons.math.stat.univariate.summary.Product;
import org.apache.commons.math.stat.univariate.summary.SumOfSquares;
import org.apache.commons.math.stat.univariate.summary.Sum;
import org.apache.commons.math.stat.univariate.rank.Percentile;
import org.apache.commons.math.stat.univariate.UnivariateStatistic;

public final class StatUtils
{
    private static UnivariateStatistic sum;
    private static UnivariateStatistic sumSq;
    private static UnivariateStatistic prod;
    private static UnivariateStatistic sumLog;
    private static UnivariateStatistic min;
    private static UnivariateStatistic max;
    private static UnivariateStatistic mean;
    private static UnivariateStatistic variance;
    private static Percentile percentile;
    
    private StatUtils() {
    }
    
    public static double sum(final double[] values) {
        return StatUtils.sum.evaluate(values);
    }
    
    public static double sum(final double[] values, final int begin, final int length) {
        return StatUtils.sum.evaluate(values, begin, length);
    }
    
    public static double sumSq(final double[] values) {
        return StatUtils.sumSq.evaluate(values);
    }
    
    public static double sumSq(final double[] values, final int begin, final int length) {
        return StatUtils.sumSq.evaluate(values, begin, length);
    }
    
    public static double product(final double[] values) {
        return StatUtils.prod.evaluate(values);
    }
    
    public static double product(final double[] values, final int begin, final int length) {
        return StatUtils.prod.evaluate(values, begin, length);
    }
    
    public static double sumLog(final double[] values) {
        return StatUtils.sumLog.evaluate(values);
    }
    
    public static double sumLog(final double[] values, final int begin, final int length) {
        return StatUtils.sumLog.evaluate(values, begin, length);
    }
    
    public static double mean(final double[] values) {
        return StatUtils.mean.evaluate(values);
    }
    
    public static double mean(final double[] values, final int begin, final int length) {
        return StatUtils.mean.evaluate(values, begin, length);
    }
    
    public static double variance(final double[] values) {
        return StatUtils.variance.evaluate(values);
    }
    
    public static double variance(final double[] values, final int begin, final int length) {
        return StatUtils.variance.evaluate(values, begin, length);
    }
    
    public static double max(final double[] values) {
        return StatUtils.max.evaluate(values);
    }
    
    public static double max(final double[] values, final int begin, final int length) {
        return StatUtils.max.evaluate(values, begin, length);
    }
    
    public static double min(final double[] values) {
        return StatUtils.min.evaluate(values);
    }
    
    public static double min(final double[] values, final int begin, final int length) {
        return StatUtils.min.evaluate(values, begin, length);
    }
    
    public static double percentile(final double[] values, final double p) {
        return StatUtils.percentile.evaluate(values, p);
    }
    
    public static double percentile(final double[] values, final int begin, final int length, final double p) {
        return StatUtils.percentile.evaluate(values, begin, length, p);
    }
    
    static {
        StatUtils.sum = new Sum();
        StatUtils.sumSq = new SumOfSquares();
        StatUtils.prod = new Product();
        StatUtils.sumLog = new SumOfLogs();
        StatUtils.min = new Min();
        StatUtils.max = new Max();
        StatUtils.mean = new Mean();
        StatUtils.variance = new Variance();
        StatUtils.percentile = new Percentile();
    }
}
