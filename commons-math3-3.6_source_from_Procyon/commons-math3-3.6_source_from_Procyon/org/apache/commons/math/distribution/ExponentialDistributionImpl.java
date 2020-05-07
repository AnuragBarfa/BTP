// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.math.distribution;

import org.apache.commons.math.MathException;
import java.io.Serializable;

public class ExponentialDistributionImpl implements ExponentialDistribution, Serializable
{
    private double mean;
    
    public ExponentialDistributionImpl(final double mean) {
        this.setMean(mean);
    }
    
    public void setMean(final double mean) {
        if (mean <= 0.0) {
            throw new IllegalArgumentException("mean must be positive.");
        }
        this.mean = mean;
    }
    
    public double getMean() {
        return this.mean;
    }
    
    public double cumulativeProbability(final double x) throws MathException {
        double ret;
        if (x <= 0.0) {
            ret = 0.0;
        }
        else {
            ret = 1.0 - Math.exp(-x / this.getMean());
        }
        return ret;
    }
    
    public double inverseCumulativeProbability(final double p) throws MathException {
        double ret;
        if (p < 0.0 || p > 1.0) {
            ret = Double.NaN;
        }
        else if (p == 1.0) {
            ret = Double.POSITIVE_INFINITY;
        }
        else {
            ret = -this.getMean() * Math.log(1.0 - p);
        }
        return ret;
    }
    
    public double cumulativeProbability(final double x0, final double x1) throws MathException {
        return this.cumulativeProbability(x1) - this.cumulativeProbability(x0);
    }
}
