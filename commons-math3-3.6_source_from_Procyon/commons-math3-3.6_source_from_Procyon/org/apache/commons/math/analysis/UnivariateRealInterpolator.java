// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.math.analysis;

import org.apache.commons.math.MathException;

public interface UnivariateRealInterpolator
{
    UnivariateRealFunction interpolate(final double[] p0, final double[] p1) throws MathException;
}
