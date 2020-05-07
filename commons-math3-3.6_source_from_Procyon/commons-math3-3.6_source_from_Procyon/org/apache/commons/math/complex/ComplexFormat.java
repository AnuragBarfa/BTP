// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.math.complex;

import java.text.NumberFormat;

public class ComplexFormat
{
    private static final ComplexFormat DEFAULT;
    private String imaginaryCharacter;
    private int fractionDigits;
    
    public ComplexFormat() {
        this.imaginaryCharacter = "i";
        this.fractionDigits = 2;
    }
    
    public ComplexFormat(final String imaginaryCharacter) {
        this.imaginaryCharacter = "i";
        this.fractionDigits = 2;
        this.imaginaryCharacter = imaginaryCharacter;
    }
    
    public ComplexFormat(final String imaginaryCharacter, final int fractionDigits) {
        this.imaginaryCharacter = "i";
        this.fractionDigits = 2;
        this.imaginaryCharacter = imaginaryCharacter;
        this.fractionDigits = fractionDigits;
    }
    
    public String format(final Complex c) {
        final NumberFormat format = NumberFormat.getInstance();
        format.setMaximumFractionDigits(this.fractionDigits);
        final StringBuffer buffer = new StringBuffer();
        buffer.append(format.format(c.getReal()));
        if (c.getImaginary() < 0.0) {
            buffer.append(" - ");
            buffer.append(format.format(Math.abs(c.getImaginary())));
            buffer.append(this.imaginaryCharacter);
        }
        else if (c.getImaginary() > 0.0) {
            buffer.append(" + ");
            buffer.append(format.format(c.getImaginary()));
            buffer.append(this.imaginaryCharacter);
        }
        return buffer.toString();
    }
    
    public static String formatComplex(final Complex c) {
        return ComplexFormat.DEFAULT.format(c);
    }
    
    static {
        DEFAULT = new ComplexFormat();
    }
}
