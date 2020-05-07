// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.math.distribution;

import org.apache.commons.math.MathException;
import java.io.Serializable;

public class ChiSquaredDistributionImpl extends AbstractContinuousDistribution implements ChiSquaredDistribution, Serializable
{
    private GammaDistribution gamma;
    
    public ChiSquaredDistributionImpl(final double degreesOfFreedom) {
        this.setGamma(DistributionFactory.newInstance().createGammaDistribution(degreesOfFreedom / 2.0, 2.0));
    }
    
    public void setDegreesOfFreedom(final double degreesOfFreedom) {
        this.getGamma().setAlpha(degreesOfFreedom / 2.0);
    }
    
    public double getDegreesOfFreedom() {
        return this.getGamma().getAlpha() * 2.0;
    }
    
    public double cumulativeProbability(final double x) throws MathException {
        return this.getGamma().cumulativeProbability(x);
    }
    
    protected double getDomainLowerBound(final double p) {
        return Double.MIN_VALUE * this.getGamma().getBeta();
    }
    
    protected double getDomainUpperBound(final double p) {
        double ret;
        if (p < 0.5) {
            ret = this.getDegreesOfFreedom();
        }
        else {
            ret = Double.MAX_VALUE;
        }
        return ret;
    }
    
    protected double getInitialDomain(final double p) {
        double ret;
        if (p < 0.5) {
            ret = this.getDegreesOfFreedom() * 0.5;
        }
        else {
            ret = this.getDegreesOfFreedom();
        }
        return ret;
    }
    
    private void setGamma(final GammaDistribution gamma) {
        this.gamma = gamma;
    }
    
    private GammaDistribution getGamma() {
        return this.gamma;
    }
}
