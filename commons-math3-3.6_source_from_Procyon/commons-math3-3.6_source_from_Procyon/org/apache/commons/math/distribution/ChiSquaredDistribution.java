// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.math.distribution;

public interface ChiSquaredDistribution extends ContinuousDistribution
{
    void setDegreesOfFreedom(final double p0);
    
    double getDegreesOfFreedom();
}
