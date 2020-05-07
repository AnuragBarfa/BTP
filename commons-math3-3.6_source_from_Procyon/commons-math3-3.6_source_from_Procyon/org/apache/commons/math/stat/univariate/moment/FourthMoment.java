// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.math.stat.univariate.moment;

import java.io.Serializable;

public class FourthMoment extends ThirdMoment implements Serializable
{
    static final long serialVersionUID = 4763990447117157611L;
    protected double m4;
    protected double prevM3;
    protected double n3;
    
    public FourthMoment() {
        this.m4 = Double.NaN;
        this.prevM3 = 0.0;
        this.n3 = 0.0;
    }
    
    public void increment(final double d) {
        if (super.n < 1L) {
            this.m4 = 0.0;
            super.m3 = 0.0;
            super.m2 = 0.0;
            super.m1 = 0.0;
        }
        this.prevM3 = super.m3;
        super.increment(d);
        this.n3 = (double)(super.n - 3L);
        this.m4 = this.m4 - 4.0 * super.v * this.prevM3 + 6.0 * super.v2 * super.prevM2 + (super.n0 * super.n0 - 3.0 * super.n1) * (super.v2 * super.v2 * super.n1 * super.n0);
    }
    
    public double getResult() {
        return this.m4;
    }
    
    public void clear() {
        super.clear();
        this.m4 = Double.NaN;
        this.prevM3 = 0.0;
        this.n3 = 0.0;
    }
}
