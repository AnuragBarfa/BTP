// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.math.util;

import org.apache.commons.math.MathException;
import java.io.Serializable;

public class DefaultTransformer implements NumberTransformer, Serializable
{
    public double transform(final Object o) throws MathException {
        if (o == null) {
            throw new MathException("Conversion Exception in Transformation, Object is null");
        }
        if (o instanceof Number) {
            return ((Number)o).doubleValue();
        }
        try {
            return new Double(o.toString());
        }
        catch (Exception e) {
            throw new MathException("Conversion Exception in Transformation: " + e.getMessage(), e);
        }
    }
}
