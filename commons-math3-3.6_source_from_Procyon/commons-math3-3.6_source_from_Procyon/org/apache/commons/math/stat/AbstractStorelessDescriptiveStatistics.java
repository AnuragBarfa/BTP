// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.math.stat;

import org.apache.commons.math.stat.univariate.UnivariateStatistic;
import org.apache.commons.math.stat.univariate.moment.ThirdMoment;
import org.apache.commons.math.stat.univariate.moment.SecondMoment;
import org.apache.commons.math.stat.univariate.moment.FirstMoment;
import org.apache.commons.math.stat.univariate.moment.Kurtosis;
import org.apache.commons.math.stat.univariate.moment.Skewness;
import org.apache.commons.math.stat.univariate.moment.Variance;
import org.apache.commons.math.stat.univariate.moment.Mean;
import org.apache.commons.math.stat.univariate.moment.GeometricMean;
import org.apache.commons.math.stat.univariate.summary.SumOfLogs;
import org.apache.commons.math.stat.univariate.rank.Max;
import org.apache.commons.math.stat.univariate.rank.Min;
import org.apache.commons.math.stat.univariate.summary.SumOfSquares;
import org.apache.commons.math.stat.univariate.summary.Sum;
import org.apache.commons.math.stat.univariate.moment.FourthMoment;

public abstract class AbstractStorelessDescriptiveStatistics extends DescriptiveStatistics
{
    protected int windowSize;
    protected int n;
    protected FourthMoment moment;
    protected Sum sum;
    protected SumOfSquares sumsq;
    protected Min min;
    protected Max max;
    protected SumOfLogs sumLog;
    protected GeometricMean geoMean;
    protected Mean mean;
    protected Variance variance;
    protected Skewness skewness;
    protected Kurtosis kurtosis;
    
    public AbstractStorelessDescriptiveStatistics() {
        this.windowSize = -1;
        this.n = 0;
        this.moment = null;
        this.sum = null;
        this.sumsq = null;
        this.min = null;
        this.max = null;
        this.sumLog = null;
        this.geoMean = null;
        this.mean = null;
        this.variance = null;
        this.skewness = null;
        this.kurtosis = null;
        this.sum = new Sum();
        this.sumsq = new SumOfSquares();
        this.min = new Min();
        this.max = new Max();
        this.sumLog = new SumOfLogs();
        this.geoMean = new GeometricMean();
        this.moment = new FourthMoment();
        this.mean = new Mean(this.moment);
        this.variance = new Variance(this.moment);
        this.skewness = new Skewness(this.moment);
        this.kurtosis = new Kurtosis(this.moment);
    }
    
    public AbstractStorelessDescriptiveStatistics(final int window) {
        this();
        this.setWindowSize(window);
    }
    
    public abstract double apply(final UnivariateStatistic p0);
    
    public abstract void addValue(final double p0);
    
    public int getN() {
        return this.n;
    }
    
    public double getSum() {
        return this.apply(this.sum);
    }
    
    public double getSumsq() {
        return this.apply(this.sumsq);
    }
    
    public double getMean() {
        return this.apply(this.mean);
    }
    
    public double getStandardDeviation() {
        double stdDev = Double.NaN;
        if (this.getN() > 0) {
            if (this.getN() > 1) {
                stdDev = Math.sqrt(this.getVariance());
            }
            else {
                stdDev = 0.0;
            }
        }
        return stdDev;
    }
    
    public double getVariance() {
        return this.apply(this.variance);
    }
    
    public double getSkewness() {
        return this.apply(this.skewness);
    }
    
    public double getKurtosis() {
        return this.apply(this.kurtosis);
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
        return this.apply(this.max);
    }
    
    public double getMin() {
        return this.apply(this.min);
    }
    
    public double getGeometricMean() {
        return this.apply(this.geoMean);
    }
    
    public String toString() {
        final StringBuffer outBuffer = new StringBuffer();
        outBuffer.append("UnivariateImpl:\n");
        outBuffer.append("n: " + this.n + "\n");
        outBuffer.append("min: " + this.min + "\n");
        outBuffer.append("max: " + this.max + "\n");
        outBuffer.append("mean: " + this.getMean() + "\n");
        outBuffer.append("std dev: " + this.getStandardDeviation() + "\n");
        outBuffer.append("skewness: " + this.getSkewness() + "\n");
        outBuffer.append("kurtosis: " + this.getKurtosis() + "\n");
        return outBuffer.toString();
    }
    
    public void clear() {
        this.n = 0;
        this.min.clear();
        this.max.clear();
        this.sum.clear();
        this.sumLog.clear();
        this.sumsq.clear();
        this.geoMean.clear();
        this.moment.clear();
        this.mean.clear();
        this.variance.clear();
        this.skewness.clear();
        this.kurtosis.clear();
    }
    
    public int getWindowSize() {
        return this.windowSize;
    }
    
    public void setWindowSize(final int windowSize) {
        this.clear();
        this.windowSize = windowSize;
    }
}
