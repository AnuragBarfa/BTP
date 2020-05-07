// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.math.stat;

public interface StatisticalSummary
{
    double getMean();
    
    double getVariance();
    
    double getStandardDeviation();
    
    double getMax();
    
    double getMin();
    
    long getN();
    
    double getSum();
}
