// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.math.distribution;

public interface BinomialDistribution extends DiscreteDistribution
{
    int getNumberOfTrials();
    
    double getProbabilityOfSuccess();
    
    void setNumberOfTrials(final int p0);
    
    void setProbabilityOfSuccess(final double p0);
}
