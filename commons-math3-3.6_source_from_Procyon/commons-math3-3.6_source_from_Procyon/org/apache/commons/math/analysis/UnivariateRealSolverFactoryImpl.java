// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.math.analysis;

import java.io.Serializable;

public class UnivariateRealSolverFactoryImpl extends UnivariateRealSolverFactory implements Serializable
{
    public UnivariateRealSolver newDefaultSolver(final UnivariateRealFunction f) {
        return this.newBrentSolver(f);
    }
    
    public UnivariateRealSolver newBisectionSolver(final UnivariateRealFunction f) {
        return new BisectionSolver(f);
    }
    
    public UnivariateRealSolver newBrentSolver(final UnivariateRealFunction f) {
        return new BrentSolver(f);
    }
    
    public UnivariateRealSolver newSecantSolver(final UnivariateRealFunction f) {
        return new SecantSolver(f);
    }
}
