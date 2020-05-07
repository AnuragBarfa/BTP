// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.math.linear;

public interface RealMatrix
{
    RealMatrix copy();
    
    RealMatrix add(final RealMatrix p0) throws IllegalArgumentException;
    
    RealMatrix subtract(final RealMatrix p0) throws IllegalArgumentException;
    
    int getRank();
    
    RealMatrix scalarAdd(final double p0);
    
    RealMatrix scalarMultiply(final double p0);
    
    RealMatrix multiply(final RealMatrix p0) throws IllegalArgumentException;
    
    double[][] getData();
    
    void setData(final double[][] p0);
    
    double getNorm();
    
    double[] getRow(final int p0) throws MatrixIndexException;
    
    double[] getColumn(final int p0) throws MatrixIndexException;
    
    double getEntry(final int p0, final int p1) throws MatrixIndexException;
    
    void setEntry(final int p0, final int p1, final double p2) throws MatrixIndexException;
    
    RealMatrix transpose();
    
    RealMatrix inverse() throws IllegalArgumentException;
    
    double getDeterminant();
    
    boolean isSquare();
    
    boolean isSingular();
    
    int getRowDimension();
    
    int getColumnDimension();
    
    double getTrace();
    
    double[] operate(final double[] p0) throws IllegalArgumentException;
    
    RealMatrix preMultiply(final double[] p0) throws IllegalArgumentException;
    
    double[] solve(final double[] p0) throws IllegalArgumentException, InvalidMatrixException;
    
    RealMatrix solve(final RealMatrix p0) throws IllegalArgumentException, InvalidMatrixException;
}
