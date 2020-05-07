// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.math.stat.univariate.moment;

import java.io.Serializable;
import org.apache.commons.math.stat.univariate.AbstractStorelessUnivariateStatistic;

public class Skewness extends AbstractStorelessUnivariateStatistic implements Serializable
{
    static final long serialVersionUID = 7101857578996691352L;
    protected ThirdMoment moment;
    protected boolean incMoment;
    protected double skewness;
    private long n;
    Mean mean;
    
    public Skewness() {
        this.moment = null;
        this.incMoment = true;
        this.skewness = Double.NaN;
        this.n = 0L;
        this.mean = new Mean();
        this.moment = new ThirdMoment();
    }
    
    public Skewness(final ThirdMoment m3) {
        this.moment = null;
        this.incMoment = true;
        this.skewness = Double.NaN;
        this.n = 0L;
        this.mean = new Mean();
        this.incMoment = false;
        this.moment = m3;
    }
    
    public void increment(final double d) {
        if (this.incMoment) {
            this.moment.increment(d);
        }
    }
    
    public double getResult() {
        if (this.n < this.moment.n) {
            if (this.moment.n <= 0L) {
                this.skewness = Double.NaN;
            }
            final double variance = (this.moment.n < 1L) ? 0.0 : (this.moment.m2 / (this.moment.n - 1L));
            if (this.moment.n <= 2L || variance < 1.0E-19) {
                this.skewness = 0.0;
            }
            else {
                this.skewness = this.moment.n0 * this.moment.m3 / (this.moment.n1 * this.moment.n2 * Math.sqrt(variance) * variance);
            }
            this.n = this.moment.n;
        }
        return this.skewness;
    }
    
    public double getN() {
        return this.moment.getN();
    }
    
    public void clear() {
        if (this.incMoment) {
            this.moment.clear();
        }
        this.skewness = Double.NaN;
        this.n = 0L;
    }
    
    public double evaluate(final double[] values, final int begin, final int length) {
        double skew = Double.NaN;
        if (this.test(values, begin, length)) {
            if (length <= 2) {
                skew = 0.0;
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
                    accum3 += Math.pow((values[j] - m) / stdDev, 3.0);
                }
                final double n0 = length;
                skew = n0 / ((n0 - 1.0) * (n0 - 2.0)) * accum3;
            }
        }
        return skew;
    }
}
