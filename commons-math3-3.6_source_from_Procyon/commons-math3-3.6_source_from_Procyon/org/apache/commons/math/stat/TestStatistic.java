// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.math.stat;

import org.apache.commons.math.MathException;

public interface TestStatistic
{
    double chiSquare(final double[] p0, final double[] p1) throws IllegalArgumentException, MathException;
    
    double chiSquareTest(final double[] p0, final double[] p1) throws IllegalArgumentException, MathException;
    
    boolean chiSquareTest(final double[] p0, final double[] p1, final double p2) throws IllegalArgumentException, MathException;
    
    double t(final double p0, final double[] p1) throws IllegalArgumentException, MathException;
    
    double t(final double[] p0, final double[] p1) throws IllegalArgumentException, MathException;
    
    double tTest(final double[] p0, final double[] p1) throws IllegalArgumentException, MathException;
    
    boolean tTest(final double[] p0, final double[] p1, final double p2) throws IllegalArgumentException, MathException;
    
    boolean tTest(final double p0, final double[] p1, final double p2) throws IllegalArgumentException, MathException;
    
    double tTest(final double p0, final double[] p1) throws IllegalArgumentException, MathException;
    
    double t(final double p0, final StatisticalSummary p1) throws IllegalArgumentException, MathException;
    
    double t(final StatisticalSummary p0, final StatisticalSummary p1) throws IllegalArgumentException, MathException;
    
    double tTest(final StatisticalSummary p0, final StatisticalSummary p1) throws IllegalArgumentException, MathException;
    
    boolean tTest(final StatisticalSummary p0, final StatisticalSummary p1, final double p2) throws IllegalArgumentException, MathException;
    
    boolean tTest(final double p0, final StatisticalSummary p1, final double p2) throws IllegalArgumentException, MathException;
    
    double tTest(final double p0, final StatisticalSummary p1) throws IllegalArgumentException, MathException;
}
