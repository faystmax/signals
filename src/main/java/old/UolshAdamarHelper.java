package old;

import java.util.ArrayList;
import java.util.List;

public class UolshAdamarHelper {

    public static ArrayList<Double> getUolshTransform(List<Double> signal, boolean invers) {
        ArrayList<Double> uolsh = new ArrayList<>();

        int P = Helper.getPowerOfTwo(signal.size());
        double N = Math.pow(2, P);

        System.out.println("2^" + P + "= " + N + "; size = " + signal.size());

        for (int k = 0; k < N; k++) {
            double tmp = 0.;

            for (int i = 0; i < N; i++) {
                tmp += signal.get(i) * getUolsh((int) N, k, i);
            }

            if (invers) {
                uolsh.add(tmp);
            } else {
                uolsh.add(tmp / N);
            }

        }

        return uolsh;
    }

    public static ArrayList<Double> getAdamarTransform(List<Double> signal, boolean invers) {
        ArrayList<Double> adamar = new ArrayList<>();

        int P = Helper.getPowerOfTwo(signal.size());
        double N = Math.pow(2, P);

        System.out.println("2^" + P + "= " + N + "; size = " + signal.size());

        for (int k = 0; k < N; k++) {
            double tmp = 0.;

            for (int i = 0; i < N; i++) {
                tmp += signal.get(i) * getAdamar((int) N, k, i);
            }

            if (invers) {
                adamar.add(tmp);
            } else {
                adamar.add(tmp / N);
            }

        }

        return adamar;
    }

    public static ArrayList<Double> getAmplitude(List<Double> signal) {
        ArrayList<Double> amplitude = new ArrayList<>();

        for (int i = 0; i < signal.size() - 1; i++) {
            amplitude.add(Math.sqrt(signal.get(i) * signal.get(i) + signal.get(i + 1) * signal.get(i + 1)));
        }

        return amplitude;
    }

    public static ArrayList<Double> getPhase(List<Double> signal) {
        ArrayList<Double> phase = new ArrayList<>();

        for (int i = 0; i < signal.size() - 1; i++) {
            phase.add(Math.atan2(signal.get(i), signal.get(i + 1)));
        }

        return phase;
    }

    public static ArrayList<Double> getAprox(List<Double> signal, int from, int to) {
        ArrayList<Double> aprox = new ArrayList<>();

        if (from > signal.size()) {
            from = 0;
        }

        if (to > signal.size()) {
            to = signal.size();
        }

        for (int i = 0; i < from; i++) {
            aprox.add(0.);

        }

        for (int i = from; i < to; i++) {
            aprox.add(signal.get(i));
        }

        for (int i = to; i < signal.size(); i++) {
            aprox.add(0.);

        }

        return aprox;
    }

    public static int getUolsh(int N, int u, int v) {
        int n = (int) (Math.log(N) / Math.log(2));
        int R[] = new int[n];
        int ut = u;
        int sr = 1 << (n - 1);
        R[0] = (ut & sr) != 0 ? 1 : 0;
        for (int i = 1; i < n; i++) {
            R[i] = (ut & sr) != 0 ? 1 : 0;
            sr >>= 1;
            R[i] += (ut & sr) != 0 ? 1 : 0;
            R[i] %= 2;
        }
        int vt = v;
        int sum = 0;
        for (int i = 0; i < n; i++) {
            sum += R[i] * (vt & 1);
            vt >>= 1;
        }
        return (sum % 2) == 0 ? 1 : -1;
    }

    public static int getAdamar(int N, int u, int v) {
        int n = (int) (Math.log(N) / Math.log(2));
        int ut = decodeGray(reverseBit(u, n));
        int sr = 1 << (n - 1);
        int R[] = new int[n];
        R[0] = (ut & sr) != 0 ? 1 : 0;
        for (int i = 1; i < n; i++) {
            R[i] = (ut & sr) != 0 ? 1 : 0;
            sr >>= 1;
            R[i] += (ut & sr) != 0 ? 1 : 0;
            R[i] %= 2;
        }
        int vt = v;
        int sum = 0;
        for (int i = 0; i < n; i++) {
            sum += R[i] * (vt & 1);
            vt >>= 1;

        }
        return (sum % 2) == 0 ? 1 : -1;
    }

    protected static int reverseBit(int x, int n) {
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

    private static int decodeGray(int gray) {
        int bin;
        for (bin = 0; gray > 0; gray >>= 1) {
            bin ^= gray;
        }
        return bin;
    }

}
