// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.math.stat.univariate;

public abstract class AbstractUnivariateStatistic implements UnivariateStatistic
{
    public double evaluate(final double[] values) {
        return this.evaluate(values, 0, values.length);
    }
    
    public abstract double evaluate(final double[] p0, final int p1, final int p2);
    
    protected boolean test(final double[] values, final int begin, final int length) {
        if (length > values.length) {
            throw new IllegalArgumentException("length > values.length");
        }
        if (begin + length > values.length) {
            throw new IllegalArgumentException("begin + length > values.length");
        }
        if (values == null) {
            throw new IllegalArgumentException("input value array is null");
        }
        return values.length != 0 && length != 0;
    }
}
