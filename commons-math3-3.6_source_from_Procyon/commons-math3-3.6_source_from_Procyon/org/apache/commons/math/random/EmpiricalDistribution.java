// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.math.random;

import java.util.ArrayList;
import org.apache.commons.math.stat.SummaryStatistics;
import java.net.URL;
import java.io.File;
import java.io.IOException;

public interface EmpiricalDistribution
{
    void load(final double[] p0);
    
    void load(final String p0) throws IOException;
    
    void load(final File p0) throws IOException;
    
    void load(final URL p0) throws IOException;
    
    double getNextValue() throws IllegalStateException;
    
    SummaryStatistics getSampleStats() throws IllegalStateException;
    
    void loadDistribution(final File p0) throws IOException;
    
    void loadDistribution(final String p0) throws IOException;
    
    void saveDistribution(final String p0) throws IOException, IllegalStateException;
    
    void saveDistribution(final File p0) throws IOException, IllegalStateException;
    
    boolean isLoaded();
    
    int getBinCount();
    
    ArrayList getBinStats();
    
    double[] getUpperBounds();
}
