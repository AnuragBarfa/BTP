// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.math.distribution;

import org.apache.commons.discovery.tools.DiscoverClass;

public abstract class DistributionFactory
{
    protected DistributionFactory() {
    }
    
    public static DistributionFactory newInstance() {
        DistributionFactory factory = null;
        try {
            final DiscoverClass dc = new DiscoverClass();
            factory = (DistributionFactory)dc.newInstance(DistributionFactory.class, "org.apache.commons.math.distribution.DistributionFactoryImpl");
        }
        catch (Exception ex) {}
        return factory;
    }
    
    public abstract BinomialDistribution createBinomialDistribution(final int p0, final double p1);
    
    public abstract ChiSquaredDistribution createChiSquareDistribution(final double p0);
    
    public abstract ExponentialDistribution createExponentialDistribution(final double p0);
    
    public abstract FDistribution createFDistribution(final double p0, final double p1);
    
    public abstract GammaDistribution createGammaDistribution(final double p0, final double p1);
    
    public abstract TDistribution createTDistribution(final double p0);
    
    public abstract HypergeometricDistribution createHypergeometricDistribution(final int p0, final int p1, final int p2);
    
    public abstract NormalDistribution createNormalDistribution(final double p0, final double p1);
    
    public abstract NormalDistribution createNormalDistribution();
}
