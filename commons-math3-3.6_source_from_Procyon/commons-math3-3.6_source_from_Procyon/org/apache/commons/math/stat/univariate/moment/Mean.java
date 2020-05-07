// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.math.stat.univariate.moment;

import org.apache.commons.math.stat.univariate.summary.Sum;
import java.io.Serializable;
import org.apache.commons.math.stat.univariate.AbstractStorelessUnivariateStatistic;

public class Mean extends AbstractStorelessUnivariateStatistic implements Serializable
{
    static final long serialVersionUID = -1296043746617791564L;
    protected FirstMoment moment;
    protected boolean incMoment;
    protected Sum sum;
    
    public Mean() {
        this.moment = null;
        this.incMoment = true;
        this.sum = new Sum();
        this.moment = new FirstMoment();
    }
    
    public Mean(final FirstMoment m1) {
        this.moment = null;
        this.incMoment = true;
        this.sum = new Sum();
        this.moment = m1;
        this.incMoment = false;
    }
    
    public void increment(final double d) {
        if (this.incMoment) {
            this.moment.increment(d);
        }
    }
    
    public void clear() {
        if (this.incMoment) {
            this.moment.clear();
        }
    }
    
    public double getResult() {
        return this.moment.m1;
    }
    
    public double getN() {
        return this.moment.getN();
    }
    
    public double evaluate(final double[] values, final int begin, final int length) {
        if (this.test(values, begin, length)) {
            return this.sum.evaluate(values) / length;
        }
        return Double.NaN;
    }
}
