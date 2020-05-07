// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.math.analysis;

import java.io.Serializable;

public class SplineInterpolator implements UnivariateRealInterpolator, Serializable
{
    private double[][] c;
    
    public SplineInterpolator() {
        this.c = null;
    }
    
    public UnivariateRealFunction interpolate(final double[] xval, final double[] yval) {
        if (xval.length != yval.length) {
            throw new IllegalArgumentException("Dataset arrays must have same length.");
        }
        if (this.c == null) {
            final int n = xval.length - 1;
            for (int i = 0; i < n; ++i) {
                if (xval[i] >= xval[i + 1]) {
                    throw new IllegalArgumentException("Dataset must specify sorted, ascending x values.");
                }
            }
            final double[] b = new double[n - 1];
            final double[] d = new double[n - 1];
            double dquot = (yval[1] - yval[0]) / (xval[1] - xval[0]);
            for (int j = 0; j < n - 1; ++j) {
                final double dquotNext = (yval[j + 2] - yval[j + 1]) / (xval[j + 2] - xval[j + 1]);
                b[j] = 6.0 * (dquotNext - dquot);
                d[j] = 2.0 * (xval[j + 2] - xval[j]);
                dquot = dquotNext;
            }
            for (int j = 0; j < n - 2; ++j) {
                final double delta = xval[j + 2] - xval[j + 1];
                final double deltaquot = delta / d[j];
                final double[] array = d;
                final int n2 = j + 1;
                array[n2] -= delta * deltaquot;
                final double[] array2 = b;
                final int n3 = j + 1;
                array2[n3] -= b[j] * deltaquot;
            }
            d[n - 2] = b[n - 2] / d[n - 2];
            for (int j = n - 3; j >= 0; --j) {
                d[j] = (b[j] - (xval[j + 2] - xval[j + 1]) * d[j + 1]) / d[j];
            }
            this.c = new double[n][4];
            double delta2 = xval[1] - xval[0];
            this.c[0][3] = d[0] / delta2 / 6.0;
            this.c[0][2] = 0.0;
            this.c[0][1] = (yval[1] - yval[0]) / delta2 - d[0] * delta2 / 6.0;
            for (int k = 1; k < n - 2; ++k) {
                delta2 = xval[k + 1] - xval[k];
                this.c[k][3] = (d[k] - d[k - 1]) / delta2 / 6.0;
                this.c[k][2] = d[k - 1] / 2.0;
                this.c[k][1] = (yval[k + 1] - yval[k]) / delta2 - (d[k] / 2.0 - d[k - 1]) * delta2 / 3.0;
            }
            delta2 = xval[n] - xval[n - 1];
            this.c[n - 1][3] = -d[n - 2] / delta2 / 6.0;
            this.c[n - 1][2] = d[n - 2] / 2.0;
            this.c[n - 1][1] = (yval[n] - yval[n - 1]) / delta2 - d[n - 2] * delta2 / 3.0;
            for (int k = 0; k < n; ++k) {
                this.c[k][0] = yval[k];
            }
        }
        return new CubicSplineFunction(xval, this.c);
    }
}
