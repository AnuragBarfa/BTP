// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.math.stat.univariate.moment;

import java.io.Serializable;
import org.apache.commons.math.stat.univariate.AbstractStorelessUnivariateStatistic;

public class FirstMoment extends AbstractStorelessUnivariateStatistic implements Serializable
{
    static final long serialVersionUID = -803343206421984070L;
    protected long n;
    protected double m1;
    protected double dev;
    protected double v;
    protected double n0;
    
    public FirstMoment() {
        this.n = 0L;
        this.m1 = Double.NaN;
        this.dev = 0.0;
        this.v = 0.0;
        this.n0 = 0.0;
    }
    
    public void increment(final double d) {
        if (this.n < 1L) {
            this.m1 = 0.0;
        }
        ++this.n;
        this.dev = d - this.m1;
        this.n0 = (double)this.n;
        this.v = this.dev / this.n0;
        this.m1 += this.v;
    }
    
    public void clear() {
        this.m1 = Double.NaN;
        this.n = 0L;
        this.dev = 0.0;
        this.v = 0.0;
        this.n0 = 0.0;
    }
    
    public double getResult() {
        return this.m1;
    }
    
    public double getN() {
        return (double)this.n;
    }
}
