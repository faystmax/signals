package lab4.service;

import java.util.ArrayList;
import java.util.List;

import static lab3.service.UolshAdamarService.getPowerOfTwo;

/**
 * @author Amosov Maxim - amosov.m@ext-system.com
 * @since 05.11.2018
 */
public class WaveletService {

    public static List<Double> getHaarTransform(List<Double> signal, int scaleFactor) {

        int P = getPowerOfTwo(signal.size());
        double N = Math.pow(2, P);
        List<Double> result = new ArrayList<>();

        for (int i = 0; i < N; i++) {
            result.add(signal.get(i));
        }
        System.out.println("2^" + P + "= " + N + "; size = " + signal.size());

        int n = (int) N;
        int stop = 1 << scaleFactor;
        while (n > stop) {
            int half = n >> 1;
            ArrayList<Double> sum = new ArrayList<>(half);
            ArrayList<Double> diff = new ArrayList<>(half);
            for (int i = 0; i < half; i++) {
                sum.add((result.get(i * 2) + result.get(i * 2 + 1)) / 2.0);
                diff.add((result.get(i * 2) - result.get(i * 2 + 1)) / 2.0);
            }
            for (int i = 0; i < half; i++) {
                result.set(i, sum.get(i));
                result.set(i + half, diff.get(i));
            }
            n = n >> 1;
        }
        return result;
    }

    public static List<Double> getHaarInverseTransform(List<Double> signal, int scaleFactor) {
        List<Double> result = new ArrayList<>(signal);
        int n = 2 * (1 << scaleFactor);
        while (n <= result.size()) {
            int half = n >> 1;
            ArrayList<Double> sum = new ArrayList<>(half);
            ArrayList<Double> diff = new ArrayList<>(half);
            for (int i = 0; i < half; i++) {
                sum.add(result.get(i));
                diff.add(result.get(i + half));
            }
            for (int i = 0; i < half; i++) {
                result.set(2 * i, (sum.get(i) + diff.get(i)));
                result.set(2 * i + 1, (sum.get(i) - diff.get(i)));
            }
            n <<= 1;
        }
        return result;
    }

    private static double h1 = (3 + Math.sqrt(3)) / (4 * Math.sqrt(2));
    private static double h2 = (3 - Math.sqrt(3)) / (4 * Math.sqrt(2));
    private static double h3 = (1 - Math.sqrt(3)) / (4 * Math.sqrt(2));
    private static double h0 = (1 + Math.sqrt(3)) / (4 * Math.sqrt(2));

    private static double g0 = h3;
    private static double g1 = -h2;
    private static double g2 = h1;
    private static double g3 = -h0;

    private static double invH0 = h2;
    private static double invH1 = g2;  // h1
    private static double invH2 = h0;
    private static double invH3 = g0;  // h3

    private static double invG0 = h3;
    private static double invG1 = g3;  // -h0
    private static double invG2 = h1;
    private static double invG3 = g1;  // -h2


    public static List<Double> getDaubechiesTransform(List<Double> signal, int scaleFactor) {
        int P = getPowerOfTwo(signal.size());
        double N = Math.pow(2, P);

        List<Double> result = new ArrayList<>();

        for (int i = 0; i < N; i++) {
            result.add(signal.get(i));
        }
        System.out.println("2^" + P + "= " + N + "; size = " + signal.size());


        for (int n = (int) N; n >= 4 * (1 << (scaleFactor)); n >>= 1) {

            if (n >= 4) {
                int i, j;
                int half = n >> 1;

                List<Double> tmp = new ArrayList<>();

                for (int k = 0; k < N; k++) {
                    tmp.add(signal.get(k));
                }

                i = 0;
                for (j = 0; j < n - 3; j = j + 2) {
                    tmp.set(i, result.get(j) * h0 +
                               result.get(j + 1) * h1 +
                               result.get(j + 2) * h2 +
                               result.get(j + 3) * h3);
                    tmp.set(i + half, result.get(j) * g0 +
                                      result.get(j + 1) * g1 +
                                      result.get(j + 2) * g2 +
                                      result.get(j + 3) * g3);
                    i++;
                }

                tmp.set(i, result.get(n - 2) * h0 +
                           result.get(n - 1) * h1 +
                           result.get(0) * h2 +
                           result.get(1) * h3);
                tmp.set(i + half, result.get(n - 2) * g0 +
                                  result.get(n - 1) * g1 +
                                  result.get(0) * g2 +
                                  result.get(1) * g3);

                for (i = 0; i < n; i++) {
                    result.set(i, tmp.get(i));
                }
            }
        }
        return result;
    }

    public static List<Double> getDaubechiesInverseTransform(List<Double> signal, int scaleFactor) {
        List<Double> result = new ArrayList<>(signal);
        int N = result.size();
        int n;
        for (n = 4 * (1 << scaleFactor); n <= N; n <<= 1) {
            int i, j;
            int half = n >> 1;
            int halfPls1 = half + 1;

            List<Double> tmp = new ArrayList<>();

            for (int k = 0; k < N; k++) {
                tmp.add(signal.get(k));
            }

            tmp.set(0, result.get(half - 1) * invH0 +
                       result.get(n - 1) * invH1 +
                       result.get(0) * invH2 +
                       result.get(half) * invH3);
            tmp.set(1, result.get(half - 1) * invG0 +
                       result.get(n - 1) * invG1 +
                       result.get(0) * invG2 +
                       result.get(half) * invG3);
            j = 2;
            for (i = 0; i < half - 1; i++) {
                tmp.set(j++, result.get(i) * invH0 +
                             result.get(i + half) * invH1 +
                             result.get(i + 1) * invH2 +
                             result.get(i + halfPls1) * invH3);
                tmp.set(j++, result.get(i) * invG0 +
                             result.get(i + half) * invG1 +
                             result.get(i + 1) * invG2 +
                             result.get(i + halfPls1) * invG3);

            }
            for (i = 0; i < n; i++) {
                result.set(i, tmp.get(i));
            }
        }
        return result;
    }

    public static List<Double> filterMinMax(List<Double> signal, int min, int max) {
        List<Double> result = new ArrayList<>(signal);
        for (int i = min, ei = Math.min(max, signal.size()); i < ei; i++) {
            result.set(i, 0.0);
        }
        return result;
    }

    private static void multiply(double[][] matrix, List<Double> vector) {
        int n = vector.size();
        double[] result = new double[n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                result[i] += matrix[i][j] * vector.get(j);
            }
        }
        for (int i = 0; i < n; i++) {
            vector.set(i, result[i]);
        }
    }

    private static double[][] getTransformMatrix(double[][] matrix) {
        int n = matrix.length;
        double[][] result = new double[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                result[i][j] = matrix[j][i];
            }
        }
        return result;
    }

    private static double[][] getHaarMatrix(int n) {
        double[][] a = new double[n][n];
        double value = 1.0 / Math.sqrt(2);
        for (int i = 0, ei = n / 2; i < ei; i++) {
            a[i][i * 2] = value;
            a[i][i * 2 + 1] = value;
            a[i + ei][i * 2] = value;
            a[i + ei][i * 2 + 1] = -value;
        }
        return a;
    }

    private static double[][] getDaubechiesMatrix(int n) {
        double[][] a = new double[n][n];
        double c0 = getC0();
        double c1 = getC1();
        double c2 = getC2();
        double c3 = getC3();
        for (int i = 0, ei = n / 2; i < ei; i++) {
            a[i][i * 2] = c0;
            a[i][i * 2 + 1] = c1;
            a[i][(i * 2 + 2) % n] = c2;
            a[i][(i * 2 + 3) % n] = c3;
            a[i + ei][i * 2] = c3;
            a[i + ei][i * 2 + 1] = -c2;
            a[i + ei][(i * 2 + 2) % n] = c1;
            a[i + ei][(i * 2 + 3) % n] = -c0;
        }
        return a;
    }

    private static double getC0() {
        return (1.0 + Math.sqrt(3.0)) / (4.0 * Math.sqrt(2.0));
    }

    private static double getC1() {
        return (3.0 + Math.sqrt(3.0)) / (4.0 * Math.sqrt(2.0));
    }

    private static double getC2() {
        return (3.0 - Math.sqrt(3.0)) / (4.0 * Math.sqrt(2.0));
    }

    private static double getC3() {
        return (1.0 - Math.sqrt(3.0)) / (4.0 * Math.sqrt(2.0));
    }
}
