// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.math.distribution;

public interface HypergeometricDistribution extends DiscreteDistribution
{
    int getNumberOfSuccesses();
    
    int getPopulationSize();
    
    int getSampleSize();
    
    void setNumberOfSuccesses(final int p0);
    
    void setPopulationSize(final int p0);
    
    void setSampleSize(final int p0);
}
