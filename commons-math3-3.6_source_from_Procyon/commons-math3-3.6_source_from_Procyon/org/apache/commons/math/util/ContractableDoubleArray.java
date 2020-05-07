// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.math.util;

import java.io.Serializable;

public class ContractableDoubleArray extends ExpandableDoubleArray implements Serializable
{
    static final long serialVersionUID = -3485529955529426875L;
    private float contractionCriteria;
    
    public ContractableDoubleArray() {
        this.contractionCriteria = 2.5f;
    }
    
    public ContractableDoubleArray(final int initialCapacity) {
        super(initialCapacity);
        this.contractionCriteria = 2.5f;
    }
    
    public ContractableDoubleArray(final int initialCapacity, final float expansionFactor) {
        this.contractionCriteria = 2.5f;
        super.expansionFactor = expansionFactor;
        this.setInitialCapacity(initialCapacity);
        super.internalArray = new double[initialCapacity];
        this.checkContractExpand(this.getContractionCriteria(), expansionFactor);
    }
    
    public ContractableDoubleArray(final int initialCapacity, final float expansionFactor, final float contractionCriteria) {
        this.contractionCriteria = 2.5f;
        this.contractionCriteria = contractionCriteria;
        super.expansionFactor = expansionFactor;
        this.setInitialCapacity(initialCapacity);
        super.internalArray = new double[initialCapacity];
        this.checkContractExpand(contractionCriteria, expansionFactor);
    }
    
    public synchronized void contract() {
        final double[] tempArray = new double[super.numElements + 1];
        System.arraycopy(super.internalArray, super.startIndex, tempArray, 0, super.numElements);
        super.internalArray = tempArray;
        super.startIndex = 0;
    }
    
    public synchronized void addElement(final double value) {
        super.addElement(value);
        if (this.shouldContract()) {
            this.contract();
        }
    }
    
    public synchronized double addElementRolling(final double value) {
        final double discarded = super.addElementRolling(value);
        if (this.shouldContract()) {
            this.contract();
        }
        return discarded;
    }
    
    private synchronized boolean shouldContract() {
        boolean shouldContract = false;
        if (super.internalArray.length / super.numElements > this.contractionCriteria) {
            shouldContract = true;
        }
        return shouldContract;
    }
    
    public synchronized void setElement(final int index, final double value) {
        super.setElement(index, value);
        if (this.shouldContract()) {
            this.contract();
        }
    }
    
    public void setExpansionFactor(final float expansionFactor) {
        this.checkContractExpand(this.getContractionCriteria(), expansionFactor);
        super.setExpansionFactor(expansionFactor);
    }
    
    public float getContractionCriteria() {
        return this.contractionCriteria;
    }
    
    public void setContractionCriteria(final float contractionCriteria) {
        this.checkContractExpand(contractionCriteria, this.getExpansionFactor());
        this.contractionCriteria = contractionCriteria;
    }
    
    protected void checkContractExpand(final float contractionCritera, final float expansionFactor) {
        if (contractionCritera < expansionFactor) {
            final String msg = "Contraction criteria can never be smaller than the expansion factor.  This would lead to a never ending loop of expansion and contraction as a newly expanded internal storage array would immediately satisfy the criteria for contraction";
            throw new IllegalArgumentException(msg);
        }
        if (this.contractionCriteria <= 1.0) {
            final String msg = "The contraction criteria must be a number larger than one.  If the contractionCriteria is less than or equal to one an endless loop of contraction and expansion would ensue as an internalArray.length == numElements would satisfy the contraction criteria";
            throw new IllegalArgumentException(msg);
        }
        if (expansionFactor < 1.0) {
            final String msg = "The expansion factor must be a number greater than 1.0";
            throw new IllegalArgumentException(msg);
        }
    }
    
    public synchronized void discardFrontElements(final int i) {
        super.discardFrontElements(i);
        if (this.shouldContract()) {
            this.contract();
        }
    }
}
