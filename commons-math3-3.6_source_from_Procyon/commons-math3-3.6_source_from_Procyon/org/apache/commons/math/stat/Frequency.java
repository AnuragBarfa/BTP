// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.math.stat;

import java.util.Collection;
import org.apache.commons.collections.Bag;
import java.util.Iterator;
import java.text.NumberFormat;
import java.util.Comparator;
import org.apache.commons.collections.TreeBag;
import org.apache.commons.collections.SortedBag;
import java.io.Serializable;

public class Frequency implements Serializable
{
    private SortedBag freqTable;
    
    public Frequency() {
        this.freqTable = null;
        this.freqTable = (SortedBag)new TreeBag();
    }
    
    public Frequency(final Comparator comparator) {
        this.freqTable = null;
        this.freqTable = (SortedBag)new TreeBag(comparator);
    }
    
    public String toString() {
        final NumberFormat nf = NumberFormat.getPercentInstance();
        final StringBuffer outBuffer = new StringBuffer();
        outBuffer.append("Value \t Freq. \t Pct. \t Cum Pct. \n");
        for (final Object value : ((Bag)this.freqTable).uniqueSet()) {
            outBuffer.append(value);
            outBuffer.append('\t');
            outBuffer.append(this.getCount(value));
            outBuffer.append('\t');
            outBuffer.append(nf.format(this.getPct(value)));
            outBuffer.append('\t');
            outBuffer.append(nf.format(this.getCumPct(value)));
            outBuffer.append('\n');
        }
        return outBuffer.toString();
    }
    
    public void addValue(final Object v) {
        try {
            ((Bag)this.freqTable).add(v);
        }
        catch (ClassCastException ex) {
            throw new IllegalArgumentException("Value not comparable to existing values.");
        }
    }
    
    public void addValue(final int v) {
        this.addValue(new Long(v));
    }
    
    public void addValue(final long v) {
        this.addValue(new Long(v));
    }
    
    public void addValue(final char v) {
        this.addValue(new Character(v));
    }
    
    public void clear() {
        ((Collection)this.freqTable).clear();
    }
    
    public Iterator valuesIterator() {
        return ((Bag)this.freqTable).uniqueSet().iterator();
    }
    
    public long getSumFreq() {
        return ((Bag)this.freqTable).size();
    }
    
    public long getCount(final Object v) {
        long result = 0L;
        try {
            result = ((Bag)this.freqTable).getCount(v);
        }
        catch (Exception ex) {}
        return result;
    }
    
    public long getCount(final int v) {
        long result = 0L;
        try {
            result = ((Bag)this.freqTable).getCount((Object)new Long(v));
        }
        catch (Exception ex) {}
        return result;
    }
    
    public long getCount(final long v) {
        long result = 0L;
        try {
            result = ((Bag)this.freqTable).getCount((Object)new Long(v));
        }
        catch (Exception ex) {}
        return result;
    }
    
    public long getCount(final char v) {
        long result = 0L;
        try {
            result = ((Bag)this.freqTable).getCount((Object)new Character(v));
        }
        catch (Exception ex) {}
        return result;
    }
    
    public double getPct(final Object v) {
        return this.getCount(v) / (double)this.getSumFreq();
    }
    
    public double getPct(final int v) {
        return this.getPct(new Long(v));
    }
    
    public double getPct(final long v) {
        return this.getPct(new Long(v));
    }
    
    public double getPct(final char v) {
        return this.getPct(new Character(v));
    }
    
    public long getCumFreq(final Object v) {
        long result = 0L;
        try {
            result = ((Bag)this.freqTable).getCount(v);
        }
        catch (ClassCastException ex) {
            return result;
        }
        final Comparable c = (Comparable)v;
        if (c.compareTo(this.freqTable.first()) < 0) {
            return 0L;
        }
        if (c.compareTo(this.freqTable.last()) > 0) {
            return this.getSumFreq();
        }
        final Iterator values = this.valuesIterator();
        while (values.hasNext()) {
            final Object nextValue = values.next();
            if (c.compareTo(nextValue) <= 0) {
                return result;
            }
            result += this.getCount(nextValue);
        }
        return result;
    }
    
    public long getCumFreq(final int v) {
        return this.getCumFreq(new Long(v));
    }
    
    public long getCumFreq(final long v) {
        return this.getCumFreq(new Long(v));
    }
    
    public long getCumFreq(final char v) {
        return this.getCumFreq(new Character(v));
    }
    
    public double getCumPct(final Object v) {
        return this.getCumFreq(v) / (double)this.getSumFreq();
    }
    
    public double getCumPct(final int v) {
        return this.getCumPct(new Long(v));
    }
    
    public double getCumPct(final long v) {
        return this.getCumPct(new Long(v));
    }
    
    public double getCumPct(final char v) {
        return this.getCumPct(new Character(v));
    }
}
