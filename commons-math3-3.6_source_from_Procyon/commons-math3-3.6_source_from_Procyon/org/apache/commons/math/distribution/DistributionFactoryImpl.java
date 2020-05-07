// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.math.distribution;

import java.io.Serializable;

public class DistributionFactoryImpl extends DistributionFactory implements Serializable
{
    public ChiSquaredDistribution createChiSquareDistribution(final double degreesOfFreedom) {
        return new ChiSquaredDistributionImpl(degreesOfFreedom);
    }
    
    public GammaDistribution createGammaDistribution(final double alpha, final double beta) {
        return new GammaDistributionImpl(alpha, beta);
    }
    
    public TDistribution createTDistribution(final double degreesOfFreedom) {
        return new TDistributionImpl(degreesOfFreedom);
    }
    
    public FDistribution createFDistribution(final double numeratorDegreesOfFreedom, final double denominatorDegreesOfFreedom) {
        return new FDistributionImpl(numeratorDegreesOfFreedom, denominatorDegreesOfFreedom);
    }
    
    public ExponentialDistribution createExponentialDistribution(final double mean) {
        return new ExponentialDistributionImpl(mean);
    }
    
    public BinomialDistribution createBinomialDistribution(final int numberOfTrials, final double probabilityOfSuccess) {
        return new BinomialDistributionImpl(numberOfTrials, probabilityOfSuccess);
    }
    
    public HypergeometricDistribution createHypergeometricDistribution(final int populationSize, final int numberOfSuccesses, final int sampleSize) {
        return new HypergeometricDistributionImpl(populationSize, numberOfSuccesses, sampleSize);
    }
    
    public NormalDistribution createNormalDistribution(final double mean, final double sd) {
        return new NormalDistributionImpl(mean, sd);
    }
    
    public NormalDistribution createNormalDistribution() {
        return new NormalDistributionImpl();
    }
}
