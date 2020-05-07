// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.math.analysis;

import org.apache.commons.math.MathException;
import java.io.Serializable;

public class SecantSolver extends UnivariateRealSolverImpl implements Serializable
{
    public SecantSolver(final UnivariateRealFunction f) {
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
        double oldDelta = x3 - x2;
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
            if (Math.abs(oldDelta) < Math.max(super.relativeAccuracy * Math.abs(x2), super.absoluteAccuracy)) {
                this.setResult(x2, i);
                return super.result;
            }
            double delta;
            if (Math.abs(y2) > Math.abs(y0)) {
                delta = 0.5 * oldDelta;
            }
            else {
                delta = (x0 - x2) / (1.0 - y0 / y2);
                if (delta / oldDelta > 1.0) {
                    delta = 0.5 * oldDelta;
                }
            }
            x0 = x2;
            y0 = y2;
            x2 += delta;
            y2 = super.f.value(x2);
            if (y2 > 0.0 == y3 > 0.0) {
                x3 = x0;
                y3 = y0;
            }
            oldDelta = x3 - x2;
        }
        throw new MathException("Maximal iteration number exceeded");
    }
}
