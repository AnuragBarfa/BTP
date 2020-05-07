// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.math.distribution;

import org.apache.commons.math.MathException;

public interface DiscreteDistribution
{
    double probability(final int p0);
    
    double cumulativeProbability(final int p0) throws MathException;
    
    double cumulativeProbability(final int p0, final int p1) throws MathException;
    
    int inverseCumulativeProbability(final double p0) throws MathException;
}
