// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.math.distribution;

import java.io.Serializable;

public class NormalDistributionImpl extends AbstractContinuousDistribution implements NormalDistribution, Serializable
{
    private double mean;
    private double standardDeviation;
    private NormalCDFAlgorithm cdfAlgorithm;
    
    public NormalDistributionImpl(final double mean, final double sd) {
        this.mean = 0.0;
        this.standardDeviation = 1.0;
        this.cdfAlgorithm = new NormalCDFPreciseAlgorithm();
        this.setMean(mean);
        this.setStandardDeviation(sd);
    }
    
    public NormalDistributionImpl() {
        this.mean = 0.0;
        this.standardDeviation = 1.0;
        this.cdfAlgorithm = new NormalCDFPreciseAlgorithm();
        this.setMean(0.0);
        this.setStandardDeviation(1.0);
    }
    
    public double getMean() {
        return this.mean;
    }
    
    public void setMean(final double mean) {
        this.mean = mean;
    }
    
    public double getStandardDeviation() {
        return this.standardDeviation;
    }
    
    public void setStandardDeviation(final double sd) {
        if (sd < 0.0) {
            throw new IllegalArgumentException("Standard deviation must bepositive or zero.");
        }
        this.standardDeviation = sd;
    }
    
    public double cumulativeProbability(final double x) {
        double z = x;
        if (this.standardDeviation > 0.0) {
            z = (x - this.mean) / this.standardDeviation;
            return this.cdfAlgorithm.cdf(z);
        }
        return 0.0;
    }
    
    public double inverseCumulativeProbability(final double p) {
        if (p < 0.0 || p > 1.0) {
            throw new IllegalArgumentException("p must be between 0.0 and 1.0, inclusive.");
        }
        if (this.standardDeviation == 0.0) {
            return this.mean;
        }
        final double q = p - 0.5;
        double val;
        if (Math.abs(q) <= 0.425) {
            final double r = 0.180625 - q * q;
            val = q * (((((((r * 2509.0809287301227 + 33430.57558358813) * r + 67265.7709270087) * r + 45921.95393154987) * r + 13731.69376550946) * r + 1971.5909503065513) * r + 133.14166789178438) * r + 3.3871328727963665) / (((((((r * 5226.495278852854 + 28729.085735721943) * r + 39307.89580009271) * r + 21213.794301586597) * r + 5394.196021424751) * r + 687.1870074920579) * r + 42.31333070160091) * r + 1.0);
        }
        else {
            double r;
            if (q > 0.0) {
                r = 1.0 - p;
            }
            else {
                r = p;
            }
            r = Math.sqrt(-Math.log(r));
            if (r <= 5.0) {
                r -= 1.6;
                val = (((((((r * 7.745450142783414E-4 + 0.022723844989269184) * r + 0.2417807251774506) * r + 1.2704582524523684) * r + 3.6478483247632045) * r + 5.769497221460691) * r + 4.630337846156546) * r + 1.4234371107496835) / (((((((r * 1.0507500716444169E-9 + 5.475938084995345E-4) * r + 0.015198666563616457) * r + 0.14810397642748008) * r + 0.6897673349851) * r + 1.6763848301838038) * r + 2.053191626637759) * r + 1.0);
            }
            else {
                r -= 5.0;
                val = (((((((r * 2.0103343992922881E-7 + 2.7115555687434876E-5) * r + 0.0012426609473880784) * r + 0.026532189526576124) * r + 0.29656057182850487) * r + 1.7848265399172913) * r + 5.463784911164114) * r + 6.657904643501103) / (((((((r * 2.0442631033899397E-15 + 1.421511758316446E-7) * r + 1.8463183175100548E-5) * r + 7.868691311456133E-4) * r + 0.014875361290850615) * r + 0.1369298809227358) * r + 0.599832206555888) * r + 1.0);
            }
            if (q < 0.0) {
                val = -val;
            }
        }
        return this.mean + this.standardDeviation * val;
    }
    
    public NormalCDFAlgorithm getCdfAlgorithm() {
        return this.cdfAlgorithm;
    }
    
    public void setCdfAlgorithm(final NormalCDFAlgorithm normalCDF) {
        this.cdfAlgorithm = normalCDF;
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
