// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.math.distribution;

import org.apache.commons.math.MathException;
import org.apache.commons.math.special.Gamma;
import java.io.Serializable;

public class GammaDistributionImpl extends AbstractContinuousDistribution implements GammaDistribution, Serializable
{
    private double alpha;
    private double beta;
    
    public GammaDistributionImpl(final double alpha, final double beta) {
        this.setAlpha(alpha);
        this.setBeta(beta);
    }
    
    public double cumulativeProbability(final double x) throws MathException {
        double ret;
        if (x <= 0.0) {
            ret = 0.0;
        }
        else {
            ret = Gamma.regularizedGammaP(this.getAlpha(), x / this.getBeta());
        }
        return ret;
    }
    
    public void setAlpha(final double alpha) {
        if (alpha <= 0.0) {
            throw new IllegalArgumentException("alpha must be positive");
        }
        this.alpha = alpha;
    }
    
    public double getAlpha() {
        return this.alpha;
    }
    
    public void setBeta(final double beta) {
        if (beta <= 0.0) {
            throw new IllegalArgumentException("beta must be positive");
        }
        this.beta = beta;
    }
    
    public double getBeta() {
        return this.beta;
    }
    
    protected double getDomainLowerBound(final double p) {
        return Double.MIN_VALUE;
    }
    
    protected double getDomainUpperBound(final double p) {
        double ret;
        if (p < 0.5) {
            ret = this.getAlpha() * this.getBeta();
        }
        else {
            ret = Double.MAX_VALUE;
        }
        return ret;
    }
    
    protected double getInitialDomain(final double p) {
        double ret;
        if (p < 0.5) {
            ret = this.getAlpha() * this.getBeta() * 0.5;
        }
        else {
            ret = this.getAlpha() * this.getBeta();
        }
        return ret;
    }
}
