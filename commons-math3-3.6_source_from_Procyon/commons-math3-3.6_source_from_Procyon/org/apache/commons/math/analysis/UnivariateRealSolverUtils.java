// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.math.analysis;

import org.apache.commons.math.MathException;

public class UnivariateRealSolverUtils
{
    private UnivariateRealSolverUtils() {
    }
    
    public static double solve(final UnivariateRealFunction f, final double x0, final double x1) throws MathException {
        if (f == null) {
            throw new IllegalArgumentException("f can not be null.");
        }
        return UnivariateRealSolverFactory.newInstance().newDefaultSolver(f).solve(x0, x1);
    }
    
    public static double solve(final UnivariateRealFunction f, final double x0, final double x1, final double absoluteAccuracy) throws MathException {
        if (f == null) {
            throw new IllegalArgumentException("f can not be null.");
        }
        final UnivariateRealSolver solver = UnivariateRealSolverFactory.newInstance().newDefaultSolver(f);
        solver.setAbsoluteAccuracy(absoluteAccuracy);
        return solver.solve(x0, x1);
    }
    
    public static double[] bracket(final UnivariateRealFunction function, final double initial, final double lowerBound, final double upperBound) throws MathException {
        return bracket(function, initial, lowerBound, upperBound, Integer.MAX_VALUE);
    }
    
    public static double[] bracket(final UnivariateRealFunction function, final double initial, final double lowerBound, final double upperBound, final int maximumIterations) throws MathException {
        double a = initial;
        double b = initial;
        int numIterations = 0;
        double fa;
        double fb;
        do {
            a = Math.max(a - 1.0, lowerBound);
            b = Math.min(b + 1.0, upperBound);
            fa = function.value(a);
            fb = function.value(b);
            ++numIterations;
        } while (fa * fb > 0.0 && numIterations < maximumIterations);
        return new double[] { a, b };
    }
}
