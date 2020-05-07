// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.math.random;

import java.util.Collection;
import java.security.NoSuchProviderException;
import java.security.NoSuchAlgorithmException;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Random;
import java.io.Serializable;

public class RandomDataImpl implements RandomData, Serializable
{
    private Random rand;
    private SecureRandom secRand;
    
    public RandomDataImpl() {
        this.rand = null;
        this.secRand = null;
    }
    
    public String nextHexString(final int len) {
        if (len <= 0) {
            throw new IllegalArgumentException("length must be positive");
        }
        final Random ran = this.getRan();
        final StringBuffer outBuffer = new StringBuffer();
        final byte[] randomBytes = new byte[len / 2 + 1];
        ran.nextBytes(randomBytes);
        for (int i = 0; i < randomBytes.length; ++i) {
            final Integer c = new Integer(randomBytes[i]);
            String hex = Integer.toHexString(c + 128);
            if (hex.length() == 1) {
                hex = "0" + hex;
            }
            outBuffer.append(hex);
        }
        return outBuffer.toString().substring(0, len);
    }
    
    public int nextInt(final int lower, final int upper) {
        if (lower >= upper) {
            throw new IllegalArgumentException("upper bound must be > lower bound");
        }
        final Random rand = this.getRan();
        return lower + (int)(rand.nextDouble() * (upper - lower + 1));
    }
    
    public long nextLong(final long lower, final long upper) {
        if (lower >= upper) {
            throw new IllegalArgumentException("upper bound must be > lower bound");
        }
        final Random rand = this.getRan();
        return lower + (long)(rand.nextDouble() * (upper - lower + 1L));
    }
    
    public String nextSecureHexString(final int len) {
        if (len <= 0) {
            throw new IllegalArgumentException("length must be positive");
        }
        final SecureRandom secRan = this.getSecRan();
        MessageDigest alg = null;
        try {
            alg = MessageDigest.getInstance("SHA-1");
        }
        catch (NoSuchAlgorithmException ex) {
            return null;
        }
        alg.reset();
        final int numIter = len / 40 + 1;
        final StringBuffer outBuffer = new StringBuffer();
        for (int iter = 1; iter < numIter + 1; ++iter) {
            final byte[] randomBytes = new byte[40];
            secRan.nextBytes(randomBytes);
            alg.update(randomBytes);
            final byte[] hash = alg.digest();
            for (int i = 0; i < hash.length; ++i) {
                final Integer c = new Integer(hash[i]);
                String hex = Integer.toHexString(c + 128);
                if (hex.length() == 1) {
                    hex = "0" + hex;
                }
                outBuffer.append(hex);
            }
        }
        return outBuffer.toString().substring(0, len);
    }
    
    public int nextSecureInt(final int lower, final int upper) {
        if (lower >= upper) {
            throw new IllegalArgumentException("lower bound must be < upper bound");
        }
        final SecureRandom sec = this.getSecRan();
        return lower + (int)(sec.nextDouble() * (upper - lower + 1));
    }
    
    public long nextSecureLong(final long lower, final long upper) {
        if (lower >= upper) {
            throw new IllegalArgumentException("lower bound must be < upper bound");
        }
        final SecureRandom sec = this.getSecRan();
        return lower + (long)(sec.nextDouble() * (upper - lower + 1L));
    }
    
    public long nextPoisson(final double mean) {
        if (mean <= 0.0) {
            throw new IllegalArgumentException("Poisson mean must be > 0");
        }
        final double p = Math.exp(-mean);
        long n = 0L;
        double r = 1.0;
        double rnd = 1.0;
        final Random rand = this.getRan();
        while (n < 1000.0 * mean) {
            rnd = rand.nextDouble();
            r *= rnd;
            if (r < p) {
                return n;
            }
            ++n;
        }
        return n;
    }
    
    public double nextGaussian(final double mu, final double sigma) {
        if (sigma <= 0.0) {
            throw new IllegalArgumentException("Gaussian std dev must be > 0");
        }
        final Random rand = this.getRan();
        return sigma * rand.nextGaussian() + mu;
    }
    
    public double nextExponential(final double mean) {
        if (mean < 0.0) {
            throw new IllegalArgumentException("Exponential mean must be >= 0");
        }
        Random rand;
        double unif;
        for (rand = this.getRan(), unif = rand.nextDouble(); unif == 0.0; unif = rand.nextDouble()) {}
        return -mean * Math.log(unif);
    }
    
    public double nextUniform(final double lower, final double upper) {
        if (lower >= upper) {
            throw new IllegalArgumentException("lower bound must be <= upper bound");
        }
        Random rand;
        double u;
        for (rand = this.getRan(), u = rand.nextDouble(); u <= 0.0; u = rand.nextDouble()) {}
        return lower + u * (upper - lower);
    }
    
    private Random getRan() {
        if (this.rand == null) {
            (this.rand = new Random()).setSeed(System.currentTimeMillis());
        }
        return this.rand;
    }
    
    private SecureRandom getSecRan() {
        if (this.secRand == null) {
            (this.secRand = new SecureRandom()).setSeed(System.currentTimeMillis());
        }
        return this.secRand;
    }
    
    public void reSeed(final long seed) {
        if (this.rand == null) {
            this.rand = new Random();
        }
        this.rand.setSeed(seed);
    }
    
    public void reSeedSecure() {
        if (this.secRand == null) {
            this.secRand = new SecureRandom();
        }
        this.secRand.setSeed(System.currentTimeMillis());
    }
    
    public void reSeedSecure(final long seed) {
        if (this.secRand == null) {
            this.secRand = new SecureRandom();
        }
        this.secRand.setSeed(seed);
    }
    
    public void reSeed() {
        if (this.rand == null) {
            this.rand = new Random();
        }
        this.rand.setSeed(System.currentTimeMillis());
    }
    
    public void setSecureAlgorithm(final String algorithm, final String provider) throws NoSuchAlgorithmException, NoSuchProviderException {
        this.secRand = SecureRandom.getInstance(algorithm, provider);
    }
    
    public int[] nextPermutation(final int n, final int k) {
        if (k > n) {
            throw new IllegalArgumentException("permutation k exceeds n");
        }
        if (k == 0) {
            throw new IllegalArgumentException("permutation k must be > 0");
        }
        final int[] index = this.getNatural(n);
        this.shuffle(index, n - k);
        final int[] result = new int[k];
        for (int i = 0; i < k; ++i) {
            result[i] = index[n - i - 1];
        }
        return result;
    }
    
    public Object[] nextSample(final Collection c, final int k) {
        final int len = c.size();
        if (k > len) {
            throw new IllegalArgumentException("sample size exceeds collection size");
        }
        if (k == 0) {
            throw new IllegalArgumentException("sample size must be > 0");
        }
        final Object[] objects = c.toArray();
        final int[] index = this.nextPermutation(len, k);
        final Object[] result = new Object[k];
        for (int i = 0; i < k; ++i) {
            result[i] = objects[index[i]];
        }
        return result;
    }
    
    private void shuffle(final int[] list, final int end) {
        int target = 0;
        for (int i = list.length - 1; i >= end; --i) {
            if (i == 0) {
                target = 0;
            }
            else {
                target = this.nextInt(0, i);
            }
            final int temp = list[target];
            list[target] = list[i];
            list[i] = temp;
        }
    }
    
    private int[] getNatural(final int n) {
        final int[] natural = new int[n];
        for (int i = 0; i < n; ++i) {
            natural[i] = i;
        }
        return natural;
    }
}
