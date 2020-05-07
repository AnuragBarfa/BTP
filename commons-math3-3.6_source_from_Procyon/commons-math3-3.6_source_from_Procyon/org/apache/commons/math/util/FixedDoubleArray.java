// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.math.util;

import java.io.Serializable;

public class FixedDoubleArray implements DoubleArray, Serializable
{
    static final long serialVersionUID = 1247853239629842963L;
    private double[] internalArray;
    private int size;
    private int nextAdd;
    private int maxElements;
    
    public FixedDoubleArray(final int maxElements) {
        this.size = 0;
        this.nextAdd = 0;
        this.maxElements = 0;
        this.maxElements = maxElements;
        this.internalArray = new double[maxElements];
    }
    
    public FixedDoubleArray(final double[] array) {
        this.size = 0;
        this.nextAdd = 0;
        this.maxElements = 0;
        this.maxElements = array.length;
        this.size = array.length;
        this.internalArray = array;
    }
    
    public int getNumElements() {
        return this.size;
    }
    
    public double getElement(final int index) {
        if (index > this.size - 1) {
            final String msg = "Attempted to retrieve an element outside of the element array";
            throw new ArrayIndexOutOfBoundsException(msg);
        }
        return this.internalArray[index];
    }
    
    public void setElement(final int index, final double value) {
        if (index > this.size - 1) {
            final String msg = "Attempted to set an element outside ofthe element array";
            throw new ArrayIndexOutOfBoundsException(msg);
        }
        this.internalArray[index] = value;
    }
    
    public void addElement(final double value) {
        if (this.size < this.internalArray.length) {
            ++this.size;
            this.internalArray[this.nextAdd] = value;
            ++this.nextAdd;
            this.nextAdd %= this.maxElements;
            return;
        }
        final String msg = "Attempted to add a value to an array of fixed size, please use addElementRolling to avoid this exception";
        throw new ArrayIndexOutOfBoundsException(msg);
    }
    
    public double addElementRolling(final double value) {
        double discarded = Double.NaN;
        if (this.size < this.internalArray.length) {
            ++this.size;
        }
        else {
            discarded = this.internalArray[this.nextAdd];
        }
        this.internalArray[this.nextAdd] = value;
        ++this.nextAdd;
        this.nextAdd %= this.maxElements;
        return discarded;
    }
    
    public double[] getElements() {
        final double[] copy = new double[this.size];
        System.arraycopy(this.internalArray, 0, copy, 0, this.size);
        return copy;
    }
    
    public double[] getValues() {
        return this.internalArray;
    }
    
    public int start() {
        return 0;
    }
    
    public void clear() {
        this.size = 0;
        this.nextAdd = 0;
        this.internalArray = new double[this.maxElements];
    }
}
