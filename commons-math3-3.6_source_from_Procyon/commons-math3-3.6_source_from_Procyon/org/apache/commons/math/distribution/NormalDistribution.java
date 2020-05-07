// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.math.distribution;

public interface NormalDistribution extends ContinuousDistribution
{
    double getMean();
    
    void setMean(final double p0);
    
    double getStandardDeviation();
    
    void setStandardDeviation(final double p0);
    
    NormalCDFAlgorithm getCdfAlgorithm();
    
    void setCdfAlgorithm(final NormalCDFAlgorithm p0);
}
