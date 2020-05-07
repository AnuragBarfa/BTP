// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.math.distribution;

import org.apache.commons.math.MathException;

public interface ContinuousDistribution
{
    double cumulativeProbability(final double p0) throws MathException;
    
    double cumulativeProbability(final double p0, final double p1) throws MathException;
    
    double inverseCumulativeProbability(final double p0) throws MathException;
}
