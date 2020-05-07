// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.math.stat;

import org.apache.commons.math.distribution.DistributionFactory;
import org.apache.commons.math.distribution.TDistribution;
import org.apache.commons.math.MathException;
import java.io.Serializable;

public class BivariateRegression implements Serializable
{
    static final long serialVersionUID = -3004689053607543335L;
    private double sumX;
    private double sumXX;
    private double sumY;
    private double sumYY;
    private double sumXY;
    private long n;
    private double xbar;
    private double ybar;
    
    public BivariateRegression() {
        this.sumX = 0.0;
        this.sumXX = 0.0;
        this.sumY = 0.0;
        this.sumYY = 0.0;
        this.sumXY = 0.0;
        this.n = 0L;
        this.xbar = 0.0;
        this.ybar = 0.0;
    }
    
    public void addData(final double x, final double y) {
        if (this.n == 0L) {
            this.xbar = x;
            this.ybar = y;
        }
        else {
            final double dx = x - this.xbar;
            final double dy = y - this.ybar;
            this.sumXX += dx * dx * this.n / (this.n + 1.0);
            this.sumYY += dy * dy * this.n / (this.n + 1.0);
            this.sumXY += dx * dy * this.n / (this.n + 1.0);
            this.xbar += dx / (this.n + 1.0);
            this.ybar += dy / (this.n + 1.0);
        }
        this.sumX += x;
        this.sumY += y;
        ++this.n;
    }
    
    public void addData(final double[][] data) {
        for (int i = 0; i < data.length; ++i) {
            this.addData(data[i][0], data[i][1]);
        }
    }
    
    public void clear() {
        this.sumX = 0.0;
        this.sumXX = 0.0;
        this.sumY = 0.0;
        this.sumYY = 0.0;
        this.sumXY = 0.0;
        this.n = 0L;
    }
    
    public long getN() {
        return this.n;
    }
    
    public double predict(final double x) {
        final double b1 = this.getSlope();
        return this.getIntercept(b1) + b1 * x;
    }
    
    public double getIntercept() {
        return this.getIntercept(this.getSlope());
    }
    
    public double getSlope() {
        if (this.n < 2L) {
            return Double.NaN;
        }
        if (Math.abs(this.sumXX) < 4.9E-323) {
            return Double.NaN;
        }
        return this.sumXY / this.sumXX;
    }
    
    public double getSumSquaredErrors() {
        return this.getSumSquaredErrors(this.getSlope());
    }
    
    public double getTotalSumSquares() {
        if (this.n < 2L) {
            return Double.NaN;
        }
        return this.sumYY;
    }
    
    public double getRegressionSumSquares() {
        return this.getRegressionSumSquares(this.getSlope());
    }
    
    public double getMeanSquareError() {
        if (this.n < 3L) {
            return Double.NaN;
        }
        return this.getSumSquaredErrors() / (this.n - 2L);
    }
    
    public double getR() {
        final double b1 = this.getSlope();
        double result = Math.sqrt(this.getRSquare(b1));
        if (b1 < 0.0) {
            result = -result;
        }
        return result;
    }
    
    public double getRSquare() {
        return this.getRSquare(this.getSlope());
    }
    
    public double getInterceptStdErr() {
        return Math.sqrt(this.getMeanSquareError() * (1.0 / this.n + this.xbar * this.xbar / this.sumXX));
    }
    
    public double getSlopeStdErr() {
        return Math.sqrt(this.getMeanSquareError() / this.sumXX);
    }
    
    public double getSlopeConfidenceInterval() throws MathException {
        return this.getSlopeConfidenceInterval(0.05);
    }
    
    public double getSlopeConfidenceInterval(final double alpha) throws MathException {
        if (alpha >= 1.0 || alpha <= 0.0) {
            throw new IllegalArgumentException();
        }
        return this.getSlopeStdErr() * this.getTDistribution().inverseCumulativeProbability(1.0 - alpha / 2.0);
    }
    
    public double getSignificance() throws MathException {
        return 1.0 - this.getTDistribution().cumulativeProbability(Math.abs(this.getSlope()) / this.getSlopeStdErr());
    }
    
    private double getIntercept(final double slope) {
        return (this.sumY - slope * this.sumX) / this.n;
    }
    
    private double getSumSquaredErrors(final double b1) {
        return this.sumYY - this.sumXY * this.sumXY / this.sumXX;
    }
    
    private double getRSquare(final double b1) {
        final double ssto = this.getTotalSumSquares();
        return (ssto - this.getSumSquaredErrors(b1)) / ssto;
    }
    
    private double getRegressionSumSquares(final double slope) {
        return slope * slope * this.sumXX;
    }
    
    private TDistribution getTDistribution() {
        return DistributionFactory.newInstance().createTDistribution((double)(this.n - 2L));
    }
}
