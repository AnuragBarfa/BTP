// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.math.stat;

import org.apache.commons.math.distribution.TDistribution;
import org.apache.commons.math.MathException;
import org.apache.commons.math.distribution.ChiSquaredDistribution;
import org.apache.commons.math.distribution.DistributionFactory;
import java.io.Serializable;

public class TestStatisticImpl implements TestStatistic, Serializable
{
    static final long serialVersionUID = 3357444126133491679L;
    
    public double chiSquare(final double[] expected, final double[] observed) throws IllegalArgumentException {
        double sumSq = 0.0;
        double dev = 0.0;
        if (expected.length < 2 || expected.length != observed.length) {
            throw new IllegalArgumentException("observed, expected array lengths incorrect");
        }
        if (StatUtils.min(expected) <= 0.0 || StatUtils.min(observed) < 0.0) {
            throw new IllegalArgumentException("observed counts must be non-negative, expected counts must be postive");
        }
        for (int i = 0; i < observed.length; ++i) {
            dev = observed[i] - expected[i];
            sumSq += dev * dev / expected[i];
        }
        return sumSq;
    }
    
    public double chiSquareTest(final double[] expected, final double[] observed) throws IllegalArgumentException, MathException {
        final ChiSquaredDistribution chiSquaredDistribution = DistributionFactory.newInstance().createChiSquareDistribution(expected.length - 1.0);
        return 1.0 - chiSquaredDistribution.cumulativeProbability(this.chiSquare(expected, observed));
    }
    
    public boolean chiSquareTest(final double[] expected, final double[] observed, final double alpha) throws IllegalArgumentException, MathException {
        if (alpha <= 0.0 || alpha > 0.5) {
            throw new IllegalArgumentException("bad significance level: " + alpha);
        }
        return this.chiSquareTest(expected, observed) < alpha;
    }
    
    public double t(final double mu, final double[] observed) throws IllegalArgumentException {
        if (observed == null || observed.length < 5) {
            throw new IllegalArgumentException("insufficient data for t statistic");
        }
        return this.t(StatUtils.mean(observed), mu, StatUtils.variance(observed), observed.length);
    }
    
    public boolean tTest(final double mu, final double[] sample, final double alpha) throws IllegalArgumentException, MathException {
        if (alpha <= 0.0 || alpha > 0.5) {
            throw new IllegalArgumentException("bad significance level: " + alpha);
        }
        return this.tTest(mu, sample) < alpha;
    }
    
    public double t(final double[] sample1, final double[] sample2) throws IllegalArgumentException {
        if (sample1 == null || sample2 == null || Math.min(sample1.length, sample2.length) < 5) {
            throw new IllegalArgumentException("insufficient data for t statistic");
        }
        return this.t(StatUtils.mean(sample1), StatUtils.mean(sample2), StatUtils.variance(sample1), StatUtils.variance(sample2), sample1.length, sample2.length);
    }
    
    public double tTest(final double[] sample1, final double[] sample2) throws IllegalArgumentException, MathException {
        if (sample1 == null || sample2 == null || Math.min(sample1.length, sample2.length) < 5) {
            throw new IllegalArgumentException("insufficient data");
        }
        return this.tTest(StatUtils.mean(sample1), StatUtils.mean(sample2), StatUtils.variance(sample1), StatUtils.variance(sample2), sample1.length, sample2.length);
    }
    
    public boolean tTest(final double[] sample1, final double[] sample2, final double alpha) throws IllegalArgumentException, MathException {
        if (alpha <= 0.0 || alpha > 0.5) {
            throw new IllegalArgumentException("bad significance level: " + alpha);
        }
        return this.tTest(sample1, sample2) < alpha;
    }
    
    public double tTest(final double mu, final double[] sample) throws IllegalArgumentException, MathException {
        if (sample == null || sample.length < 5) {
            throw new IllegalArgumentException("insufficient data for t statistic");
        }
        return this.tTest(StatUtils.mean(sample), mu, StatUtils.variance(sample), sample.length);
    }
    
    public double t(final double mu, final StatisticalSummary sampleStats) throws IllegalArgumentException {
        if (sampleStats == null || sampleStats.getN() < 5L) {
            throw new IllegalArgumentException("insufficient data for t statistic");
        }
        return this.t(sampleStats.getMean(), mu, sampleStats.getVariance(), (double)sampleStats.getN());
    }
    
    public double t(final StatisticalSummary sampleStats1, final StatisticalSummary sampleStats2) throws IllegalArgumentException {
        if (sampleStats1 == null || sampleStats2 == null || Math.min(sampleStats1.getN(), sampleStats2.getN()) < 5L) {
            throw new IllegalArgumentException("insufficient data for t statistic");
        }
        return this.t(sampleStats1.getMean(), sampleStats2.getMean(), sampleStats1.getVariance(), sampleStats2.getVariance(), (double)sampleStats1.getN(), (double)sampleStats2.getN());
    }
    
    public double tTest(final StatisticalSummary sampleStats1, final StatisticalSummary sampleStats2) throws IllegalArgumentException, MathException {
        if (sampleStats1 == null || sampleStats2 == null || Math.min(sampleStats1.getN(), sampleStats2.getN()) < 5L) {
            throw new IllegalArgumentException("insufficient data for t statistic");
        }
        return this.tTest(sampleStats1.getMean(), sampleStats2.getMean(), sampleStats1.getVariance(), sampleStats2.getVariance(), (double)sampleStats1.getN(), (double)sampleStats2.getN());
    }
    
    public boolean tTest(final StatisticalSummary sampleStats1, final StatisticalSummary sampleStats2, final double alpha) throws IllegalArgumentException, MathException {
        if (alpha <= 0.0 || alpha > 0.5) {
            throw new IllegalArgumentException("bad significance level: " + alpha);
        }
        return this.tTest(sampleStats1, sampleStats2) < alpha;
    }
    
    public boolean tTest(final double mu, final StatisticalSummary sampleStats, final double alpha) throws IllegalArgumentException, MathException {
        if (alpha <= 0.0 || alpha > 0.5) {
            throw new IllegalArgumentException("bad significance level: " + alpha);
        }
        return this.tTest(mu, sampleStats) < alpha;
    }
    
    public double tTest(final double mu, final StatisticalSummary sampleStats) throws IllegalArgumentException, MathException {
        if (sampleStats == null || sampleStats.getN() < 5L) {
            throw new IllegalArgumentException("insufficient data for t statistic");
        }
        return this.tTest(sampleStats.getMean(), mu, sampleStats.getVariance(), (double)sampleStats.getN());
    }
    
    private double df(final double v1, final double v2, final double n1, final double n2) {
        return (v1 / n1 + v2 / n2) * (v1 / n1 + v2 / n2) / (v1 * v1 / (n1 * n1 * (n1 - 1.0)) + v2 * v2 / (n2 * n2 * (n2 - 1.0)));
    }
    
    private double t(final double m1, final double m2, final double v1, final double v2, final double n1, final double n2) {
        return (m1 - m2) / Math.sqrt(v1 / n1 + v2 / n2);
    }
    
    private double t(final double m, final double mu, final double v, final double n) {
        return (m - mu) / Math.sqrt(v / n);
    }
    
    private double tTest(final double m1, final double m2, final double v1, final double v2, final double n1, final double n2) throws MathException {
        final double t = Math.abs(this.t(m1, m2, v1, v2, n1, n2));
        final TDistribution tDistribution = DistributionFactory.newInstance().createTDistribution(this.df(v1, v2, n1, n2));
        return 1.0 - tDistribution.cumulativeProbability(-t, t);
    }
    
    private double tTest(final double m, final double mu, final double v, final double n) throws MathException {
        final double t = Math.abs(this.t(m, mu, v, n));
        final TDistribution tDistribution = DistributionFactory.newInstance().createTDistribution(n - 1.0);
        return 1.0 - tDistribution.cumulativeProbability(-t, t);
    }
}
