// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.math.distribution;

import org.apache.commons.math.util.MathUtils;
import org.apache.commons.math.MathException;
import java.io.Serializable;

public class HypergeometricDistributionImpl extends AbstractDiscreteDistribution implements HypergeometricDistribution, Serializable
{
    private int numberOfSuccesses;
    private int populationSize;
    private int sampleSize;
    
    public HypergeometricDistributionImpl(final int populationSize, final int numberOfSuccesses, final int sampleSize) {
        this.setPopulationSize(populationSize);
        this.setSampleSize(sampleSize);
        this.setNumberOfSuccesses(numberOfSuccesses);
    }
    
    public double cumulativeProbability(final int x) throws MathException {
        final int n = this.getPopulationSize();
        final int m = this.getNumberOfSuccesses();
        final int k = this.getSampleSize();
        final int[] domain = this.getDomain(n, m, k);
        double ret;
        if (x < domain[0]) {
            ret = 0.0;
        }
        else if (x >= domain[1]) {
            ret = 1.0;
        }
        else {
            ret = 0.0;
            for (int i = domain[0]; i <= x; ++i) {
                ret += this.probability(n, m, k, i);
            }
        }
        return ret;
    }
    
    private int[] getDomain(final int n, final int m, final int k) {
        return new int[] { this.getLowerDomain(n, m, k), this.getUpperDomain(m, k) };
    }
    
    protected int getDomainLowerBound(final double p) {
        return this.getLowerDomain(this.getPopulationSize(), this.getNumberOfSuccesses(), this.getSampleSize());
    }
    
    protected int getDomainUpperBound(final double p) {
        return this.getUpperDomain(this.getSampleSize(), this.getNumberOfSuccesses());
    }
    
    private int getLowerDomain(final int n, final int m, final int k) {
        return Math.max(0, m - (n - k));
    }
    
    public int getNumberOfSuccesses() {
        return this.numberOfSuccesses;
    }
    
    public int getPopulationSize() {
        return this.populationSize;
    }
    
    public int getSampleSize() {
        return this.sampleSize;
    }
    
    private int getUpperDomain(final int m, final int k) {
        return Math.min(k, m);
    }
    
    public double probability(final int x) {
        final int n = this.getPopulationSize();
        final int m = this.getNumberOfSuccesses();
        final int k = this.getSampleSize();
        final int[] domain = this.getDomain(n, m, k);
        double ret;
        if (x < domain[0] || x > domain[1]) {
            ret = 0.0;
        }
        else {
            ret = this.probability(n, m, k, x);
        }
        return ret;
    }
    
    private double probability(final int n, final int m, final int k, final int x) {
        return Math.exp(MathUtils.binomialCoefficientLog(m, x) + MathUtils.binomialCoefficientLog(n - m, k - x) - MathUtils.binomialCoefficientLog(n, k));
    }
    
    public void setNumberOfSuccesses(final int num) {
        if (num < 0) {
            throw new IllegalArgumentException("number of successes must be non-negative.");
        }
        this.numberOfSuccesses = num;
    }
    
    public void setPopulationSize(final int size) {
        if (size <= 0) {
            throw new IllegalArgumentException("population size must be positive.");
        }
        this.populationSize = size;
    }
    
    public void setSampleSize(final int size) {
        if (size < 0) {
            throw new IllegalArgumentException("sample size must be non-negative.");
        }
        this.sampleSize = size;
    }
}
