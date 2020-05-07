// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.math.random;

import java.util.Collection;

public interface RandomData
{
    String nextHexString(final int p0);
    
    int nextInt(final int p0, final int p1);
    
    long nextLong(final long p0, final long p1);
    
    String nextSecureHexString(final int p0);
    
    int nextSecureInt(final int p0, final int p1);
    
    long nextSecureLong(final long p0, final long p1);
    
    long nextPoisson(final double p0);
    
    double nextGaussian(final double p0, final double p1);
    
    double nextExponential(final double p0);
    
    double nextUniform(final double p0, final double p1);
    
    int[] nextPermutation(final int p0, final int p1);
    
    Object[] nextSample(final Collection p0, final int p1);
}
