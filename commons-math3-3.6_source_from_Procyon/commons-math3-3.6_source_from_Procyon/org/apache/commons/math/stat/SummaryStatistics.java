// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.math.stat;

import org.apache.commons.discovery.tools.DiscoverClass;
import java.io.Serializable;

public abstract class SummaryStatistics implements Serializable, StatisticalSummary
{
    public static SummaryStatistics newInstance(final String cls) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        return newInstance(Class.forName(cls));
    }
    
    public static SummaryStatistics newInstance(final Class cls) throws InstantiationException, IllegalAccessException {
        return cls.newInstance();
    }
    
    public static SummaryStatistics newInstance() {
        SummaryStatistics factory = null;
        try {
            final DiscoverClass dc = new DiscoverClass();
            factory = (SummaryStatistics)dc.newInstance(SummaryStatistics.class, "org.apache.commons.math.stat.SummaryStatisticsImpl");
        }
        catch (Exception ex) {}
        return factory;
    }
    
    public abstract void addValue(final double p0);
    
    public abstract double getMean();
    
    public abstract double getGeometricMean();
    
    public abstract double getVariance();
    
    public abstract double getStandardDeviation();
    
    public abstract double getMax();
    
    public abstract double getMin();
    
    public abstract long getN();
    
    public abstract double getSum();
    
    public abstract double getSumsq();
    
    public abstract void clear();
}
