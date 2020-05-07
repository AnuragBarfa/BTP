// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.math.stat.univariate.moment;

import java.io.Serializable;
import org.apache.commons.math.stat.univariate.summary.SumOfLogs;

public class GeometricMean extends SumOfLogs implements Serializable
{
    static final long serialVersionUID = -8178734905303459453L;
    protected long n;
    private double geoMean;
    private double lastSum;
    
    public GeometricMean() {
        this.n = 0L;
        this.geoMean = Double.NaN;
        this.lastSum = 0.0;
    }
    
    public void increment(final double d) {
        ++this.n;
        super.increment(d);
    }
    
    public double getResult() {
        if (this.lastSum != super.getResult() || this.n == 1L) {
            this.lastSum = super.getResult();
            this.geoMean = Math.exp(this.lastSum / this.n);
        }
        return this.geoMean;
    }
    
    public void clear() {
        super.clear();
        this.lastSum = 0.0;
        this.geoMean = Double.NaN;
        this.n = 0L;
    }
    
    public double evaluate(final double[] values, final int begin, final int length) {
        return Math.exp(super.evaluate(values, begin, length) / length);
    }
}
