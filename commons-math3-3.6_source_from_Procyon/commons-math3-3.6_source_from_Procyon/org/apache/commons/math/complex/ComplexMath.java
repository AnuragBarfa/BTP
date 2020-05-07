// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.math.complex;

import org.apache.commons.math.util.MathUtils;

public class ComplexMath
{
    private ComplexMath() {
    }
    
    public static Complex acos(final Complex z) {
        if (z.isNaN()) {
            return Complex.NaN;
        }
        return Complex.I.negate().multiply(log(z.add(Complex.I.multiply(sqrt1z(z)))));
    }
    
    public static Complex asin(final Complex z) {
        if (z.isNaN()) {
            return Complex.NaN;
        }
        return Complex.I.negate().multiply(log(sqrt1z(z).add(Complex.I.multiply(z))));
    }
    
    public static Complex atan(final Complex z) {
        if (z.isNaN()) {
            return Complex.NaN;
        }
        return Complex.I.multiply(log(Complex.I.add(z).divide(Complex.I.subtract(z)))).divide(new Complex(2.0, 0.0));
    }
    
    public static Complex cos(final Complex z) {
        if (z.isNaN()) {
            return Complex.NaN;
        }
        final double a = z.getReal();
        final double b = z.getImaginary();
        return new Complex(Math.cos(a) * MathUtils.cosh(b), -Math.sin(a) * MathUtils.sinh(b));
    }
    
    public static Complex cosh(final Complex z) {
        if (z.isNaN()) {
            return Complex.NaN;
        }
        final double a = z.getReal();
        final double b = z.getImaginary();
        return new Complex(MathUtils.cosh(a) * Math.cos(b), MathUtils.sinh(a) * Math.sin(b));
    }
    
    public static Complex exp(final Complex z) {
        if (z.isNaN()) {
            return Complex.NaN;
        }
        final double b = z.getImaginary();
        final double expA = Math.exp(z.getReal());
        final double sinB = Math.sin(b);
        final double cosB = Math.cos(b);
        return new Complex(expA * cosB, expA * sinB);
    }
    
    public static Complex log(final Complex z) {
        if (z.isNaN()) {
            return Complex.NaN;
        }
        return new Complex(Math.log(z.abs()), Math.atan2(z.getImaginary(), z.getReal()));
    }
    
    public static Complex pow(final Complex y, final Complex x) {
        return exp(x.multiply(log(y)));
    }
    
    public static Complex sin(final Complex z) {
        if (z.isNaN()) {
            return Complex.NaN;
        }
        final double a = z.getReal();
        final double b = z.getImaginary();
        return new Complex(Math.sin(a) * MathUtils.cosh(b), Math.cos(a) * MathUtils.sinh(b));
    }
    
    public static Complex sinh(final Complex z) {
        if (z.isNaN()) {
            return Complex.NaN;
        }
        final double a = z.getReal();
        final double b = z.getImaginary();
        return new Complex(MathUtils.sinh(a) * Math.cos(b), MathUtils.cosh(a) * Math.sin(b));
    }
    
    public static Complex sqrt(final Complex z) {
        if (z.isNaN()) {
            return Complex.NaN;
        }
        final double a = z.getReal();
        final double b = z.getImaginary();
        final double t = Math.sqrt((Math.abs(a) + z.abs()) / 2.0);
        if (a >= 0.0) {
            return new Complex(t, b / (2.0 * t));
        }
        return new Complex(Math.abs(z.getImaginary()) / (2.0 * t), MathUtils.indicator(b) * t);
    }
    
    public static Complex sqrt1z(final Complex z) {
        return sqrt(Complex.ONE.subtract(z.multiply(z)));
    }
    
    public static Complex tan(final Complex z) {
        if (z.isNaN()) {
            return Complex.NaN;
        }
        final double a2 = 2.0 * z.getReal();
        final double b2 = 2.0 * z.getImaginary();
        final double d = Math.cos(a2) + MathUtils.cosh(b2);
        return new Complex(Math.sin(a2) / d, MathUtils.sinh(b2) / d);
    }
    
    public static Complex tanh(final Complex z) {
        if (z.isNaN()) {
            return Complex.NaN;
        }
        final double a2 = 2.0 * z.getReal();
        final double b2 = 2.0 * z.getImaginary();
        final double d = MathUtils.cosh(a2) + Math.cos(b2);
        return new Complex(MathUtils.sinh(a2) / d, Math.sin(b2) / d);
    }
}
