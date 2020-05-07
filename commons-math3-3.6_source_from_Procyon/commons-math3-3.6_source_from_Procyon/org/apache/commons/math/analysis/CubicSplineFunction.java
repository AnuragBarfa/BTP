// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.math.analysis;

import org.apache.commons.math.MathException;
import java.util.Arrays;
import java.io.Serializable;

public class CubicSplineFunction implements UnivariateRealFunction, Serializable
{
    private double[] xval;
    private double[][] c;
    
    public CubicSplineFunction(final double[] xval, final double[][] c) {
        this.xval = xval;
        this.c = c;
    }
    
    public double value(double x) throws MathException {
        if (x < this.xval[0] || x > this.xval[this.xval.length - 1]) {
            throw new IllegalArgumentException("Argument outside domain");
        }
        int i = Arrays.binarySearch(this.xval, x);
        if (i < 0) {
            i = -i - 2;
        }
        x -= this.xval[i];
        return ((this.c[i][3] * x + this.c[i][2]) * x + this.c[i][1]) * x + this.c[i][0];
    }
    
    public double firstDerivative(double x) throws MathException {
        if (x < this.xval[0] || x > this.xval[this.xval.length - 1]) {
            throw new IllegalArgumentException("Argument outside domain");
        }
        int i = Arrays.binarySearch(this.xval, x);
        if (i < 0) {
            i = -i - 2;
        }
        x -= this.xval[i];
        return (3.0 * this.c[i][3] * x + 2.0 * this.c[i][2]) * x + this.c[i][1];
    }
    
    public double secondDerivative(double x) throws MathException {
        if (x < this.xval[0] || x > this.xval[this.xval.length - 1]) {
            throw new IllegalArgumentException("Argument outside domain");
        }
        int i = Arrays.binarySearch(this.xval, x);
        if (i < 0) {
            i = -i - 2;
        }
        x -= this.xval[i];
        return 6.0 * this.c[i][3] * x + 2.0 * this.c[i][2];
    }
}
