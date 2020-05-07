// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.math.stat.univariate.moment;

import java.io.Serializable;

public class ThirdMoment extends SecondMoment implements Serializable
{
    static final long serialVersionUID = -7818711964045118679L;
    protected double m3;
    protected double v2;
    protected double n2;
    protected double prevM2;
    
    public ThirdMoment() {
        this.m3 = Double.NaN;
        this.v2 = 0.0;
        this.n2 = 0.0;
        this.prevM2 = 0.0;
    }
    
    public void increment(final double d) {
        if (super.n < 1L) {
            final double m3 = 0.0;
            super.m1 = m3;
            super.m2 = m3;
            this.m3 = m3;
        }
        this.prevM2 = super.m2;
        super.increment(d);
        this.v2 = super.v * super.v;
        this.n2 = (double)(super.n - 2L);
        this.m3 = this.m3 - 3.0 * super.v * this.prevM2 + super.n0 * super.n1 * this.n2 * this.v2 * super.v;
    }
    
    public double getResult() {
        return this.m3;
    }
    
    public void clear() {
        super.clear();
        this.m3 = Double.NaN;
        this.v2 = 0.0;
        this.n2 = 0.0;
        this.prevM2 = 0.0;
    }
}
