// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.math.stat.univariate;

public interface StorelessUnivariateStatistic extends UnivariateStatistic
{
    void increment(final double p0);
    
    double getResult();
    
    double getN();
    
    void clear();
}
