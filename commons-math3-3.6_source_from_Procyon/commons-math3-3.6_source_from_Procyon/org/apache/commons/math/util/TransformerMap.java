// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.math.util;

import org.apache.commons.math.MathException;
import java.util.Collection;
import java.util.Set;
import java.util.HashMap;
import java.util.Map;
import java.io.Serializable;

public class TransformerMap implements NumberTransformer, Serializable
{
    private NumberTransformer defaultTransformer;
    private Map map;
    
    public TransformerMap() {
        this.defaultTransformer = null;
        this.map = null;
        this.map = new HashMap();
        this.defaultTransformer = new DefaultTransformer();
    }
    
    public boolean containsClass(final Class key) {
        return this.map.containsKey(key);
    }
    
    public boolean containsTransformer(final NumberTransformer value) {
        return this.map.containsValue(value);
    }
    
    public NumberTransformer getTransformer(final Class key) {
        return this.map.get(key);
    }
    
    public Object putTransformer(final Class key, final NumberTransformer transformer) {
        return this.map.put(key, transformer);
    }
    
    public Object removeTransformer(final Class key) {
        return this.map.remove(key);
    }
    
    public void clear() {
        this.map.clear();
    }
    
    public Set classes() {
        return this.map.keySet();
    }
    
    public Collection transformers() {
        return this.map.values();
    }
    
    public double transform(final Object o) throws MathException {
        double value = Double.NaN;
        if (o instanceof Number || o instanceof String) {
            value = this.defaultTransformer.transform(o);
        }
        else {
            final NumberTransformer trans = this.getTransformer(o.getClass());
            if (trans != null) {
                value = trans.transform(o);
            }
        }
        return value;
    }
}
