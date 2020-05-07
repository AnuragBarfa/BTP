// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.math.distribution;

public interface FDistribution extends ContinuousDistribution
{
    void setNumeratorDegreesOfFreedom(final double p0);
    
    double getNumeratorDegreesOfFreedom();
    
    void setDenominatorDegreesOfFreedom(final double p0);
    
    double getDenominatorDegreesOfFreedom();
}
