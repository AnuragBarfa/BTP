// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.math.analysis;

import org.apache.commons.discovery.tools.DiscoverClass;

public abstract class UnivariateRealSolverFactory
{
    protected UnivariateRealSolverFactory() {
    }
    
    public static UnivariateRealSolverFactory newInstance() {
        UnivariateRealSolverFactory factory = null;
        try {
            final DiscoverClass dc = new DiscoverClass();
            factory = (UnivariateRealSolverFactory)dc.newInstance(UnivariateRealSolverFactory.class, "org.apache.commons.math.analysis.UnivariateRealSolverFactoryImpl");
        }
        catch (Exception ex) {}
        return factory;
    }
    
    public abstract UnivariateRealSolver newDefaultSolver(final UnivariateRealFunction p0);
    
    public abstract UnivariateRealSolver newBisectionSolver(final UnivariateRealFunction p0);
    
    public abstract UnivariateRealSolver newBrentSolver(final UnivariateRealFunction p0);
    
    public abstract UnivariateRealSolver newSecantSolver(final UnivariateRealFunction p0);
}
