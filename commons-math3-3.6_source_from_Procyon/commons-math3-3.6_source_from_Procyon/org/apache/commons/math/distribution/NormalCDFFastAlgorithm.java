// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.math.distribution;

class NormalCDFFastAlgorithm implements NormalCDFAlgorithm
{
    public double cdf(double x) {
        final double ltone = 7.0;
        final double utzero = 18.66;
        final double con = 1.28;
        final double p = 0.398942280444;
        final double q = 0.399903438504;
        final double r = 0.398942280385;
        final double a1 = 5.75885480458;
        final double a2 = 2.62433121679;
        final double a3 = 5.92885724438;
        final double b1 = -29.8213557808;
        final double b2 = 48.6959930692;
        final double c1 = -3.8052E-8;
        final double c2 = 3.98064794E-4;
        final double c3 = -0.151679116635;
        final double c4 = 4.8385912808;
        final double c5 = 0.742380924027;
        final double c6 = 3.99019417011;
        final double d1 = 1.00000615302;
        final double d2 = 1.98615381364;
        final double d3 = 5.29330324926;
        final double d4 = -15.1508972451;
        final double d5 = 30.789933034;
        boolean upper = false;
        if (x < 0.0) {
            upper = !upper;
            x = -x;
        }
        double alnorm;
        if (x <= 7.0 || (upper && x <= 18.66)) {
            final double y = 0.5 * x * x;
            if (x > 1.28) {
                alnorm = 0.398942280385 * Math.exp(-y) / (x - 3.8052E-8 + 1.00000615302 / (x + 3.98064794E-4 + 1.98615381364 / (x - 0.151679116635 + 5.29330324926 / (x + 4.8385912808 + -15.1508972451 / (x + 0.742380924027 + 30.789933034 / (x + 3.99019417011))))));
            }
            else {
                alnorm = 0.5 - x * (0.398942280444 - 0.399903438504 * y / (y + 5.75885480458 + -29.8213557808 / (y + 2.62433121679 + 48.6959930692 / (y + 5.92885724438))));
            }
        }
        else {
            alnorm = 0.0;
        }
        if (!upper) {
            alnorm = 1.0 - alnorm;
        }
        return alnorm;
    }
}
