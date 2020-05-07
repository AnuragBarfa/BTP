// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.math.stat.univariate.moment;

import java.io.Serializable;

public class SecondMoment extends FirstMoment implements Serializable
{
    static final long serialVersionUID = 3942403127395076445L;
    protected double m2;
    protected double n1;
    
    public SecondMoment() {
        this.m2 = Double.NaN;
        this.n1 = 0.0;
    }
    
    public void increment(final double d) {
        if (super.n < 1L) {
            final double n = 0.0;
            this.m2 = n;
            super.m1 = n;
        }
        super.increment(d);
        this.n1 = super.n0 - 1.0;
        this.m2 += this.n1 * super.dev * super.v;
    }
    
    public void clear() {
        super.clear();
        this.m2 = Double.NaN;
        this.n1 = 0.0;
    }
    
    public double getResult() {
        return this.m2;
    }
}
