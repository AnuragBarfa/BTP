// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.math.stat.univariate.summary;

import java.io.Serializable;
import org.apache.commons.math.stat.univariate.AbstractStorelessUnivariateStatistic;

public class SumOfLogs extends AbstractStorelessUnivariateStatistic implements Serializable
{
    static final long serialVersionUID = -370076995648386763L;
    private int n;
    private double value;
    private boolean init;
    
    public SumOfLogs() {
        this.n = 0;
        this.value = Double.NaN;
        this.init = true;
    }
    
    public void increment(final double d) {
        if (this.init) {
            this.value = Math.log(d);
            this.init = false;
        }
        else {
            this.value += Math.log(d);
        }
        ++this.n;
    }
    
    public double getResult() {
        return this.value;
    }
    
    public double getN() {
        return this.n;
    }
    
    public void clear() {
        this.value = Double.NaN;
        this.init = true;
        this.n = 0;
    }
    
    public double evaluate(final double[] values, final int begin, final int length) {
        double sumLog = Double.NaN;
        if (this.test(values, begin, length)) {
            sumLog = 0.0;
            for (int i = begin; i < begin + length; ++i) {
                sumLog += Math.log(values[i]);
            }
        }
        return sumLog;
    }
}
