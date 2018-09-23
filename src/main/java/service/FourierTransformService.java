package service;

import org.apache.commons.math3.complex.Complex;

import java.util.ArrayList;

/**
 * @author Amosov Maxim - amosov.m@ext-system.com
 * @since 23.09.2018
 */
public class FourierTransformService {

    private static Complex j = new Complex(0, 1); //мнимая единица

    private static Complex w(double k, double N) {
        if (k == 0) {
            return new Complex(1.);
        }
        return new Complex(Math.cos((2. * Math.PI * k) / N), 0).subtract(j.multiply(Math.sin((2. * Math.PI * k) / N)));
    }

    public static ArrayList<Complex> DPF(ArrayList<Complex> x) {
        ArrayList<Complex> c = new ArrayList<>();
        double N = x.size();
        for (int k = 0; k < N; k++) {
            Complex ck = new Complex(0, 0);

            for (int i = 0; i < N; i++) {
//                Complex tmp = w(i, N).pow(k).multiply(x.get(i));    // c += w^i * x
//                Complex tmp = w(i, N).pow(k).multiply(x.get(i));    // c += w^i * x
                Complex temp = polar(1, 2 * Math.PI * i * k / N);
                temp = temp.multiply(x.get(i));
//                temp = temp.divide(N); //??
                ck = ck.add(temp);
            }
            c.add(ck.divide(N));
        }
        return c;
    }

    /**
     * Returns the complex number (r*cos(theta)) + i*(r*sin(theta)).
     */
    public static Complex polar(double r, double theta) {
        return new Complex(r * Math.cos(theta), r * Math.sin(theta));
    }
}
