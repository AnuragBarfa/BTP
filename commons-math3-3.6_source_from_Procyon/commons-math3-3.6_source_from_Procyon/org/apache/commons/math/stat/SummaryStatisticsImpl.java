// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.math.stat;

import org.apache.commons.math.stat.univariate.moment.FirstMoment;
import org.apache.commons.math.stat.univariate.moment.Variance;
import org.apache.commons.math.stat.univariate.moment.Mean;
import org.apache.commons.math.stat.univariate.moment.GeometricMean;
import org.apache.commons.math.stat.univariate.summary.SumOfLogs;
import org.apache.commons.math.stat.univariate.rank.Max;
import org.apache.commons.math.stat.univariate.rank.Min;
import org.apache.commons.math.stat.univariate.summary.SumOfSquares;
import org.apache.commons.math.stat.univariate.summary.Sum;
import org.apache.commons.math.stat.univariate.moment.SecondMoment;

public class SummaryStatisticsImpl extends SummaryStatistics
{
    protected long n;
    protected SecondMoment secondMoment;
    protected Sum sum;
    protected SumOfSquares sumsq;
    protected Min min;
    protected Max max;
    protected SumOfLogs sumLog;
    protected GeometricMean geoMean;
    protected Mean mean;
    protected Variance variance;
    
    public SummaryStatisticsImpl() {
        this.n = 0L;
        this.secondMoment = null;
        this.sum = null;
        this.sumsq = null;
        this.min = null;
        this.max = null;
        this.sumLog = null;
        this.geoMean = null;
        this.mean = null;
        this.variance = null;
        this.sum = new Sum();
        this.sumsq = new SumOfSquares();
        this.min = new Min();
        this.max = new Max();
        this.sumLog = new SumOfLogs();
        this.geoMean = new GeometricMean();
        this.secondMoment = new SecondMoment();
    }
    
    public void addValue(final double value) {
        this.sum.increment(value);
        this.sumsq.increment(value);
        this.min.increment(value);
        this.max.increment(value);
        this.sumLog.increment(value);
        this.geoMean.increment(value);
        this.secondMoment.increment(value);
        ++this.n;
    }
    
    public long getN() {
        return this.n;
    }
    
    public double getSum() {
        return this.sum.getResult();
    }
    
    public double getSumsq() {
        return this.sumsq.getResult();
    }
    
    public double getMean() {
        return new Mean(this.secondMoment).getResult();
    }
    
    public double getStandardDeviation() {
        double stdDev = Double.NaN;
        if (this.getN() > 0L) {
            if (this.getN() > 1L) {
                stdDev = Math.sqrt(this.getVariance());
            }
            else {
                stdDev = 0.0;
            }
        }
        return stdDev;
    }
    
    public double getVariance() {
        return new Variance(this.secondMoment).getResult();
    }
    
    public double getMax() {
        return this.max.getResult();
    }
    
    public double getMin() {
        return this.min.getResult();
    }
    
    public double getGeometricMean() {
        return this.geoMean.getResult();
    }
    
    public String toString() {
        final StringBuffer outBuffer = new StringBuffer();
        outBuffer.append("SummaryStatistics:\n");
        outBuffer.append("n: " + this.n + "\n");
        outBuffer.append("min: " + this.min + "\n");
        outBuffer.append("max: " + this.max + "\n");
        outBuffer.append("mean: " + this.getMean() + "\n");
        outBuffer.append("std dev: " + this.getStandardDeviation() + "\n");
        return outBuffer.toString();
    }
    
    public void clear() {
        this.n = 0L;
        this.min.clear();
        this.max.clear();
        this.sum.clear();
        this.sumLog.clear();
        this.sumsq.clear();
        this.geoMean.clear();
        this.secondMoment.clear();
    }
}
