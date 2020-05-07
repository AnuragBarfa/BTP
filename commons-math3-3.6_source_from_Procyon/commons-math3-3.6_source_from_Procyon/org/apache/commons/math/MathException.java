// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.math;

import java.io.Serializable;
import org.apache.commons.lang.exception.NestableException;

public class MathException extends NestableException implements Serializable
{
    public MathException() {
        this(null, null);
    }
    
    public MathException(final String message) {
        this(message, null);
    }
    
    public MathException(final String message, final Throwable throwable) {
        super(message, throwable);
    }
    
    public MathException(final Throwable throwable) {
        this(null, throwable);
    }
}
