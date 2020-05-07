// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.math.analysis;

import org.apache.commons.math.MathException;
import java.io.Serializable;

public abstract class UnivariateRealSolverImpl implements UnivariateRealSolver, Serializable
{
    protected double absoluteAccuracy;
    protected double relativeAccuracy;
    protected double functionValueAccuracy;
    protected int maximalIterationCount;
    protected double defaultAbsoluteAccuracy;
    protected double defaultRelativeAccuracy;
    protected double defaultFunctionValueAccuracy;
    protected int defaultMaximalIterationCount;
    protected boolean resultComputed;
    protected double result;
    protected int iterationCount;
    protected UnivariateRealFunction f;
    
    protected UnivariateRealSolverImpl(final UnivariateRealFunction f, final int defaultMaximalIterationCount, final double defaultAbsoluteAccuracy) {
        this.resultComputed = false;
        this.f = f;
        this.defaultAbsoluteAccuracy = defaultAbsoluteAccuracy;
        this.defaultRelativeAccuracy = 1.0E-14;
        this.defaultFunctionValueAccuracy = 1.0E-15;
        this.absoluteAccuracy = defaultAbsoluteAccuracy;
        this.relativeAccuracy = this.defaultRelativeAccuracy;
        this.functionValueAccuracy = this.defaultFunctionValueAccuracy;
        this.defaultMaximalIterationCount = defaultMaximalIterationCount;
        this.maximalIterationCount = defaultMaximalIterationCount;
    }
    
    public double getResult() throws MathException {
        if (this.resultComputed) {
            return this.result;
        }
        throw new MathException("No result available");
    }
    
    public int getIterationCount() throws MathException {
        if (this.resultComputed) {
            return this.iterationCount;
        }
        throw new MathException("No result available");
    }
    
    protected final void setResult(final double result, final int iterationCount) {
        this.result = result;
        this.iterationCount = iterationCount;
        this.resultComputed = true;
    }
    
    protected final void clearResult() {
        this.resultComputed = false;
    }
    
    public void setAbsoluteAccuracy(final double accuracy) throws MathException {
        this.absoluteAccuracy = accuracy;
    }
    
    public double getAbsoluteAccuracy() {
        return this.absoluteAccuracy;
    }
    
    public void resetAbsoluteAccuracy() {
        this.absoluteAccuracy = this.defaultAbsoluteAccuracy;
    }
    
    public void setMaximalIterationCount(final int count) {
        this.maximalIterationCount = count;
    }
    
    public int getMaximalIterationCount() {
        return this.maximalIterationCount;
    }
    
    public void resetMaximalIterationCount() {
        this.maximalIterationCount = this.defaultMaximalIterationCount;
    }
    
    public void setRelativeAccuracy(final double accuracy) throws MathException {
        this.relativeAccuracy = accuracy;
    }
    
    public double getRelativeAccuracy() {
        return this.relativeAccuracy;
    }
    
    public void resetRelativeAccuracy() {
        this.relativeAccuracy = this.defaultRelativeAccuracy;
    }
    
    public void setFunctionValueAccuracy(final double accuracy) throws MathException {
        this.functionValueAccuracy = accuracy;
    }
    
    public double getFunctionValueAccuracy() {
        return this.functionValueAccuracy;
    }
    
    public void resetFunctionValueAccuracy() {
        this.functionValueAccuracy = this.defaultFunctionValueAccuracy;
    }
}
