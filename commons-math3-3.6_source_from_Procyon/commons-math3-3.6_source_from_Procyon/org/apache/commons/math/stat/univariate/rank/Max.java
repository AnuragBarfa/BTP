// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.math.stat.univariate.rank;

import java.io.Serializable;
import org.apache.commons.math.stat.univariate.AbstractStorelessUnivariateStatistic;

public class Max extends AbstractStorelessUnivariateStatistic implements Serializable
{
    static final long serialVersionUID = -5593383832225844641L;
    private long n;
    private double value;
    
    public Max() {
        this.n = 0L;
        this.value = Double.NaN;
    }
    
    public void increment(final double d) {
        this.value = (Double.isNaN(this.value) ? d : Math.max(this.value, d));
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
        double max = Double.NaN;
        if (this.test(values, begin, length)) {
            max = values[begin];
            for (int i = begin; i < begin + length; ++i) {
                max = ((max > values[i]) ? max : values[i]);
            }
        }
        return max;
    }
}
