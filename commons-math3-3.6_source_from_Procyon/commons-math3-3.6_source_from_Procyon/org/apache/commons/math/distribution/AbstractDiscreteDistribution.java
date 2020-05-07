// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.math.distribution;

import org.apache.commons.math.MathException;

public abstract class AbstractDiscreteDistribution implements DiscreteDistribution
{
    protected AbstractDiscreteDistribution() {
    }
    
    public double cumulativeProbability(final int x0, final int x1) throws MathException {
        return this.cumulativeProbability(x1) - this.cumulativeProbability(x0 - 1);
    }
    
    public int inverseCumulativeProbability(final double p) throws MathException {
        if (p < 0.0 || p > 1.0) {
            throw new IllegalArgumentException("p must be between 0.0 and 1.0, inclusive.");
        }
        int x0 = this.getDomainLowerBound(p);
        int x2 = this.getDomainUpperBound(p);
        while (x0 < x2) {
            final int xm = x0 + (x2 - x0) / 2;
            final double pm = this.cumulativeProbability(xm);
            if (pm > p) {
                if (xm == x2) {
                    --x2;
                }
                else {
                    x2 = xm;
                }
            }
            else if (xm == x0) {
                ++x0;
            }
            else {
                x0 = xm;
            }
        }
        for (double pm = this.cumulativeProbability(x0); pm > p; pm = this.cumulativeProbability(x0)) {
            --x0;
        }
        return x0;
    }
    
    protected abstract int getDomainLowerBound(final double p0);
    
    protected abstract int getDomainUpperBound(final double p0);
}
