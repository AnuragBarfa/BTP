// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.math.distribution;

import org.apache.commons.math.analysis.UnivariateRealSolverUtils;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.MathException;

public abstract class AbstractContinuousDistribution implements ContinuousDistribution
{
    protected AbstractContinuousDistribution() {
    }
    
    public double cumulativeProbability(final double x0, final double x1) throws MathException {
        return this.cumulativeProbability(x1) - this.cumulativeProbability(x0);
    }
    
    public double inverseCumulativeProbability(final double p) throws MathException {
        if (p < 0.0 || p > 1.0) {
            throw new IllegalArgumentException("p must be between 0.0 and 1.0, inclusive.");
        }
        final UnivariateRealFunction rootFindingFunction = new UnivariateRealFunction() {
            public double value(final double x) throws MathException {
                return AbstractContinuousDistribution.this.cumulativeProbability(x) - p;
            }
        };
        final double[] bracket = UnivariateRealSolverUtils.bracket(rootFindingFunction, this.getInitialDomain(p), this.getDomainLowerBound(p), this.getDomainUpperBound(p));
        final double root = UnivariateRealSolverUtils.solve(rootFindingFunction, bracket[0], bracket[1]);
        return root;
    }
    
    protected abstract double getInitialDomain(final double p0);
    
    protected abstract double getDomainLowerBound(final double p0);
    
    protected abstract double getDomainUpperBound(final double p0);
}
