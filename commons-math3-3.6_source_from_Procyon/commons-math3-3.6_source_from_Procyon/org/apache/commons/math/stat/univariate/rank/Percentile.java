// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.math.stat.univariate.rank;

import java.util.Arrays;
import java.io.Serializable;
import org.apache.commons.math.stat.univariate.AbstractUnivariateStatistic;

public class Percentile extends AbstractUnivariateStatistic implements Serializable
{
    static final long serialVersionUID = -8091216485095130416L;
    private double percentile;
    
    public Percentile() {
        this.percentile = 0.0;
        this.percentile = 50.0;
    }
    
    public Percentile(final double p) {
        this.percentile = 0.0;
        this.percentile = p;
    }
    
    public double evaluate(final double[] values, final double p) {
        return this.evaluate(values, 0, values.length, p);
    }
    
    public double evaluate(final double[] values, final int start, final int length) {
        return this.evaluate(values, start, length, this.percentile);
    }
    
    public double evaluate(final double[] values, final int begin, final int length, final double p) {
        this.test(values, begin, length);
        if (p > 100.0 || p <= 0.0) {
            throw new IllegalArgumentException("invalid percentile value");
        }
        final double n = length;
        if (n == 0.0) {
            return Double.NaN;
        }
        if (n == 1.0) {
            return values[begin];
        }
        final double pos = p * (n + 1.0) / 100.0;
        final double fpos = Math.floor(pos);
        final int intPos = (int)fpos;
        final double dif = pos - fpos;
        final double[] sorted = new double[length];
        System.arraycopy(values, begin, sorted, 0, length);
        Arrays.sort(sorted);
        if (pos < 1.0) {
            return sorted[0];
        }
        if (pos >= n) {
            return sorted[length - 1];
        }
        final double lower = sorted[intPos - 1];
        final double upper = sorted[intPos];
        return lower + dif * (upper - lower);
    }
    
    public double getPercentile() {
        return this.percentile;
    }
    
    public void setPercentile(final double p) {
        this.percentile = p;
    }
}
