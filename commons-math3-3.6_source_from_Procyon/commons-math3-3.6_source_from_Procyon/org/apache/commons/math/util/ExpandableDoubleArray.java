// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.math.util;

import java.io.Serializable;

public class ExpandableDoubleArray implements Serializable, DoubleArray
{
    static final long serialVersionUID = -5697417774251632284L;
    protected double[] internalArray;
    protected int numElements;
    protected int startIndex;
    protected int initialCapacity;
    protected float expansionFactor;
    
    public ExpandableDoubleArray() {
        this.numElements = 0;
        this.startIndex = 0;
        this.initialCapacity = 16;
        this.expansionFactor = 2.0f;
        this.internalArray = new double[this.initialCapacity];
    }
    
    public ExpandableDoubleArray(final int initialCapacity) {
        this.numElements = 0;
        this.startIndex = 0;
        this.initialCapacity = 16;
        this.expansionFactor = 2.0f;
        this.setInitialCapacity(initialCapacity);
        this.internalArray = new double[this.initialCapacity];
    }
    
    public ExpandableDoubleArray(final int initialCapacity, final float expansionFactor) {
        this.numElements = 0;
        this.startIndex = 0;
        this.initialCapacity = 16;
        this.expansionFactor = 2.0f;
        this.setInitialCapacity(initialCapacity);
        this.setExpansionFactor(expansionFactor);
        this.initialCapacity = initialCapacity;
        this.internalArray = new double[initialCapacity];
    }
    
    public float getExpansionFactor() {
        return this.expansionFactor;
    }
    
    public void setExpansionFactor(final float expansionFactor) {
        if (expansionFactor > 1.0) {
            this.expansionFactor = expansionFactor;
            return;
        }
        final String msg = "The expansion factor must be a number greater than 1.0";
        throw new IllegalArgumentException(msg);
    }
    
    public void setInitialCapacity(final int initialCapacity) {
        if (initialCapacity > 0) {
            this.initialCapacity = initialCapacity;
            return;
        }
        final String msg = "The initial capacity supplied: " + initialCapacity + "must be a positive integer";
        throw new IllegalArgumentException(msg);
    }
    
    public double[] getValues() {
        return this.internalArray;
    }
    
    public int start() {
        return this.startIndex;
    }
    
    public int getNumElements() {
        return this.numElements;
    }
    
    public synchronized void setNumElements(final int i) {
        if (i < 0) {
            final String msg = "Number of elements must be zero or a positive integer";
            throw new IllegalArgumentException(msg);
        }
        if (this.startIndex + i > this.internalArray.length) {
            this.expandTo(this.startIndex + i);
        }
        this.numElements = i;
    }
    
    public double getElement(final int index) {
        double value = Double.NaN;
        if (index >= this.numElements) {
            final String msg = "The index specified: " + index + " is larger than the current number of elements";
            throw new ArrayIndexOutOfBoundsException(msg);
        }
        if (index >= 0) {
            value = this.internalArray[this.startIndex + index];
            return value;
        }
        final String msg = "Elements cannot be retrieved from a negative array index";
        throw new ArrayIndexOutOfBoundsException(msg);
    }
    
    public synchronized void setElement(final int index, final double value) {
        if (index < 0) {
            final String msg = "Cannot set an element at a negative index";
            throw new ArrayIndexOutOfBoundsException(msg);
        }
        if (this.startIndex + index >= this.internalArray.length) {
            this.expandTo(this.startIndex + (index + 1));
            this.numElements = index + 1;
        }
        this.internalArray[this.startIndex + index] = value;
    }
    
    private synchronized void expandTo(final int size) {
        final double[] tempArray = new double[size];
        System.arraycopy(this.internalArray, 0, tempArray, 0, this.internalArray.length);
        this.internalArray = tempArray;
    }
    
    protected synchronized void expand() {
        final int newSize = (int)Math.ceil(this.internalArray.length * this.expansionFactor);
        final double[] tempArray = new double[newSize];
        System.arraycopy(this.internalArray, 0, tempArray, 0, this.internalArray.length);
        this.internalArray = tempArray;
    }
    
    public synchronized void addElement(final double value) {
        ++this.numElements;
        if (this.startIndex + this.numElements > this.internalArray.length) {
            this.expand();
        }
        this.internalArray[this.startIndex + (this.numElements - 1)] = value;
    }
    
    public synchronized double addElementRolling(final double value) {
        final double discarded = this.internalArray[this.startIndex];
        if (this.startIndex + (this.numElements + 1) > this.internalArray.length) {
            this.expand();
        }
        ++this.startIndex;
        this.internalArray[this.startIndex + (this.numElements - 1)] = value;
        return discarded;
    }
    
    int getInternalLength() {
        return this.internalArray.length;
    }
    
    public synchronized void clear() {
        this.numElements = 0;
        this.internalArray = new double[this.initialCapacity];
    }
    
    public synchronized void discardFrontElements(final int i) {
        if (i > this.numElements) {
            final String msg = "Cannot discard more elements than arecontained in this array.";
            throw new IllegalArgumentException(msg);
        }
        if (i < 0) {
            final String msg = "Cannot discard a negative number of elements.";
            throw new IllegalArgumentException(msg);
        }
        this.numElements -= i;
        this.startIndex += i;
    }
    
    public double[] getElements() {
        final double[] elementArray = new double[this.numElements];
        System.arraycopy(this.internalArray, this.startIndex, elementArray, 0, this.numElements);
        return elementArray;
    }
}
