// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.math.special;

import org.apache.commons.math.util.ContinuedFraction;
import org.apache.commons.math.MathException;
import java.io.Serializable;

public class Beta implements Serializable
{
    private static final double DEFAULT_EPSILON = 1.0E-8;
    
    private Beta() {
    }
    
    public static double regularizedBeta(final double x, final double a, final double b) throws MathException {
        return regularizedBeta(x, a, b, 1.0E-8, Integer.MAX_VALUE);
    }
    
    public static double regularizedBeta(final double x, final double a, final double b, final double epsilon) throws MathException {
        return regularizedBeta(x, a, b, epsilon, Integer.MAX_VALUE);
    }
    
    public static double regularizedBeta(final double x, final double a, final double b, final int maxIterations) throws MathException {
        return regularizedBeta(x, a, b, 1.0E-8, maxIterations);
    }
    
    public static double regularizedBeta(final double x, final double a, final double b, final double epsilon, final int maxIterations) throws MathException {
        double ret;
        if (Double.isNaN(x) || Double.isNaN(a) || Double.isNaN(b) || x < 0.0 || x > 1.0 || a <= 0.0 || b <= 0.0) {
            ret = Double.NaN;
        }
        else if (x > (a + 1.0) / (a + b + 1.0)) {
            ret = 1.0 - regularizedBeta(1.0 - x, b, a, epsilon, maxIterations);
        }
        else {
            final ContinuedFraction fraction = new ContinuedFraction() {
                protected double getB(final int n, final double x) {
                    double ret = 0.0;
                    switch (n) {
                        case 1: {
                            ret = 1.0;
                            break;
                        }
                        default: {
                            if (n % 2 == 0) {
                                final double m = (n - 2.0) / 2.0;
                                ret = -((a + m) * (a + b + m) * x) / ((a + 2.0 * m) * (a + 2.0 * m + 1.0));
                                break;
                            }
                            final double m = (n - 1.0) / 2.0;
                            ret = m * (b - m) * x / ((a + 2.0 * m - 1.0) * (a + 2.0 * m));
                            break;
                        }
                    }
                    return ret;
                }
                
                protected double getA(final int n, final double x) {
                    double ret = 0.0;
                    switch (n) {
                        case 0: {
                            ret = 0.0;
                            break;
                        }
                        default: {
                            ret = 1.0;
                            break;
                        }
                    }
                    return ret;
                }
            };
            ret = Math.exp(a * Math.log(x) + b * Math.log(1.0 - x) - Math.log(a) - logBeta(a, b, epsilon, maxIterations)) * fraction.evaluate(x, epsilon, maxIterations);
        }
        return ret;
    }
    
    public static double logBeta(final double a, final double b) {
        return logBeta(a, b, 1.0E-8, Integer.MAX_VALUE);
    }
    
    public static double logBeta(final double a, final double b, final double epsilon, final int maxIterations) {
        double ret;
        if (Double.isNaN(a) || Double.isNaN(b) || a <= 0.0 || b <= 0.0) {
            ret = Double.NaN;
        }
        else {
            ret = Gamma.logGamma(a) + Gamma.logGamma(b) - Gamma.logGamma(a + b);
        }
        return ret;
    }
}
