// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.math;

import java.io.Serializable;

public class ConvergenceException extends MathException implements Serializable
{
    public ConvergenceException() {
        this(null, null);
    }
    
    public ConvergenceException(final String message) {
        this(message, null);
    }
    
    public ConvergenceException(final String message, final Throwable cause) {
        super(message, cause);
    }
    
    public ConvergenceException(final Throwable throwable) {
        this(null, throwable);
    }
}
