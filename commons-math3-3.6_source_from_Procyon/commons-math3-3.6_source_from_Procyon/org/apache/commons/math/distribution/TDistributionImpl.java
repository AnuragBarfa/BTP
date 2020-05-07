// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.math.distribution;

import org.apache.commons.math.MathException;
import org.apache.commons.math.special.Beta;
import java.io.Serializable;

public class TDistributionImpl extends AbstractContinuousDistribution implements TDistribution, Serializable
{
    private double degreesOfFreedom;
    
    public TDistributionImpl(final double degreesOfFreedom) {
        this.setDegreesOfFreedom(degreesOfFreedom);
    }
    
    public void setDegreesOfFreedom(final double degreesOfFreedom) {
        if (degreesOfFreedom <= 0.0) {
            throw new IllegalArgumentException("degrees of freedom must be positive.");
        }
        this.degreesOfFreedom = degreesOfFreedom;
    }
    
    public double getDegreesOfFreedom() {
        return this.degreesOfFreedom;
    }
    
    public double cumulativeProbability(final double x) throws MathException {
        double ret;
        if (x == 0.0) {
            ret = 0.5;
        }
        else {
            final double t = Beta.regularizedBeta(this.getDegreesOfFreedom() / (this.getDegreesOfFreedom() + x * x), 0.5 * this.getDegreesOfFreedom(), 0.5);
            if (x < 0.0) {
                ret = 0.5 * t;
            }
            else {
                ret = 1.0 - 0.5 * t;
            }
        }
        return ret;
    }
    
    protected double getDomainLowerBound(final double p) {
        return -1.7976931348623157E308;
    }
    
    protected double getDomainUpperBound(final double p) {
        return Double.MAX_VALUE;
    }
    
    protected double getInitialDomain(final double p) {
        return 0.0;
    }
}
