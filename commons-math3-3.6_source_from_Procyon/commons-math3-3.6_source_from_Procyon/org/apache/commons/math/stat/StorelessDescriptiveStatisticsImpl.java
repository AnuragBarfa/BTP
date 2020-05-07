// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.math.stat;

import org.apache.commons.math.stat.univariate.StorelessUnivariateStatistic;
import org.apache.commons.math.stat.univariate.UnivariateStatistic;
import org.apache.commons.math.util.FixedDoubleArray;
import java.io.Serializable;

public class StorelessDescriptiveStatisticsImpl extends AbstractStorelessDescriptiveStatistics implements Serializable
{
    private FixedDoubleArray storage;
    
    public StorelessDescriptiveStatisticsImpl() {
        this.storage = null;
    }
    
    public StorelessDescriptiveStatisticsImpl(final int window) {
        super(window);
        this.storage = null;
        this.storage = new FixedDoubleArray(window);
    }
    
    public void addValue(final double value) {
        if (this.storage != null) {
            if (this.getWindowSize() == super.n) {
                this.storage.addElementRolling(value);
            }
            else {
                ++super.n;
                this.storage.addElement(value);
            }
        }
        else {
            ++super.n;
            super.min.increment(value);
            super.max.increment(value);
            super.sum.increment(value);
            super.sumsq.increment(value);
            super.sumLog.increment(value);
            super.geoMean.increment(value);
            super.moment.increment(value);
        }
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
    
    public void clear() {
        super.clear();
        if (this.getWindowSize() != -1) {
            this.storage = new FixedDoubleArray(this.getWindowSize());
        }
    }
    
    public double apply(final UnivariateStatistic stat) {
        if (this.storage != null) {
            return stat.evaluate(this.storage.getValues(), this.storage.start(), this.storage.getNumElements());
        }
        if (stat instanceof StorelessUnivariateStatistic) {
            return ((StorelessUnivariateStatistic)stat).getResult();
        }
        return Double.NaN;
    }
    
    public double[] getValues() {
        throw new UnsupportedOperationException("Only Available with Finite Window");
    }
    
    public double[] getSortedValues() {
        throw new UnsupportedOperationException("Only Available with Finite Window");
    }
    
    public double getElement(final int index) {
        throw new UnsupportedOperationException("Only Available with Finite Window");
    }
    
    public double getPercentile(final double p) {
        throw new UnsupportedOperationException("Only Available with Finite Window");
    }
}
