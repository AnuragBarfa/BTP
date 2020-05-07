// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.math.stat.univariate.rank;

import java.io.Serializable;
import org.apache.commons.math.stat.univariate.AbstractStorelessUnivariateStatistic;

public class Min extends AbstractStorelessUnivariateStatistic implements Serializable
{
    static final long serialVersionUID = -2941995784909003131L;
    private long n;
    private double value;
    
    public Min() {
        this.n = 0L;
        this.value = Double.NaN;
    }
    
    public void increment(final double d) {
        this.value = (Double.isNaN(this.value) ? d : Math.min(this.value, d));
        ++this.n;
    }
    
    public void clear() {
        this.value = Double.NaN;
        this.n = 0L;
    }
    
    public double getResult() {
        return this.value;
    }
    
    public double getN() {
        return (double)this.n;
    }
    
    public double evaluate(final double[] values, final int begin, final int length) {
        double min = Double.NaN;
        if (this.test(values, begin, length)) {
            min = values[begin];
            for (int i = begin; i < begin + length; ++i) {
                min = ((min < values[i]) ? min : values[i]);
            }
        }
        return min;
    }
}
