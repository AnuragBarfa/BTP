// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.math.stat.univariate.moment;

import java.io.Serializable;
import org.apache.commons.math.stat.univariate.AbstractStorelessUnivariateStatistic;

public class Kurtosis extends AbstractStorelessUnivariateStatistic implements Serializable
{
    static final long serialVersionUID = 2784465764798260919L;
    protected FourthMoment moment;
    protected boolean incMoment;
    private double kurtosis;
    private long n;
    Mean mean;
    
    public Kurtosis() {
        this.moment = null;
        this.incMoment = true;
        this.kurtosis = Double.NaN;
        this.n = 0L;
        this.mean = new Mean();
        this.moment = new FourthMoment();
    }
    
    public Kurtosis(final FourthMoment m4) {
        this.moment = null;
        this.incMoment = true;
        this.kurtosis = Double.NaN;
        this.n = 0L;
        this.mean = new Mean();
        this.incMoment = false;
        this.moment = m4;
    }
    
    public void increment(final double d) {
        if (this.incMoment) {
            this.moment.increment(d);
        }
    }
    
    public double getResult() {
        if (this.n < this.moment.n) {
            if (this.moment.n <= 0L) {
                this.kurtosis = Double.NaN;
            }
            final double variance = (this.moment.n < 1L) ? 0.0 : (this.moment.m2 / (this.moment.n - 1L));
            if (this.moment.n <= 3L || variance < 1.0E-19) {
                this.kurtosis = 0.0;
            }
            else {
                this.kurtosis = (this.moment.n0 * (this.moment.n0 + 1.0) * this.moment.m4 - 3.0 * this.moment.m2 * this.moment.m2 * this.moment.n1) / (this.moment.n1 * this.moment.n2 * this.moment.n3 * variance * variance);
            }
            this.n = this.moment.n;
        }
        return this.kurtosis;
    }
    
    public void clear() {
        if (this.incMoment) {
            this.moment.clear();
        }
        this.kurtosis = Double.NaN;
        this.n = 0L;
    }
    
    public double getN() {
        return this.moment.getN();
    }
    
    public double evaluate(final double[] values, final int begin, final int length) {
        double kurt = Double.NaN;
        if (this.test(values, begin, length)) {
            if (length <= 3) {
                kurt = 0.0;
            }
            else {
                final double m = this.mean.evaluate(values, begin, length);
                double accum = 0.0;
                double accum2 = 0.0;
                for (int i = begin; i < begin + length; ++i) {
                    accum += Math.pow(values[i] - m, 2.0);
                    accum2 += values[i] - m;
                }
                final double stdDev = Math.sqrt((accum - Math.pow(accum2, 2.0) / length) / (length - 1));
                double accum3 = 0.0;
                for (int j = begin; j < begin + length; ++j) {
                    accum3 += Math.pow((values[j] - m) / stdDev, 4.0);
                }
                final double n0 = length;
                final double coefficientOne = n0 * (n0 + 1.0) / ((n0 - 1.0) * (n0 - 2.0) * (n0 - 3.0));
                final double termTwo = 3.0 * Math.pow(n0 - 1.0, 2.0) / ((n0 - 2.0) * (n0 - 3.0));
                kurt = coefficientOne * accum3 - termTwo;
            }
        }
        return kurt;
    }
}
