// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.math.distribution;

import org.apache.commons.math.util.MathUtils;
import org.apache.commons.math.MathException;
import org.apache.commons.math.special.Beta;
import java.io.Serializable;

public class BinomialDistributionImpl extends AbstractDiscreteDistribution implements BinomialDistribution, Serializable
{
    private int numberOfTrials;
    private double probabilityOfSuccess;
    
    public BinomialDistributionImpl(final int trials, final double p) {
        this.setNumberOfTrials(trials);
        this.setProbabilityOfSuccess(p);
    }
    
    public int getNumberOfTrials() {
        return this.numberOfTrials;
    }
    
    public double getProbabilityOfSuccess() {
        return this.probabilityOfSuccess;
    }
    
    public void setNumberOfTrials(final int trials) {
        if (trials < 0) {
            throw new IllegalArgumentException("number of trials must be non-negative.");
        }
        this.numberOfTrials = trials;
    }
    
    public void setProbabilityOfSuccess(final double p) {
        if (p < 0.0 || p > 1.0) {
            throw new IllegalArgumentException("probability of success must be between 0.0 and 1.0, inclusive.");
        }
        this.probabilityOfSuccess = p;
    }
    
    protected int getDomainLowerBound(final double p) {
        return -1;
    }
    
    protected int getDomainUpperBound(final double p) {
        return this.getNumberOfTrials();
    }
    
    public double cumulativeProbability(final int x) throws MathException {
        double ret;
        if (x < 0) {
            ret = 0.0;
        }
        else if (x >= this.getNumberOfTrials()) {
            ret = 1.0;
        }
        else {
            ret = 1.0 - Beta.regularizedBeta(this.getProbabilityOfSuccess(), x + 1.0, this.getNumberOfTrials() - x);
        }
        return ret;
    }
    
    public double probability(final int x) {
        double ret;
        if (x < 0 || x > this.getNumberOfTrials()) {
            ret = 0.0;
        }
        else {
            ret = MathUtils.binomialCoefficientDouble(this.getNumberOfTrials(), x) * Math.pow(this.getProbabilityOfSuccess(), x) * Math.pow(1.0 - this.getProbabilityOfSuccess(), this.getNumberOfTrials() - x);
        }
        return ret;
    }
}
