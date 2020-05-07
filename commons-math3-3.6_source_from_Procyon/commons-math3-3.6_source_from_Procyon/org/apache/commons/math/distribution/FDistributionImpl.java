// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.math.distribution;

import org.apache.commons.math.MathException;
import org.apache.commons.math.special.Beta;
import java.io.Serializable;

public class FDistributionImpl extends AbstractContinuousDistribution implements FDistribution, Serializable
{
    private double numeratorDegreesOfFreedom;
    private double denominatorDegreesOfFreedom;
    
    public FDistributionImpl(final double numeratorDegreesOfFreedom, final double denominatorDegreesOfFreedom) {
        this.setNumeratorDegreesOfFreedom(numeratorDegreesOfFreedom);
        this.setDenominatorDegreesOfFreedom(denominatorDegreesOfFreedom);
    }
    
    public double cumulativeProbability(final double x) throws MathException {
        double ret;
        if (x <= 0.0) {
            ret = 0.0;
        }
        else {
            final double n = this.getNumeratorDegreesOfFreedom();
            final double m = this.getDenominatorDegreesOfFreedom();
            ret = Beta.regularizedBeta(n * x / (m + n * x), 0.5 * n, 0.5 * m);
        }
        return ret;
    }
    
    protected double getDomainLowerBound(final double p) {
        return 0.0;
    }
    
    protected double getDomainUpperBound(final double p) {
        return Double.MAX_VALUE;
    }
    
    protected double getInitialDomain(final double p) {
        return this.getDenominatorDegreesOfFreedom() / (this.getDenominatorDegreesOfFreedom() - 2.0);
    }
    
    public void setNumeratorDegreesOfFreedom(final double degreesOfFreedom) {
        if (degreesOfFreedom <= 0.0) {
            throw new IllegalArgumentException("degrees of freedom must be positive.");
        }
        this.numeratorDegreesOfFreedom = degreesOfFreedom;
    }
    
    public double getNumeratorDegreesOfFreedom() {
        return this.numeratorDegreesOfFreedom;
    }
    
    public void setDenominatorDegreesOfFreedom(final double degreesOfFreedom) {
        if (degreesOfFreedom <= 0.0) {
            throw new IllegalArgumentException("degrees of freedom must be positive.");
        }
        this.denominatorDegreesOfFreedom = degreesOfFreedom;
    }
    
    public double getDenominatorDegreesOfFreedom() {
        return this.denominatorDegreesOfFreedom;
    }
}
