// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.math.linear;

import java.io.Serializable;

public class RealMatrixImpl implements RealMatrix, Serializable
{
    private double[][] data;
    private double[][] lu;
    private int[] pivot;
    private int parity;
    private static double TOO_SMALL;
    
    public RealMatrixImpl() {
        this.data = null;
        this.lu = null;
        this.pivot = null;
        this.parity = 1;
    }
    
    public RealMatrixImpl(final int rowDimension, final int columnDimension) {
        this.data = null;
        this.lu = null;
        this.pivot = null;
        this.parity = 1;
        this.data = new double[rowDimension][columnDimension];
        this.lu = null;
    }
    
    public RealMatrixImpl(final double[][] d) {
        this.data = null;
        this.lu = null;
        this.pivot = null;
        this.parity = 1;
        this.copyIn(d);
        this.lu = null;
    }
    
    public RealMatrixImpl(final double[] v) {
        this.data = null;
        this.lu = null;
        this.pivot = null;
        this.parity = 1;
        final int nRows = v.length;
        this.data = new double[nRows][1];
        for (int row = 0; row < nRows; ++row) {
            this.data[row][0] = v[row];
        }
    }
    
    public RealMatrix copy() {
        return new RealMatrixImpl(this.copyOut());
    }
    
    public RealMatrix add(final RealMatrix m) throws IllegalArgumentException {
        if (this.getColumnDimension() != m.getColumnDimension() || this.getRowDimension() != m.getRowDimension()) {
            throw new IllegalArgumentException("matrix dimension mismatch");
        }
        final int rowCount = this.getRowDimension();
        final int columnCount = this.getColumnDimension();
        final double[][] outData = new double[rowCount][columnCount];
        final double[][] mData = m.getData();
        for (int row = 0; row < rowCount; ++row) {
            for (int col = 0; col < columnCount; ++col) {
                outData[row][col] = this.data[row][col] + mData[row][col];
            }
        }
        return new RealMatrixImpl(outData);
    }
    
    public RealMatrix subtract(final RealMatrix m) throws IllegalArgumentException {
        if (this.getColumnDimension() != m.getColumnDimension() || this.getRowDimension() != m.getRowDimension()) {
            throw new IllegalArgumentException("matrix dimension mismatch");
        }
        final int rowCount = this.getRowDimension();
        final int columnCount = this.getColumnDimension();
        final double[][] outData = new double[rowCount][columnCount];
        final double[][] mData = m.getData();
        for (int row = 0; row < rowCount; ++row) {
            for (int col = 0; col < columnCount; ++col) {
                outData[row][col] = this.data[row][col] - mData[row][col];
            }
        }
        return new RealMatrixImpl(outData);
    }
    
    public int getRank() {
        throw new UnsupportedOperationException("not implemented yet");
    }
    
    public RealMatrix scalarAdd(final double d) {
        final int rowCount = this.getRowDimension();
        final int columnCount = this.getColumnDimension();
        final double[][] outData = new double[rowCount][columnCount];
        for (int row = 0; row < rowCount; ++row) {
            for (int col = 0; col < columnCount; ++col) {
                outData[row][col] = this.data[row][col] + d;
            }
        }
        return new RealMatrixImpl(outData);
    }
    
    public RealMatrix scalarMultiply(final double d) {
        final int rowCount = this.getRowDimension();
        final int columnCount = this.getColumnDimension();
        final double[][] outData = new double[rowCount][columnCount];
        for (int row = 0; row < rowCount; ++row) {
            for (int col = 0; col < columnCount; ++col) {
                outData[row][col] = this.data[row][col] * d;
            }
        }
        return new RealMatrixImpl(outData);
    }
    
    public RealMatrix multiply(final RealMatrix m) throws IllegalArgumentException {
        if (this.getColumnDimension() != m.getRowDimension()) {
            throw new IllegalArgumentException("Matrices are not multiplication compatible.");
        }
        final int nRows = this.getRowDimension();
        final int nCols = m.getColumnDimension();
        final int nSum = this.getColumnDimension();
        final double[][] mData = m.getData();
        final double[][] outData = new double[nRows][nCols];
        double sum = 0.0;
        for (int row = 0; row < nRows; ++row) {
            for (int col = 0; col < nCols; ++col) {
                sum = 0.0;
                for (int i = 0; i < nSum; ++i) {
                    sum += this.data[row][i] * mData[i][col];
                }
                outData[row][col] = sum;
            }
        }
        return new RealMatrixImpl(outData);
    }
    
    public double[][] getData() {
        return this.copyOut();
    }
    
    public void setData(final double[][] inData) {
        this.copyIn(inData);
        this.lu = null;
    }
    
    public double[][] getDataRef() {
        return this.data;
    }
    
    public void setDataRef(final double[][] inData) {
        this.data = inData;
        this.lu = null;
    }
    
    public double getNorm() {
        double maxColSum = 0.0;
        for (int col = 0; col < this.getColumnDimension(); ++col) {
            double sum = 0.0;
            for (int row = 0; row < this.getRowDimension(); ++row) {
                sum += Math.abs(this.data[row][col]);
            }
            maxColSum = Math.max(maxColSum, sum);
        }
        return maxColSum;
    }
    
    public double[] getRow(final int row) throws MatrixIndexException {
        if (!this.isValidCoordinate(row, 1)) {
            throw new MatrixIndexException("illegal row argument");
        }
        final int ncols = this.getColumnDimension();
        final double[] out = new double[ncols];
        System.arraycopy(this.data[row - 1], 0, out, 0, ncols);
        return out;
    }
    
    public double[] getColumn(final int col) throws MatrixIndexException {
        if (!this.isValidCoordinate(1, col)) {
            throw new MatrixIndexException("illegal column argument");
        }
        final int nRows = this.getRowDimension();
        final double[] out = new double[nRows];
        for (int row = 0; row < nRows; ++row) {
            out[row] = this.data[row][col - 1];
        }
        return out;
    }
    
    public double getEntry(final int row, final int column) throws MatrixIndexException {
        if (!this.isValidCoordinate(row, column)) {
            throw new MatrixIndexException("matrix entry does not exist");
        }
        return this.data[row - 1][column - 1];
    }
    
    public void setEntry(final int row, final int column, final double value) throws MatrixIndexException {
        if (!this.isValidCoordinate(row, column)) {
            throw new MatrixIndexException("matrix entry does not exist");
        }
        this.data[row - 1][column - 1] = value;
        this.lu = null;
    }
    
    public RealMatrix transpose() {
        final int nRows = this.getRowDimension();
        final int nCols = this.getColumnDimension();
        final RealMatrixImpl out = new RealMatrixImpl(nCols, nRows);
        final double[][] outData = out.getDataRef();
        for (int row = 0; row < nRows; ++row) {
            for (int col = 0; col < nCols; ++col) {
                outData[col][row] = this.data[row][col];
            }
        }
        return out;
    }
    
    public RealMatrix inverse() throws IllegalArgumentException {
        return this.solve(this.getIdentity(this.getRowDimension()));
    }
    
    public double getDeterminant() throws InvalidMatrixException {
        if (!this.isSquare()) {
            throw new InvalidMatrixException("matrix is not square");
        }
        if (this.isSingular()) {
            return 0.0;
        }
        double det = this.parity;
        for (int i = 0; i < this.getRowDimension(); ++i) {
            det *= this.lu[i][i];
        }
        return det;
    }
    
    public boolean isSquare() {
        return this.getColumnDimension() == this.getRowDimension();
    }
    
    public boolean isSingular() {
        if (this.lu == null) {
            try {
                this.LUDecompose();
                return false;
            }
            catch (InvalidMatrixException ex) {
                return true;
            }
        }
        return false;
    }
    
    public int getRowDimension() {
        return this.data.length;
    }
    
    public int getColumnDimension() {
        return this.data[0].length;
    }
    
    public double getTrace() throws IllegalArgumentException {
        if (!this.isSquare()) {
            throw new IllegalArgumentException("matrix is not square");
        }
        double trace = this.data[0][0];
        for (int i = 1; i < this.getRowDimension(); ++i) {
            trace += this.data[i][i];
        }
        return trace;
    }
    
    public double[] operate(final double[] v) throws IllegalArgumentException {
        if (v.length != this.getColumnDimension()) {
            throw new IllegalArgumentException("vector has wrong length");
        }
        final int nRows = this.getRowDimension();
        final int nCols = this.getColumnDimension();
        final double[] out = new double[v.length];
        for (int row = 0; row < nRows; ++row) {
            double sum = 0.0;
            for (int i = 0; i < nCols; ++i) {
                sum += this.data[row][i] * v[i];
            }
            out[row] = sum;
        }
        return out;
    }
    
    public RealMatrix preMultiply(final double[] v) throws IllegalArgumentException {
        final int nCols = this.getColumnDimension();
        if (v.length != nCols) {
            throw new IllegalArgumentException("vector has wrong length");
        }
        final RealMatrix pm = new RealMatrixImpl(v).transpose();
        return pm.multiply(this);
    }
    
    public double[] solve(final double[] b) throws IllegalArgumentException, InvalidMatrixException {
        final int nRows = this.getRowDimension();
        if (b.length != nRows) {
            throw new IllegalArgumentException("constant vector has wrong length");
        }
        final RealMatrix bMatrix = new RealMatrixImpl(b);
        final double[][] solution = ((RealMatrixImpl)this.solve(bMatrix)).getDataRef();
        final double[] out = new double[nRows];
        for (int row = 0; row < nRows; ++row) {
            out[row] = solution[row][0];
        }
        return out;
    }
    
    public RealMatrix solve(final RealMatrix b) throws IllegalArgumentException, InvalidMatrixException {
        if (b.getRowDimension() != this.getRowDimension()) {
            throw new IllegalArgumentException("Incorrect row dimension");
        }
        if (!this.isSquare()) {
            throw new InvalidMatrixException("coefficient matrix is not square");
        }
        if (this.isSingular()) {
            throw new InvalidMatrixException("Matrix is singular.");
        }
        final int nCol = this.getColumnDimension();
        final int nColB = b.getColumnDimension();
        final int nRowB = b.getRowDimension();
        double[][] bv = b.getData();
        final double[][] bp = new double[nRowB][nColB];
        for (int row = 0; row < nRowB; ++row) {
            for (int col = 0; col < nColB; ++col) {
                bp[row][col] = bv[this.pivot[row]][col];
            }
        }
        bv = null;
        for (int col2 = 0; col2 < nCol; ++col2) {
            for (int i = col2 + 1; i < nCol; ++i) {
                for (int j = 0; j < nColB; ++j) {
                    final double[] array = bp[i];
                    final int n = j;
                    array[n] -= bp[col2][j] * this.lu[i][col2];
                }
            }
        }
        for (int col2 = nCol - 1; col2 >= 0; --col2) {
            for (int k = 0; k < nColB; ++k) {
                final double[] array2 = bp[col2];
                final int n2 = k;
                array2[n2] /= this.lu[col2][col2];
            }
            for (int i = 0; i < col2; ++i) {
                for (int j = 0; j < nColB; ++j) {
                    final double[] array3 = bp[i];
                    final int n3 = j;
                    array3[n3] -= bp[col2][j] * this.lu[i][col2];
                }
            }
        }
        final RealMatrixImpl outMat = new RealMatrixImpl(bp);
        return outMat;
    }
    
    public void LUDecompose() throws InvalidMatrixException {
        final int nRows = this.getRowDimension();
        final int nCols = this.getColumnDimension();
        if (nRows < nCols) {
            throw new InvalidMatrixException("LU decomposition requires row dimension >= column dimension");
        }
        this.lu = this.getData();
        this.pivot = new int[nRows];
        for (int row = 0; row < nRows; ++row) {
            this.pivot[row] = row;
        }
        this.parity = 1;
        for (int col = 0; col < nCols; ++col) {
            double sum = 0.0;
            for (int row2 = 0; row2 < col; ++row2) {
                sum = this.lu[row2][col];
                for (int i = 0; i < row2; ++i) {
                    sum -= this.lu[row2][i] * this.lu[i][col];
                }
                this.lu[row2][col] = sum;
            }
            int max = col;
            double largest = 0.0;
            for (int row3 = col; row3 < nRows; ++row3) {
                sum = this.lu[row3][col];
                for (int j = 0; j < col; ++j) {
                    sum -= this.lu[row3][j] * this.lu[j][col];
                }
                this.lu[row3][col] = sum;
                if (Math.abs(sum) > largest) {
                    largest = Math.abs(sum);
                    max = row3;
                }
            }
            if (Math.abs(this.lu[max][col]) < RealMatrixImpl.TOO_SMALL) {
                this.lu = null;
                throw new InvalidMatrixException("matrix is singular");
            }
            if (max != col) {
                double tmp = 0.0;
                for (int k = 0; k < nCols; ++k) {
                    tmp = this.lu[max][k];
                    this.lu[max][k] = this.lu[col][k];
                    this.lu[col][k] = tmp;
                }
                final int temp = this.pivot[max];
                this.pivot[max] = this.pivot[col];
                this.pivot[col] = temp;
                this.parity = -this.parity;
            }
            for (int row3 = col + 1; row3 < nRows; ++row3) {
                final double[] array = this.lu[row3];
                final int n = col;
                array[n] /= this.lu[col][col];
            }
        }
    }
    
    public String toString() {
        final StringBuffer res = new StringBuffer();
        res.append("RealMatrixImpl{");
        for (int i = 0; i < this.data.length; ++i) {
            if (i > 0) {
                res.append(",");
            }
            res.append("{");
            for (int j = 0; j < this.data[0].length; ++j) {
                if (j > 0) {
                    res.append(",");
                }
                res.append(this.data[i][j]);
            }
            res.append("}");
        }
        res.append("}");
        return res.toString();
    }
    
    protected RealMatrix getIdentity(final int dimension) {
        final RealMatrixImpl out = new RealMatrixImpl(dimension, dimension);
        final double[][] d = out.getDataRef();
        for (int row = 0; row < dimension; ++row) {
            for (int col = 0; col < dimension; ++col) {
                d[row][col] = ((row == col) ? 1.0 : 0.0);
            }
        }
        return out;
    }
    
    private double[][] copyOut() {
        final int nRows = this.getRowDimension();
        final double[][] out = new double[nRows][this.getColumnDimension()];
        for (int i = 0; i < nRows; ++i) {
            System.arraycopy(this.data[i], 0, out[i], 0, this.data[i].length);
        }
        return out;
    }
    
    private void copyIn(final double[][] in) {
        final int nRows = in.length;
        final int nCols = in[0].length;
        System.arraycopy(in, 0, this.data = new double[nRows][nCols], 0, in.length);
        for (int i = 0; i < nRows; ++i) {
            System.arraycopy(in[i], 0, this.data[i], 0, nCols);
        }
        this.lu = null;
    }
    
    private boolean isValidCoordinate(final int row, final int col) {
        final int nRows = this.getRowDimension();
        final int nCols = this.getColumnDimension();
        return row >= 1 && row <= nRows && col >= 1 && col <= nCols;
    }
    
    static {
        RealMatrixImpl.TOO_SMALL = 1.0E-11;
    }
}
