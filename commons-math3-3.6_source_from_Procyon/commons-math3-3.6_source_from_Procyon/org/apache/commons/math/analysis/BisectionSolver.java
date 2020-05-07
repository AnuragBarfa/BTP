// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.math.analysis;

import org.apache.commons.math.MathException;
import java.io.Serializable;

public class BisectionSolver extends UnivariateRealSolverImpl implements Serializable
{
    public BisectionSolver(final UnivariateRealFunction f) {
        super(f, 100, 1.0E-6);
    }
    
    public double solve(final double min, final double max, final double initial) throws MathException {
        return this.solve(min, max);
    }
    
    public double solve(double min, double max) throws MathException {
        this.clearResult();
        for (int i = 0; i < super.maximalIterationCount; ++i) {
            double m = midpoint(min, max);
            double fmin = super.f.value(min);
            final double fm = super.f.value(m);
            if (fm * fmin > 0.0) {
                min = m;
                fmin = fm;
            }
            else {
                max = m;
            }
            if (Math.abs(max - min) <= super.absoluteAccuracy) {
                m = midpoint(min, max);
                this.setResult(m, i);
                return m;
            }
        }
        throw new MathException("Maximal iteration number exceeded");
    }
    
    public static double midpoint(final double a, final double b) {
        return (a + b) * 0.5;
    }
}
