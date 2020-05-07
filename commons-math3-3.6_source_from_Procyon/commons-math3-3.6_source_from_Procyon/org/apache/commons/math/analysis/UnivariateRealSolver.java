// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.math.analysis;

import org.apache.commons.math.MathException;

public interface UnivariateRealSolver
{
    void setMaximalIterationCount(final int p0);
    
    int getMaximalIterationCount();
    
    void resetMaximalIterationCount();
    
    void setAbsoluteAccuracy(final double p0) throws MathException;
    
    double getAbsoluteAccuracy();
    
    void resetAbsoluteAccuracy();
    
    void setRelativeAccuracy(final double p0) throws MathException;
    
    double getRelativeAccuracy();
    
    void resetRelativeAccuracy();
    
    void setFunctionValueAccuracy(final double p0) throws MathException;
    
    double getFunctionValueAccuracy();
    
    void resetFunctionValueAccuracy();
    
    double solve(final double p0, final double p1) throws MathException;
    
    double solve(final double p0, final double p1, final double p2) throws MathException;
    
    double getResult() throws MathException;
    
    int getIterationCount() throws MathException;
}
