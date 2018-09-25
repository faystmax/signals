package service;

import org.apache.commons.math3.complex.Complex;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Amosov Maxim - amosov.m@ext-system.com
 * @since 23.09.2018
 */
public class FourierTransformService {

    private static Complex j = new Complex(0, 1);

    public static ArrayList<Complex> DPF(List<Complex> x) {
        ArrayList<Complex> c = new ArrayList<>();
        double N = x.size();
        for (int k = 0; k < N; k++) {
            Complex ck = new Complex(0, 0);

            for (int i = 0; i < N; i++) {
                Complex temp = polar(1, 2 * Math.PI * i * k / N);
                temp = temp.multiply(x.get(i));
//                temp = temp.divide(N); //??
                ck = ck.add(temp);
            }
            c.add(ck.divide(N));
        }
        return c;
    }

    public static List<Complex> FFT(List<Complex> x, boolean inverse, boolean useRecursion) {
        List<Complex> result = useRecursion ? RFFT(x, inverse) : IFFT(x, inverse);
//        var result = fft.Select(c = > c / (inverse ? 1 : fft.Length)).ToArray();
        return result;
    }


    public static List<Complex> IFFT(List<Complex> f, boolean inverse) {
        ArrayList<Complex> fNew = new ArrayList<Complex>();

        int P = (int) (Math.log(f.size()) / Math.log(2));
        int N = (int) Math.pow(2, P);

        for (int i = 0; i < N; i++) {
            fNew.add(f.get(i));
        }

        for (int i = 0; i < N; i++) {
            int j = reverse(i, P);
            fNew.set(j, f.get(i));
        }

        for (int s = 1; s <= P; s++) {
            double m = Math.pow(2, s);
            Complex wm = j.multiply(2. * Math.PI).divide(m).exp();

            for (int k = 0; k < N; k += m) {
                Complex w = new Complex(1.);

                for (int j = 0; j < m / 2; j++) {

                    int id1 = (k + j);
                    int id2 = (int) (id1 + m / 2);

                    if (id2 >= fNew.size()) {
                        break;
                    }
                    Complex u = fNew.get(id1);
                    Complex t = fNew.get(id2).multiply(w);

                    fNew.set(id1, u.add(t));
                    fNew.set(id2, u.subtract(t));

                    w = w.multiply(wm);
                }
            }

        }


        return fNew;
    }


    public static int reverse(int x, int n) {
        int b = 0;
        int i = 0;
        while (x != 0) {
            b <<= 1;
            b |= (x & 1);
            x >>= 1;
            i++;
        }
        if (i < n)
            b <<= n - i;
        return b;
    }

    public static List<Complex> RFFT(List<Complex> x, boolean inverse) {
        int N = x.size();
        Complex[] C = new Complex[N];

        if (N == 1) {
            C[0] = x.get(0);
            return Arrays.asList(C);
        }
        int k;
        List<Complex> F1 = x.stream().filter(v -> v.getImaginary() % 2 == 0).collect(Collectors.toList());
        List<Complex> F0 = x.stream().filter(v -> v.getImaginary() % 2 != 0).collect(Collectors.toList());

        List<Complex> C1 = RFFT(F1, inverse);
        List<Complex> C0 = RFFT(F0, inverse);

        for (k = 0; k < N / 2; k++) {
            Complex W = polar(1, -2 * Math.PI * k / N * (inverse ? 1 : -1));
            C[k] = C0.get(k).add(C1.get(k).multiply(W));
            C[k + N / 2] = C0.get(k).subtract(C1.get(k).multiply(W));
        }
        return Arrays.asList(C);
    }


    /**
     * Returns the complex number (r*cos(theta)) + i*(r*sin(theta)).
     */
    public static Complex polar(double r, double theta) {
        return new Complex(r * Math.cos(theta), r * Math.sin(theta));
    }
}
