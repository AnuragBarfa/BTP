// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.math.stat.univariate;

public interface UnivariateStatistic
{
    double evaluate(final double[] p0);
    
    double evaluate(final double[] p0, final int p1, final int p2);
}
