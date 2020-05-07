// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.math.distribution;

class NormalCDFPreciseAlgorithm implements NormalCDFAlgorithm
{
    public double cdf(final double x) {
        final double[] a = { 2.2352520354606837, 161.02823106855587, 1067.6894854603709, 18154.98125334356, 0.06568233791820745 };
        final double[] b = { 47.202581904688245, 976.0985517377767, 10260.932208618979, 45507.78933502673 };
        final double[] c = { 0.39894151208813466, 8.883149794388377, 93.50665613217785, 597.2702763948002, 2494.5375852903726, 6848.190450536283, 11602.65143764735, 9842.714838383978, 1.0765576773720192E-8 };
        final double[] d = { 22.266688044328117, 235.387901782625, 1519.3775994075547, 6485.558298266761, 18615.571640885097, 34900.95272114598, 38912.00328609327, 19685.429676859992 };
        final double[] p = { 0.215898534057957, 0.12740116116024736, 0.022235277870649807, 0.0014216191932278934, 2.9112874951168793E-5, 0.023073441764940174 };
        final double[] q = { 1.284260096144911, 0.4682382124808651, 0.06598813786892856, 0.0037823963320275824, 7.297515550839662E-5 };
        final double sixten = 16.0;
        final double M_1_SQRT_2PI = 0.3989422804014327;
        final double M_SQRT_32 = 5.656854249492381;
        final double eps = 0.5 * Math.pow(2.0, -59.0);
        final double min = Double.MIN_VALUE;
        double ccum = 0.0;
        double cum = 0.0;
        double xden = 0.0;
        double xnum = 0.0;
        double xsq = 0.0;
        final double y = Math.abs(x);
        if (y <= 0.67448975) {
            if (y > eps) {
                xsq = x * x;
            }
            xnum = a[4] * xsq;
            xden = xsq;
            for (int i = 0; i < 3; ++i) {
                xnum = (xnum + a[i]) * xsq;
                xden = (xden + b[i]) * xsq;
            }
            final double temp = x * (xnum + a[3]) / (xden + b[3]);
            cum = 0.5 + temp;
            ccum = 0.5 - temp;
        }
        else if (y <= 5.656854249492381) {
            xnum = c[8] * y;
            xden = y;
            for (int i = 0; i < 7; ++i) {
                xnum = (xnum + c[i]) * y;
                xden = (xden + d[i]) * y;
            }
            double temp = (xnum + c[7]) / (xden + d[7]);
            xsq = Math.floor(y * 16.0) / 16.0;
            final double del = (y - xsq) * (y + xsq);
            cum = Math.exp(-(xsq * xsq * 0.5)) * Math.exp(-(del * 0.5)) * temp;
            ccum = 1.0 - cum;
            if (x > 0.0) {
                temp = cum;
                cum = ccum;
                ccum = temp;
            }
        }
        else if ((-37.5193 < x && x < 8.2924) || (-8.2924 < x && x < 37.5193)) {
            xsq = 1.0 / (x * x);
            xnum = p[5] * xsq;
            xden = xsq;
            for (int i = 0; i < 4; ++i) {
                xnum = (xnum + p[i]) * xsq;
                xden = (xden + q[i]) * xsq;
            }
            double temp = xsq * (xnum + p[4]) / (xden + q[4]);
            temp = (0.3989422804014327 - temp) / y;
            xsq = Math.floor(x * 16.0) / 16.0;
            final double del = (x - xsq) * (x + xsq);
            cum = Math.exp(-(xsq * xsq * 0.5)) * Math.exp(-(del * 0.5)) * temp;
            ccum = 1.0 - cum;
            if (x > 0.0) {
                temp = cum;
                cum = (ccum = ccum);
            }
        }
        else if (x > 0.0) {
            cum = 1.0;
            ccum = 0.0;
        }
        else {
            cum = 0.0;
            ccum = 1.0;
        }
        if (cum < Double.MIN_VALUE) {
            cum = 0.0;
        }
        if (ccum < Double.MIN_VALUE) {
            ccum = 0.0;
        }
        return cum;
    }
}
