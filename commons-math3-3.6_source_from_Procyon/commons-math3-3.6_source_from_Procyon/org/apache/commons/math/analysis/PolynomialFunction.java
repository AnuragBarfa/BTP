// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.math.analysis;

import org.apache.commons.math.MathException;
import java.io.Serializable;

public class PolynomialFunction implements UnivariateRealFunction, Serializable
{
    private double[] c;
    
    public PolynomialFunction(final double[] c) {
        this.c = c;
    }
    
    public double value(final double x) throws MathException {
        double value = this.c[0];
        for (int i = 1; i < this.c.length; ++i) {
            value += this.c[i] * Math.pow(x, i);
        }
        return value;
    }
    
    public double firstDerivative(final double x) throws MathException {
        double value = this.c[1];
        if (this.c.length > 1) {
            for (int i = 2; i < this.c.length; ++i) {
                value += i * this.c[i] * Math.pow(x, i - 1);
            }
        }
        return value;
    }
    
    public double secondDerivative(final double x) throws MathException {
        double value = 2.0 * this.c[2];
        if (this.c.length > 2) {
            for (int i = 3; i < this.c.length; ++i) {
                value += i * (i - 1) * this.c[i] * Math.pow(x, i - 2);
            }
        }
        return value;
    }
}
