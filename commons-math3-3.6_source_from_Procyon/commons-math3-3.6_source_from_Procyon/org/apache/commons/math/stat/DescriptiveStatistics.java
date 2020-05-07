// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.math.stat;

import org.apache.commons.math.stat.univariate.UnivariateStatistic;
import org.apache.commons.discovery.tools.DiscoverClass;
import java.io.Serializable;

public abstract class DescriptiveStatistics implements Serializable, StatisticalSummary
{
    public static final int INFINITE_WINDOW = -1;
    public static int LEPTOKURTIC;
    public static int MESOKURTIC;
    public static int PLATYKURTIC;
    
    public static DescriptiveStatistics newInstance(final String cls) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        return newInstance(Class.forName(cls));
    }
    
    public static DescriptiveStatistics newInstance(final Class cls) throws InstantiationException, IllegalAccessException {
        return cls.newInstance();
    }
    
    public static DescriptiveStatistics newInstance() {
        DescriptiveStatistics factory = null;
        try {
            final DiscoverClass dc = new DiscoverClass();
            factory = (DescriptiveStatistics)dc.newInstance(DescriptiveStatistics.class, "org.apache.commons.math.stat.DescriptiveStatisticsImpl");
        }
        catch (Exception ex) {}
        return factory;
    }
    
    public abstract void addValue(final double p0);
    
    public abstract double getMean();
    
    public abstract double getGeometricMean();
    
    public abstract double getVariance();
    
    public abstract double getStandardDeviation();
    
    public abstract double getSkewness();
    
    public abstract double getKurtosis();
    
    public abstract int getKurtosisClass();
    
    public abstract double getMax();
    
    public abstract double getMin();
    
    public abstract long getN();
    
    public abstract double getSum();
    
    public abstract double getSumsq();
    
    public abstract void clear();
    
    public abstract int getWindowSize();
    
    public abstract void setWindowSize(final int p0);
    
    public abstract double[] getValues();
    
    public abstract double[] getSortedValues();
    
    public abstract double getElement(final int p0);
    
    public abstract double getPercentile(final double p0);
    
    public abstract double apply(final UnivariateStatistic p0);
    
    static {
        DescriptiveStatistics.LEPTOKURTIC = 1;
        DescriptiveStatistics.MESOKURTIC = 0;
        DescriptiveStatistics.PLATYKURTIC = -1;
    }
}
