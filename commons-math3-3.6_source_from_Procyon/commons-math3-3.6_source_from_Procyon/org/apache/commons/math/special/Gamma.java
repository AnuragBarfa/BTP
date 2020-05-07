// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.math.special;

import org.apache.commons.math.ConvergenceException;
import org.apache.commons.math.MathException;
import java.io.Serializable;

public class Gamma implements Serializable
{
    private static final double DEFAULT_EPSILON = 1.0E-8;
    private static double[] lanczos;
    
    private Gamma() {
    }
    
    public static double regularizedGammaP(final double a, final double x) throws MathException {
        return regularizedGammaP(a, x, 1.0E-8, Integer.MAX_VALUE);
    }
    
    public static double regularizedGammaP(final double a, final double x, final double epsilon, final int maxIterations) throws MathException {
        double ret;
        if (Double.isNaN(a) || Double.isNaN(x) || a <= 0.0 || x < 0.0) {
            ret = Double.NaN;
        }
        else if (x == 0.0) {
            ret = 0.0;
        }
        else {
            double n = 0.0;
            double sum;
            for (double an = sum = 1.0 / a; Math.abs(an) > epsilon && n < maxIterations; ++n, an *= x / (a + n), sum += an) {}
            if (n >= maxIterations) {
                throw new ConvergenceException("maximum number of iterations reached");
            }
            ret = Math.exp(-x + a * Math.log(x) - logGamma(a)) * sum;
        }
        return ret;
    }
    
    public static double logGamma(final double x) {
        double ret;
        if (Double.isNaN(x) || x <= 0.0) {
            ret = Double.NaN;
        }
        else {
            final double g = 4.7421875;
            double sum = 0.0;
            for (int i = 1; i < Gamma.lanczos.length; ++i) {
                sum += Gamma.lanczos[i] / (x + i);
            }
            sum += Gamma.lanczos[0];
            final double tmp = x + g + 0.5;
            ret = (x + 0.5) * Math.log(tmp) - tmp + 0.5 * Math.log(6.283185307179586) + Math.log(sum) - Math.log(x);
        }
        return ret;
    }
    
    static {
        Gamma.lanczos = new double[] { 0.9999999999999971, 57.15623566586292, -59.59796035547549, 14.136097974741746, -0.4919138160976202, 3.399464998481189E-5, 4.652362892704858E-5, -9.837447530487956E-5, 1.580887032249125E-4, -2.1026444172410488E-4, 2.1743961811521265E-4, -1.643181065367639E-4, 8.441822398385275E-5, -2.6190838401581408E-5, 3.6899182659531625E-6 };
    }
}
