// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.math.stat.univariate;

public abstract class AbstractStorelessUnivariateStatistic extends AbstractUnivariateStatistic implements StorelessUnivariateStatistic
{
    public double evaluate(final double[] values, final int begin, final int length) {
        if (this.test(values, begin, length)) {
            this.clear();
            for (int l = begin + length, i = begin; i < l; ++i) {
                this.increment(values[i]);
            }
        }
        return this.getResult();
    }
    
    public abstract void clear();
    
    public abstract double getResult();
    
    public abstract void increment(final double p0);
}
