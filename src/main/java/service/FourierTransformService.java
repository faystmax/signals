package service;

import org.apache.commons.math3.complex.Complex;
import org.apache.commons.math3.util.Pair;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Amosov Maxim - amosov.m@ext-system.com
 * @since 23.09.2018
 */
public class FourierTransformService {

    private static Complex j = new Complex(0, 1);

    public static ArrayList<Complex> DPF(List<Complex> x, boolean inverse) {
        ArrayList<Complex> c = new ArrayList<>();
        double N = x.size();
        for (int k = 0; k < N; k++) {
            Complex ck = new Complex(0, 0);

            for (int i = 0; i < N; i++) {
                Complex temp = polar(1, 2 * Math.PI * i * k * (inverse ? -1 : 1) / N);
                temp = temp.multiply(x.get(i));
                ck = ck.add(temp);
            }
            c.add(ck.divide((inverse ? 1 : N)));
        }
        return c;
    }

    public static List<Complex> BFT(List<Complex> f) {


        int P = (int) (Math.log(f.size()) / Math.log(2));
        int N = (int) Math.pow(2, P);

        List<Complex> fNew = f.subList(0, N);

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

    public static List<Complex> BPFn(List<Complex> f) {

        ArrayList<Complex> fNew = new ArrayList<>();
        fNew.addAll(f);

        int P = getPowerOfTwo(f.size());

        Pair lm = getLandM(P, f.size());

        int M = (int) lm.getSecond();
        int L = (int) lm.getFirst();
        int N = L * M;

        System.out.println("Size = " + f.size() + "; (2^P = L) * M = " + "(2^" + P + " = " + L + ") * " + M + " = " + M * L);

        for (int i = 0; i < M; i++) {
            List<Complex> bpfnStep = getEveryL(fNew, L, M, i);
            fNew = returnEveryL(fNew, BFT(bpfnStep), L, M, i);
        }

        ArrayList<Complex> bpfn = new ArrayList<>();
        for (int i = 0; i < M * L; i++) {
            bpfn.add(new Complex(0.));
        }

        for (int s = 0; s < M; s++) {
            for (int r = 0; r < L; r++) {
                bpfn.set(r + s * L, new Complex(0.));
                for (int m = 0; m < M; m++) {
                    bpfn.set(r + s * L, bpfn.get(r + s * L).add(
                            fNew.get(m + r * M).multiply(
                                    j.multiply(-1).multiply(2. * Math.PI * m * (r + s * L) / N).exp()
                            )
                    ));
                }
            }
        }

        for (int i = 0; i < N; i++) {
            bpfn.set(i, bpfn.get(i).divide(N));
        }

        return bpfn;
    }

    private static Pair getLandM(int P, int size) {
        int M = 1;
        int eps = size;
        int L = 1;

        do {
            int l = (int) Math.pow(2, P);
            int m = 1;
            do {
                if (eps > size - l * m) {
                    eps = size - l * m;
                    M = m;
                    L = l;
                }
                m++;
            } while (m * l < size && m <= 11);

            P--;
        } while (P >= 2);

        return new Pair<Integer, Integer>(L, M);
    }

    private static List<Complex> getEveryL(ArrayList<Complex> f, int L, int M, int start) {
        List<Complex> fNew = new ArrayList<Complex>();

        for (int i = 0; i < L; i++) {
            fNew.add(f.get(i * M + start));
        }

        return fNew;
    }

    private static ArrayList<Complex> returnEveryL(List<Complex> f, List<Complex> returnF, int L, int M, int start) {
        ArrayList<Complex> fNew = new ArrayList<>();
        fNew.addAll(f);

        for (int i = 0; i < L; i++) {
            fNew.set(i * M + start, returnF.get(i));
        }

        return fNew;
    }

    private static int reverse(int x, int n) {
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

    /**
     * Returns the complex number (r*cos(theta)) + i*(r*sin(theta)).
     */
    private static Complex polar(double r, double theta) {
        return new Complex(r * Math.cos(theta), r * Math.sin(theta));
    }

    private static int getPowerOfTwo(int number) {
        int n = 1;
        while (Math.pow(2, n) <= number) {
            n++;
        }
        return n - 1;
    }

}
