// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.math;

import java.io.Serializable;

public class MathConfigurationException extends MathException implements Serializable
{
    public MathConfigurationException() {
        this(null, null);
    }
    
    public MathConfigurationException(final String message) {
        this(message, null);
    }
    
    public MathConfigurationException(final String message, final Throwable throwable) {
        super(message, throwable);
    }
    
    public MathConfigurationException(final Throwable throwable) {
        this(null, throwable);
    }
}
