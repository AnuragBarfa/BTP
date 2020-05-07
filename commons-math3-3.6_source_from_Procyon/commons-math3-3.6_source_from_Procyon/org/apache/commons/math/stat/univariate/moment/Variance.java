// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.math.stat.univariate.moment;

import java.io.Serializable;
import org.apache.commons.math.stat.univariate.AbstractStorelessUnivariateStatistic;

public class Variance extends AbstractStorelessUnivariateStatistic implements Serializable
{
    static final long serialVersionUID = -9111962718267217978L;
    protected SecondMoment moment;
    protected boolean incMoment;
    protected double variance;
    protected long n;
    protected Mean mean;
    
    public Variance() {
        this.moment = null;
        this.incMoment = true;
        this.variance = Double.NaN;
        this.n = 0L;
        this.mean = new Mean();
        this.moment = new SecondMoment();
    }
    
    public Variance(final SecondMoment m2) {
        this.moment = null;
        this.incMoment = true;
        this.variance = Double.NaN;
        this.n = 0L;
        this.mean = new Mean();
        this.incMoment = false;
        this.moment = m2;
    }
    
    public void increment(final double d) {
        if (this.incMoment) {
            this.moment.increment(d);
        }
    }
    
    public double getResult() {
        if (this.n < this.moment.n) {
            if (this.moment.n <= 0L) {
                this.variance = Double.NaN;
            }
            else if (this.moment.n <= 1L) {
                this.variance = 0.0;
            }
            else {
                this.variance = this.moment.m2 / (this.moment.n0 - 1.0);
            }
            this.n = this.moment.n;
        }
        return this.variance;
    }
    
    public double getN() {
        return this.moment.getN();
    }
    
    public void clear() {
        if (this.incMoment) {
            this.moment.clear();
        }
        this.variance = Double.NaN;
        this.n = 0L;
    }
    
    public double evaluate(final double[] values, final int begin, final int length) {
        double var = Double.NaN;
        if (this.test(values, begin, length)) {
            if (length == 1) {
                var = 0.0;
            }
            else if (length > 1) {
                final double m = this.mean.evaluate(values, begin, length);
                double accum = 0.0;
                double accum2 = 0.0;
                for (int i = begin; i < begin + length; ++i) {
                    accum += Math.pow(values[i] - m, 2.0);
                    accum2 += values[i] - m;
                }
                var = (accum - Math.pow(accum2, 2.0) / length) / (length - 1);
            }
        }
        return var;
    }
}
