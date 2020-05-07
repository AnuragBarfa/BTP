// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.math.complex;

import java.io.Serializable;

public class Complex implements Serializable
{
    public static final Complex I;
    public static final Complex NaN;
    public static final Complex ONE;
    protected double imaginary;
    protected double real;
    
    public Complex(final double real, final double imaginary) {
        this.real = real;
        this.imaginary = imaginary;
    }
    
    public double abs() {
        if (this.isNaN()) {
            return Double.NaN;
        }
        return Math.sqrt(this.squareSum());
    }
    
    public Complex add(final Complex rhs) {
        if (this.isNaN() || rhs.isNaN()) {
            return Complex.NaN;
        }
        return new Complex(this.real + rhs.getReal(), this.imaginary + rhs.getImaginary());
    }
    
    public Complex conjugate() {
        if (this.isNaN()) {
            return Complex.NaN;
        }
        return new Complex(this.real, -this.imaginary);
    }
    
    public Complex divide(final Complex rhs) {
        if (this.isNaN() || rhs.isNaN()) {
            return Complex.NaN;
        }
        if (Math.abs(rhs.getReal()) < Math.abs(rhs.getImaginary())) {
            final double q = rhs.getReal() / rhs.getImaginary();
            final double d = rhs.getReal() * q + rhs.getImaginary();
            return new Complex((this.real * q + this.imaginary) / d, (this.imaginary * q - this.real) / d);
        }
        final double q = rhs.getImaginary() / rhs.getReal();
        final double d = rhs.getImaginary() * q + rhs.getReal();
        return new Complex((this.imaginary * q + this.real) / d, (this.imaginary - this.real * q) / d);
    }
    
    public boolean equals(final Object other) {
        boolean ret;
        if (this == other) {
            ret = true;
        }
        else if (other == null) {
            ret = false;
        }
        else {
            try {
                final Complex rhs = (Complex)other;
                ret = (Double.doubleToRawLongBits(this.real) == Double.doubleToRawLongBits(rhs.getReal()) && Double.doubleToRawLongBits(this.imaginary) == Double.doubleToRawLongBits(rhs.getImaginary()));
            }
            catch (ClassCastException ex) {
                ret = false;
            }
        }
        return ret;
    }
    
    public double getImaginary() {
        return this.imaginary;
    }
    
    public double getReal() {
        return this.real;
    }
    
    public boolean isNaN() {
        return Double.isNaN(this.real) || Double.isNaN(this.imaginary);
    }
    
    public Complex multiply(final Complex rhs) {
        if (this.isNaN() || rhs.isNaN()) {
            return Complex.NaN;
        }
        final double p = (this.real + this.imaginary) * (rhs.getReal() + rhs.getImaginary());
        final double ac = this.real * rhs.getReal();
        final double bd = this.imaginary * rhs.getImaginary();
        return new Complex(ac - bd, p - ac - bd);
    }
    
    public Complex negate() {
        if (this.isNaN()) {
            return Complex.NaN;
        }
        return new Complex(-this.real, -this.imaginary);
    }
    
    private double squareSum() {
        return this.real * this.real + this.imaginary * this.imaginary;
    }
    
    public Complex subtract(final Complex rhs) {
        if (this.isNaN() || rhs.isNaN()) {
            return Complex.NaN;
        }
        return new Complex(this.real - rhs.getReal(), this.imaginary - rhs.getImaginary());
    }
    
    static {
        I = new Complex(0.0, 1.0);
        NaN = new Complex(Double.NaN, Double.NaN);
        ONE = new Complex(1.0, 0.0);
    }
}
