// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.math.stat;

import java.util.Arrays;
import org.apache.commons.math.stat.univariate.rank.Percentile;
import org.apache.commons.math.stat.univariate.moment.GeometricMean;
import org.apache.commons.math.stat.univariate.rank.Min;
import org.apache.commons.math.stat.univariate.rank.Max;
import org.apache.commons.math.stat.univariate.moment.Kurtosis;
import org.apache.commons.math.stat.univariate.moment.Skewness;
import org.apache.commons.math.stat.univariate.moment.Variance;
import org.apache.commons.math.stat.univariate.moment.Mean;
import org.apache.commons.math.stat.univariate.summary.SumOfSquares;
import org.apache.commons.math.stat.univariate.UnivariateStatistic;
import org.apache.commons.math.stat.univariate.summary.Sum;

public abstract class AbstractDescriptiveStatistics extends DescriptiveStatistics
{
    public AbstractDescriptiveStatistics() {
    }
    
    public AbstractDescriptiveStatistics(final int window) {
        this.setWindowSize(window);
    }
    
    public double getSum() {
        return this.apply(new Sum());
    }
    
    public double getSumsq() {
        return this.apply(new SumOfSquares());
    }
    
    public double getMean() {
        return this.apply(new Mean());
    }
    
    public double getStandardDeviation() {
        double stdDev = Double.NaN;
        if (this.getN() > 0L) {
            if (this.getN() > 1L) {
                stdDev = Math.sqrt(this.getVariance());
            }
            else {
                stdDev = 0.0;
            }
        }
        return stdDev;
    }
    
    public double getVariance() {
        return this.apply(new Variance());
    }
    
    public double getSkewness() {
        return this.apply(new Skewness());
    }
    
    public double getKurtosis() {
        return this.apply(new Kurtosis());
    }
    
    public int getKurtosisClass() {
        int kClass = DescriptiveStatistics.MESOKURTIC;
        final double kurtosis = this.getKurtosis();
        if (kurtosis > 0.0) {
            kClass = DescriptiveStatistics.LEPTOKURTIC;
        }
        else if (kurtosis < 0.0) {
            kClass = DescriptiveStatistics.PLATYKURTIC;
        }
        return kClass;
    }
    
    public double getMax() {
        return this.apply(new Max());
    }
    
    public double getMin() {
        return this.apply(new Min());
    }
    
    public double getGeometricMean() {
        return this.apply(new GeometricMean());
    }
    
    public double getPercentile(final double p) {
        return this.apply(new Percentile(p));
    }
    
    public String toString() {
        final StringBuffer outBuffer = new StringBuffer();
        outBuffer.append("UnivariateImpl:\n");
        outBuffer.append("n: " + this.getN() + "\n");
        outBuffer.append("min: " + this.getMin() + "\n");
        outBuffer.append("max: " + this.getMax() + "\n");
        outBuffer.append("mean: " + this.getMean() + "\n");
        outBuffer.append("std dev: " + this.getStandardDeviation() + "\n");
        outBuffer.append("skewness: " + this.getSkewness() + "\n");
        outBuffer.append("kurtosis: " + this.getKurtosis() + "\n");
        return outBuffer.toString();
    }
    
    public double[] getSortedValues() {
        final double[] sort = this.getValues();
        Arrays.sort(sort);
        return sort;
    }
    
    public abstract void addValue(final double p0);
    
    public abstract double[] getValues();
    
    public abstract double getElement(final int p0);
    
    public abstract double apply(final UnivariateStatistic p0);
}
