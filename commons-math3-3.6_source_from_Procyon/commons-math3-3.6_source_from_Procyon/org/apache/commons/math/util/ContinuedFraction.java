// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.math.util;

import org.apache.commons.math.ConvergenceException;
import org.apache.commons.math.MathException;
import java.io.Serializable;

public abstract class ContinuedFraction implements Serializable
{
    private static final double DEFAULT_EPSILON = 1.0E-8;
    
    protected ContinuedFraction() {
    }
    
    protected abstract double getA(final int p0, final double p1);
    
    protected abstract double getB(final int p0, final double p1);
    
    public double evaluate(final double x) throws MathException {
        return this.evaluate(x, 1.0E-8, Integer.MAX_VALUE);
    }
    
    public double evaluate(final double x, final double epsilon) throws MathException {
        return this.evaluate(x, epsilon, Integer.MAX_VALUE);
    }
    
    public double evaluate(final double x, final int maxIterations) throws MathException {
        return this.evaluate(x, 1.0E-8, maxIterations);
    }
    
    public double evaluate(final double x, final double epsilon, final int maxIterations) throws MathException {
        final double[][] f = new double[2][2];
        final double[][] a = new double[2][2];
        final double[][] an = new double[2][2];
        a[0][0] = this.getA(0, x);
        a[0][1] = 1.0;
        a[1][0] = 1.0;
        a[1][1] = 0.0;
        return this.evaluate(1, x, a, an, f, epsilon, maxIterations);
    }
    
    private double evaluate(final int n, final double x, final double[][] a, final double[][] an, final double[][] f, final double epsilon, final int maxIterations) throws MathException {
        an[0][0] = this.getA(n, x);
        an[0][1] = 1.0;
        an[1][0] = this.getB(n, x);
        an[1][1] = 0.0;
        f[0][0] = a[0][0] * an[0][0] + a[0][1] * an[1][0];
        f[0][1] = a[0][0] * an[0][1] + a[0][1] * an[1][1];
        f[1][0] = a[1][0] * an[0][0] + a[1][1] * an[1][0];
        f[1][1] = a[1][0] * an[0][1] + a[1][1] * an[1][1];
        double ret;
        if (Math.abs(f[0][0] * f[1][1] - f[1][0] * f[0][1]) < Math.abs(epsilon * f[1][0] * f[1][1])) {
            ret = f[0][0] / f[1][0];
        }
        else {
            if (n >= maxIterations) {
                throw new ConvergenceException("Continued fraction convergents failed to converge.");
            }
            ret = this.evaluate(n + 1, x, f, an, a, epsilon, maxIterations);
        }
        return ret;
    }
}
