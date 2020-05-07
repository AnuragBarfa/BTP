// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.math.random;

import java.io.FileReader;
import java.io.File;
import java.net.URL;
import java.io.IOException;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.FileInputStream;
import org.apache.commons.math.stat.SummaryStatistics;
import java.util.ArrayList;
import java.io.Serializable;

public class EmpiricalDistributionImpl implements Serializable, EmpiricalDistribution
{
    private ArrayList binStats;
    SummaryStatistics sampleStats;
    private int binCount;
    private boolean loaded;
    private double[] upperBounds;
    private RandomData randomData;
    
    public EmpiricalDistributionImpl() {
        this.binStats = null;
        this.sampleStats = null;
        this.binCount = 1000;
        this.loaded = false;
        this.upperBounds = null;
        this.randomData = new RandomDataImpl();
        this.binStats = new ArrayList();
    }
    
    public EmpiricalDistributionImpl(final int binCount) {
        this.binStats = null;
        this.sampleStats = null;
        this.binCount = 1000;
        this.loaded = false;
        this.upperBounds = null;
        this.randomData = new RandomDataImpl();
        this.binCount = binCount;
        this.binStats = new ArrayList();
    }
    
    public void load(final double[] in) {
        final DataAdapter da = new ArrayDataAdapter(in);
        try {
            da.computeStats();
            this.fillBinStats(in);
        }
        catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
        this.loaded = true;
    }
    
    public void load(final String filePath) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(filePath)));
        try {
            final DataAdapter da = new StreamDataAdapter(in);
            try {
                da.computeStats();
            }
            catch (Exception e) {
                throw new IOException(e.getMessage());
            }
            in = new BufferedReader(new InputStreamReader(new FileInputStream(filePath)));
            this.fillBinStats(in);
            this.loaded = true;
        }
        finally {
            if (in != null) {
                try {
                    in.close();
                }
                catch (Exception ex) {}
            }
        }
    }
    
    public void load(final URL url) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
        try {
            final DataAdapter da = new StreamDataAdapter(in);
            try {
                da.computeStats();
            }
            catch (Exception e) {
                throw new IOException(e.getMessage());
            }
            in = new BufferedReader(new InputStreamReader(url.openStream()));
            this.fillBinStats(in);
            this.loaded = true;
        }
        finally {
            if (in != null) {
                try {
                    in.close();
                }
                catch (Exception ex) {}
            }
        }
    }
    
    public void load(final File file) throws IOException {
        BufferedReader in = new BufferedReader(new FileReader(file));
        try {
            final DataAdapter da = new StreamDataAdapter(in);
            try {
                da.computeStats();
            }
            catch (Exception e) {
                throw new IOException(e.getMessage());
            }
            in = new BufferedReader(new FileReader(file));
            this.fillBinStats(in);
            this.loaded = true;
        }
        finally {
            if (in != null) {
                try {
                    in.close();
                }
                catch (Exception ex) {}
            }
        }
    }
    
    private void fillBinStats(final Object in) throws IOException {
        final double min = this.sampleStats.getMin();
        final double max = this.sampleStats.getMax();
        final double delta = (max - min) / new Double(this.binCount);
        final double[] binUpperBounds = new double[this.binCount];
        binUpperBounds[0] = min + delta;
        for (int i = 1; i < this.binCount - 1; ++i) {
            binUpperBounds[i] = binUpperBounds[i - 1] + delta;
        }
        binUpperBounds[this.binCount - 1] = max;
        if (!this.binStats.isEmpty()) {
            this.binStats.clear();
        }
        for (int i = 0; i < this.binCount; ++i) {
            final SummaryStatistics stats = SummaryStatistics.newInstance();
            this.binStats.add(i, stats);
        }
        final DataAdapterFactory aFactory = new DataAdapterFactory();
        final DataAdapter da = aFactory.getAdapter(in);
        try {
            da.computeBinStats(min, delta);
        }
        catch (Exception e) {
            if (e instanceof RuntimeException) {
                throw new RuntimeException(e.getMessage());
            }
            throw new IOException(e.getMessage());
        }
        (this.upperBounds = new double[this.binCount])[0] = this.binStats.get(0).getN() / (double)this.sampleStats.getN();
        for (int j = 1; j < this.binCount - 1; ++j) {
            this.upperBounds[j] = this.upperBounds[j - 1] + this.binStats.get(j).getN() / (double)this.sampleStats.getN();
        }
        this.upperBounds[this.binCount - 1] = 1.0;
    }
    
    public double getNextValue() throws IllegalStateException {
        if (!this.loaded) {
            throw new IllegalStateException("distribution not loaded");
        }
        final double x = Math.random();
        for (int i = 0; i < this.binCount; ++i) {
            if (x <= this.upperBounds[i]) {
                final SummaryStatistics stats = this.binStats.get(i);
                if (stats.getN() > 0L) {
                    if (stats.getStandardDeviation() > 0.0) {
                        return this.randomData.nextGaussian(stats.getMean(), stats.getStandardDeviation());
                    }
                    return stats.getMean();
                }
            }
        }
        throw new RuntimeException("No bin selected");
    }
    
    public void loadDistribution(final String filePath) throws IOException {
        throw new UnsupportedOperationException("Not Implemented yet :-(");
    }
    
    public void loadDistribution(final File file) throws IOException {
        throw new UnsupportedOperationException("Not Implemented yet :-(");
    }
    
    public void saveDistribution(final String filePath) throws IOException, IllegalStateException {
        throw new UnsupportedOperationException("Not Implemented yet :-(");
    }
    
    public void saveDistribution(final File file) throws IOException, IllegalStateException {
        throw new UnsupportedOperationException("Not Implemented yet :-(");
    }
    
    public SummaryStatistics getSampleStats() {
        return this.sampleStats;
    }
    
    public int getBinCount() {
        return this.binCount;
    }
    
    public ArrayList getBinStats() {
        return this.binStats;
    }
    
    public double[] getUpperBounds() {
        return this.upperBounds;
    }
    
    public boolean isLoaded() {
        return this.loaded;
    }
    
    private abstract class DataAdapter
    {
        public abstract void computeBinStats(final double p0, final double p1) throws Exception;
        
        public abstract void computeStats() throws Exception;
    }
    
    private class DataAdapterFactory
    {
        public DataAdapter getAdapter(final Object in) {
            if (in instanceof BufferedReader) {
                final BufferedReader inputStream = (BufferedReader)in;
                return new StreamDataAdapter(inputStream);
            }
            if (in instanceof double[]) {
                final double[] inputArray = (double[])in;
                return new ArrayDataAdapter(inputArray);
            }
            throw new IllegalArgumentException("Input data comes from the unsupported source");
        }
    }
    
    private class StreamDataAdapter extends DataAdapter
    {
        BufferedReader inputStream;
        
        public StreamDataAdapter(final BufferedReader in) {
            this.inputStream = in;
        }
        
        public void computeBinStats(final double min, final double delta) throws IOException {
            String str = null;
            double val = 0.0;
            while ((str = this.inputStream.readLine()) != null) {
                val = Double.parseDouble(str);
                final SummaryStatistics stats = EmpiricalDistributionImpl.this.binStats.get(Math.max((int)Math.ceil((val - min) / delta) - 1, 0));
                stats.addValue(val);
            }
            this.inputStream.close();
            this.inputStream = null;
        }
        
        public void computeStats() throws IOException {
            String str = null;
            double val = 0.0;
            EmpiricalDistributionImpl.this.sampleStats = SummaryStatistics.newInstance();
            while ((str = this.inputStream.readLine()) != null) {
                val = new Double(str);
                EmpiricalDistributionImpl.this.sampleStats.addValue(val);
            }
            this.inputStream.close();
            this.inputStream = null;
        }
    }
    
    private class ArrayDataAdapter extends DataAdapter
    {
        private double[] inputArray;
        
        public ArrayDataAdapter(final double[] in) {
            this.inputArray = in;
        }
        
        public void computeStats() throws IOException {
            EmpiricalDistributionImpl.this.sampleStats = SummaryStatistics.newInstance();
            for (int i = 0; i < this.inputArray.length; ++i) {
                EmpiricalDistributionImpl.this.sampleStats.addValue(this.inputArray[i]);
            }
        }
        
        public void computeBinStats(final double min, final double delta) throws IOException {
            for (int i = 0; i < this.inputArray.length; ++i) {
                final SummaryStatistics stats = EmpiricalDistributionImpl.this.binStats.get(Math.max((int)Math.ceil((this.inputArray[i] - min) / delta) - 1, 0));
                stats.addValue(this.inputArray[i]);
            }
        }
    }
}
