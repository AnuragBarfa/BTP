// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.math.stat.univariate.moment;

import java.io.Serializable;

public class StandardDeviation extends Variance implements Serializable
{
    static final long serialVersionUID = 5728716329662425188L;
    protected double std;
    private double lastVar;
    
    public StandardDeviation() {
        this.std = Double.NaN;
        this.lastVar = 0.0;
    }
    
    public StandardDeviation(final SecondMoment m2) {
        super(m2);
        this.std = Double.NaN;
        this.lastVar = 0.0;
    }
    
    public void increment(final double d) {
        super.increment(d);
    }
    
    public double getResult() {
        if (this.lastVar != super.getResult()) {
            this.lastVar = super.getResult();
            if (Double.isNaN(this.lastVar)) {
                this.std = Double.NaN;
            }
            else if (this.lastVar == 0.0) {
                this.std = 0.0;
            }
            else {
                this.std = Math.sqrt(this.lastVar);
            }
        }
        return this.std;
    }
    
    public void clear() {
        super.clear();
        this.lastVar = 0.0;
    }
    
    public double evaluate(final double[] values, final int begin, final int length) {
        final double var = super.evaluate(values, begin, length);
        if (Double.isNaN(var)) {
            return Double.NaN;
        }
        return (var != 0.0) ? Math.sqrt(var) : 0.0;
    }
}
