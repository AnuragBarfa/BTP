// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.math.util;

public interface DoubleArray
{
    int getNumElements();
    
    double getElement(final int p0);
    
    void setElement(final int p0, final double p1);
    
    void addElement(final double p0);
    
    double addElementRolling(final double p0);
    
    double[] getElements();
    
    void clear();
}
