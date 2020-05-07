// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.math.util;

public final class MathUtils
{
    private static final byte ZB = 0;
    private static final byte NB = -1;
    private static final byte PB = 1;
    private static final short ZS = 0;
    private static final short NS = -1;
    private static final short PS = 1;
    
    private MathUtils() {
    }
    
    public static double sign(final double x) {
        if (Double.isNaN(x)) {
            return Double.NaN;
        }
        return (x == 0.0) ? 0.0 : ((x > 0.0) ? 1.0 : -1.0);
    }
    
    public static float sign(final float x) {
        if (Float.isNaN(x)) {
            return Float.NaN;
        }
        return (x == 0.0f) ? 0.0f : ((x > 0.0f) ? 1.0f : -1.0f);
    }
    
    public static byte sign(final byte x) {
        return (byte)((x == 0) ? 0 : ((x > 0) ? 1 : -1));
    }
    
    public static short sign(final short x) {
        return (short)((x == 0) ? 0 : ((x > 0) ? 1 : -1));
    }
    
    public static int sign(final int x) {
        return (x == 0) ? 0 : ((x > 0) ? 1 : -1);
    }
    
    public static long sign(final long x) {
        return (x == 0L) ? 0L : ((x > 0L) ? 1L : -1L);
    }
    
    public static double indicator(final double x) {
        if (Double.isNaN(x)) {
            return Double.NaN;
        }
        return (x >= 0.0) ? 1.0 : -1.0;
    }
    
    public static float indicator(final float x) {
        if (Float.isNaN(x)) {
            return Float.NaN;
        }
        return (x >= 0.0f) ? 1.0f : -1.0f;
    }
    
    public static byte indicator(final byte x) {
        return (byte)((x >= 0) ? 1 : -1);
    }
    
    public static short indicator(final short x) {
        return (short)((x > 0) ? 1 : -1);
    }
    
    public static int indicator(final int x) {
        return (x >= 0) ? 1 : -1;
    }
    
    public static long indicator(final long x) {
        return (x >= 0L) ? 1L : -1L;
    }
    
    public static long binomialCoefficient(final int n, final int k) {
        if (n < k) {
            throw new IllegalArgumentException("must have n >= k for binomial coefficient (n,k)");
        }
        if (n <= 0) {
            throw new IllegalArgumentException("must have n > 0 for binomial coefficient (n,k)");
        }
        if (n == k || k == 0) {
            return 1L;
        }
        if (k == 1 || k == n - 1) {
            return n;
        }
        final long result = Math.round(binomialCoefficientDouble(n, k));
        if (result == Long.MAX_VALUE) {
            throw new ArithmeticException("result too large to represent in a long integer");
        }
        return result;
    }
    
    public static double binomialCoefficientDouble(final int n, final int k) {
        return Math.floor(Math.exp(binomialCoefficientLog(n, k)) + 0.5);
    }
    
    public static double binomialCoefficientLog(final int n, final int k) {
        if (n < k) {
            throw new IllegalArgumentException("must have n >= k for binomial coefficient (n,k)");
        }
        if (n <= 0) {
            throw new IllegalArgumentException("must have n > 0 for binomial coefficient (n,k)");
        }
        if (n == k || k == 0) {
            return 0.0;
        }
        if (k == 1 || k == n - 1) {
            return Math.log(n);
        }
        double logSum = 0.0;
        for (int i = k + 1; i <= n; ++i) {
            logSum += Math.log(i);
        }
        for (int i = 2; i <= n - k; ++i) {
            logSum -= Math.log(i);
        }
        return logSum;
    }
    
    public static long factorial(final int n) {
        final long result = Math.round(factorialDouble(n));
        if (result == Long.MAX_VALUE) {
            throw new ArithmeticException("result too large to represent in a long integer");
        }
        return result;
    }
    
    public static double factorialDouble(final int n) {
        if (n <= 0) {
            throw new IllegalArgumentException("must have n > 0 for n!");
        }
        return Math.floor(Math.exp(factorialLog(n)) + 0.5);
    }
    
    public static double factorialLog(final int n) {
        if (n <= 0) {
            throw new IllegalArgumentException("must have n > 0 for n!");
        }
        double logSum = 0.0;
        for (int i = 2; i <= n; ++i) {
            logSum += Math.log(i);
        }
        return logSum;
    }
    
    public static double cosh(final double x) {
        return (Math.exp(x) + Math.exp(-x)) / 2.0;
    }
    
    public static double sinh(final double x) {
        return (Math.exp(x) - Math.exp(-x)) / 2.0;
    }
}
