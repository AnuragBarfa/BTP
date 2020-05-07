// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.math.random;

import java.io.Reader;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.io.IOException;
import java.io.BufferedReader;
import java.net.URL;
import java.io.Serializable;

public class ValueServer implements Serializable
{
    private int mode;
    private URL valuesFileURL;
    private double mu;
    private double sigma;
    private EmpiricalDistribution empiricalDistribution;
    private BufferedReader filePointer;
    private RandomDataImpl randomData;
    public static final int DIGEST_MODE = 0;
    public static final int REPLAY_MODE = 1;
    public static final int UNIFORM_MODE = 2;
    public static final int EXPONENTIAL_MODE = 3;
    public static final int GAUSSIAN_MODE = 4;
    public static final int CONSTANT_MODE = 5;
    
    public ValueServer() {
        this.mode = 5;
        this.valuesFileURL = null;
        this.mu = 0.0;
        this.sigma = 0.0;
        this.empiricalDistribution = null;
        this.filePointer = null;
        this.randomData = new RandomDataImpl();
    }
    
    public double getNext() throws IOException {
        switch (this.mode) {
            case 0: {
                return this.getNextDigest();
            }
            case 1: {
                return this.getNextReplay();
            }
            case 2: {
                return this.getNextUniform();
            }
            case 3: {
                return this.getNextExponential();
            }
            case 4: {
                return this.getNextGaussian();
            }
            case 5: {
                return this.mu;
            }
            default: {
                throw new IllegalStateException("Bad mode: " + this.mode);
            }
        }
    }
    
    public void fill(final double[] values) throws IOException {
        for (int i = 0; i < values.length; ++i) {
            values[i] = this.getNext();
        }
    }
    
    public double[] fill(final int length) throws IOException {
        final double[] out = new double[length];
        for (int i = 0; i < length; ++i) {
            out[i] = this.getNext();
        }
        return out;
    }
    
    public void computeDistribution() throws IOException {
        (this.empiricalDistribution = new EmpiricalDistributionImpl()).load(this.valuesFileURL);
    }
    
    public void computeDistribution(final int binCount) throws IOException {
        (this.empiricalDistribution = new EmpiricalDistributionImpl(binCount)).load(this.valuesFileURL);
        this.mu = this.empiricalDistribution.getSampleStats().getMean();
        this.sigma = this.empiricalDistribution.getSampleStats().getStandardDeviation();
    }
    
    public int getMode() {
        return this.mode;
    }
    
    public void setMode(final int mode) {
        this.mode = mode;
    }
    
    public URL getValuesFileURL() {
        return this.valuesFileURL;
    }
    
    public void setValuesFileURL(final String url) throws MalformedURLException {
        this.valuesFileURL = new URL(url);
    }
    
    public void setValuesFileURL(final URL url) {
        this.valuesFileURL = url;
    }
    
    public EmpiricalDistribution getEmpiricalDistribution() {
        return this.empiricalDistribution;
    }
    
    public void openReplayFile() throws IOException {
        this.resetReplayFile();
    }
    
    public void resetReplayFile() throws IOException {
        if (this.filePointer != null) {
            try {
                this.filePointer.close();
                this.filePointer = null;
            }
            catch (IOException ex) {}
        }
        this.filePointer = new BufferedReader(new InputStreamReader(this.valuesFileURL.openStream()));
    }
    
    public void closeReplayFile() throws IOException {
        if (this.filePointer != null) {
            this.filePointer.close();
            this.filePointer = null;
        }
    }
    
    public double getMu() {
        return this.mu;
    }
    
    public void setMu(final double mu) {
        this.mu = mu;
    }
    
    public double getSigma() {
        return this.sigma;
    }
    
    public void setSigma(final double sigma) {
        this.sigma = sigma;
    }
    
    private double getNextDigest() {
        if (this.empiricalDistribution == null || this.empiricalDistribution.getBinStats().size() == 0) {
            throw new IllegalStateException("Digest not initialized");
        }
        return this.empiricalDistribution.getNextValue();
    }
    
    private double getNextReplay() throws IOException {
        String str = null;
        if (this.filePointer == null) {
            this.resetReplayFile();
        }
        if ((str = this.filePointer.readLine()) == null) {
            this.closeReplayFile();
            this.resetReplayFile();
            str = this.filePointer.readLine();
        }
        return new Double(str);
    }
    
    private double getNextUniform() {
        return this.randomData.nextUniform(0.0, 2.0 * this.mu);
    }
    
    private double getNextExponential() {
        return this.randomData.nextExponential(this.mu);
    }
    
    private double getNextGaussian() {
        return this.randomData.nextGaussian(this.mu, this.sigma);
    }
}
