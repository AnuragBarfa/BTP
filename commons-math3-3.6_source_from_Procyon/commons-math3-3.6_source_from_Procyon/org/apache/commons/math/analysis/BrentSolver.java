// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.math.analysis;

import org.apache.commons.math.MathException;
import java.io.Serializable;

public class BrentSolver extends UnivariateRealSolverImpl implements Serializable
{
    public BrentSolver(final UnivariateRealFunction f) {
        super(f, 100, 1.0E-6);
    }
    
    public double solve(final double min, final double max, final double initial) throws MathException {
        return this.solve(min, max);
    }
    
    public double solve(final double min, final double max) throws MathException {
        this.clearResult();
        double x0 = min;
        double x2 = max;
        double y0 = super.f.value(x0);
        double y2 = super.f.value(x2);
        if (y0 > 0.0 == y2 > 0.0) {
            throw new MathException("Interval doesn't bracket a zero.");
        }
        double x3 = x0;
        double y3 = y0;
        double oldDelta;
        double delta = oldDelta = x2 - x0;
        for (int i = 0; i < super.maximalIterationCount; ++i) {
            if (Math.abs(y3) < Math.abs(y2)) {
                x0 = x2;
                x2 = x3;
                x3 = x0;
                y0 = y2;
                y2 = y3;
                y3 = y0;
            }
            if (Math.abs(y2) <= super.functionValueAccuracy) {
                this.setResult(x2, i);
                return super.result;
            }
            final double dx = x3 - x2;
            final double tolerance = Math.max(super.relativeAccuracy * Math.abs(x2), super.absoluteAccuracy);
            if (Math.abs(dx) <= tolerance) {
                this.setResult(x2, i);
                return super.result;
            }
            if (Math.abs(oldDelta) < tolerance || Math.abs(y0) <= Math.abs(y2)) {
                delta = (oldDelta = 0.5 * dx);
            }
            else {
                final double r3 = y2 / y0;
                double p;
                double p2;
                if (x0 == x3) {
                    p = dx * r3;
                    p2 = 1.0 - r3;
                }
                else {
                    final double r4 = y0 / y3;
                    final double r5 = y2 / y3;
                    p = r3 * (dx * r4 * (r4 - r5) - (x2 - x0) * (r5 - 1.0));
                    p2 = (r4 - 1.0) * (r5 - 1.0) * (r3 - 1.0);
                }
                if (p > 0.0) {
                    p2 = -p2;
                }
                else {
                    p = -p;
                }
                if (2.0 * p >= 1.5 * dx * p2 - Math.abs(tolerance * p2) || p >= Math.abs(0.5 * oldDelta * p2)) {
                    delta = (oldDelta = 0.5 * dx);
                }
                else {
                    oldDelta = delta;
                    delta = p / p2;
                }
            }
            x0 = x2;
            y0 = y2;
            if (Math.abs(delta) > tolerance) {
                x2 += delta;
            }
            else if (dx > 0.0) {
                x2 += 0.5 * tolerance;
            }
            else if (dx <= 0.0) {
                x2 -= 0.5 * tolerance;
            }
            y2 = super.f.value(x2);
            if (y2 > 0.0 == y3 > 0.0) {
                x3 = x0;
                y3 = y0;
                delta = (oldDelta = x2 - x0);
            }
        }
        throw new MathException("Maximal iteration number exceeded.");
    }
}
