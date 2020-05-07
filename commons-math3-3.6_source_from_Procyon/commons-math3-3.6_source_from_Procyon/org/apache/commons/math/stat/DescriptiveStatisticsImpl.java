// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.math.stat;

import org.apache.commons.math.stat.univariate.UnivariateStatistic;
import java.util.Arrays;
import org.apache.commons.math.util.ContractableDoubleArray;
import java.io.Serializable;

public class DescriptiveStatisticsImpl extends AbstractDescriptiveStatistics implements Serializable
{
    protected int windowSize;
    protected ContractableDoubleArray eDA;
    
    public DescriptiveStatisticsImpl() {
        this.windowSize = -1;
        this.eDA = new ContractableDoubleArray();
    }
    
    public DescriptiveStatisticsImpl(final int window) {
        super(window);
        this.windowSize = -1;
        this.eDA = new ContractableDoubleArray();
    }
    
    public int getWindowSize() {
        return this.windowSize;
    }
    
    public double[] getValues() {
        final double[] copiedArray = new double[this.eDA.getNumElements()];
        System.arraycopy(this.eDA.getElements(), 0, copiedArray, 0, this.eDA.getNumElements());
        return copiedArray;
    }
    
    public double[] getSortedValues() {
        final double[] sort = this.getValues();
        Arrays.sort(sort);
        return sort;
    }
    
    public double getElement(final int index) {
        return this.eDA.getElement(index);
    }
    
    public long getN() {
        return this.eDA.getNumElements();
    }
    
    public synchronized void addValue(final double v) {
        if (this.windowSize != -1) {
            if (this.getN() == this.windowSize) {
                this.eDA.addElementRolling(v);
            }
            else {
                if (this.getN() >= this.windowSize) {
                    final String msg = "A window Univariate had more element than the windowSize.  This is an inconsistent state.";
                    throw new RuntimeException(msg);
                }
                this.eDA.addElement(v);
            }
        }
        else {
            this.eDA.addElement(v);
        }
    }
    
    public synchronized void clear() {
        this.eDA.clear();
    }
    
    public synchronized void setWindowSize(final int windowSize) {
        this.windowSize = windowSize;
        if (windowSize < this.eDA.getNumElements()) {
            this.eDA.discardFrontElements(this.eDA.getNumElements() - windowSize);
        }
    }
    
    public double apply(final UnivariateStatistic stat) {
        if (this.eDA != null) {
            return stat.evaluate(this.eDA.getValues(), this.eDA.start(), this.eDA.getNumElements());
        }
        return Double.NaN;
    }
}
