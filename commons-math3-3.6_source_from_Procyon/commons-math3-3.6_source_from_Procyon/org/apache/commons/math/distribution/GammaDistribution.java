// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.math.distribution;

public interface GammaDistribution extends ContinuousDistribution
{
    void setAlpha(final double p0);
    
    double getAlpha();
    
    void setBeta(final double p0);
    
    double getBeta();
}
