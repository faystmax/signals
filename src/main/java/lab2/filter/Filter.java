package lab2.filter;


import lab1.service.FourierTransformService;
import org.apache.commons.math3.complex.Complex;

import java.util.ArrayList;
import java.util.List;

public class Filter {

    private int N;
    private double fc;
    private List<Complex> signal;
    private ArrayList<Complex> y;
    private ArrayList<Complex> ampityde;
    private ArrayList<Complex> logAmpityde;


    public Filter(double gc, int N, List<Complex> signal) {
        this.fc = 1000. / gc;
        this.N = N;
        this.signal = signal;
        System.out.println("N = " + N);
    }


    public Filter(double gc, int N, double fc, List<Complex> signal) {
        this.fc = fc / (gc / 2.);
        this.N = N;
        this.signal = signal;
        System.out.println("N = " + N);
    }

    private double[] getH(boolean high) {
        double[] h = new double[N];
        double M = N - 1;

        for (int i = 0; i < N; i++) {
            if (i == M / 2.) {
                if (!high) {
                    h[i] = 2. * fc;
                } else {
                    h[i] = 1 - 2. * fc;
                }
                //h[i] = (Math.sin(2. * Math.PI * fc * ((i + 1) - M / 2.))) / (Math.PI * ((i + 1) - M / 2.));
            } else {
                if (!high) {
                    h[i] = (Math.sin(2. * Math.PI * fc * (i - M / 2.))) / (Math.PI * (i - M / 2.));
                } else {
                    h[i] = -1 * (Math.sin(2. * Math.PI * fc * (i - M / 2.))) / (Math.PI * (i - M / 2.));
                }

            }
        }

        return h;
    }

    private double[] getWRectangular() {
        double[] w = new double[N];
        for (int i = 0; i < N; i++) {
            w[i] = 1.;
        }
        return w;
    }

    private double[] getWHamming() {
        double M = N - 1;
        double[] w = new double[N];
        for (int i = 0; i < N; i++) {
            w[i] = 0.54 - 0.46 * Math.cos(2. * Math.PI / M);
        }
        return w;
    }

    private double[] getWHanning() {
        double M = N - 1;
        double[] w = new double[N];
        for (int i = 0; i < N; i++) {
            w[i] = 0.5 - 0.5 * Math.cos(2. * Math.PI / M);
        }
        return w;
    }

    private double[] getWBartlett() {
        double M = N - 1;
        double[] w = new double[N];
        for (int i = 0; i < N; i++) {
            w[i] = 1. - (2 * Math.abs(i - M / 2.)) / M;
        }
        return w;
    }

    private double[] getWBlackman() {
        double M = N - 1;
        double[] w = new double[N];
        for (int i = 0; i < N; i++) {
            w[i] = 0.42 - 0.5 * Math.cos(2. * Math.PI / M) + 0.08 * Math.cos(4. * Math.PI / M);
        }
        return w;
    }

    public ArrayList<Complex> getY(String window, boolean high) {
        y = new ArrayList<>();
        double[] h = getH(high);
        double[] w;

        switch (window) {
            case "Rectangular":
                w = getWRectangular();
                break;
            case "Hamming":
                w = getWHamming();
                break;
            case "Hanning":
                w = getWHanning();
                break;
            case "Bartlett":
                w = getWBartlett();
                break;
            case "Blackman":
                w = getWBlackman();
                break;
            default:
                w = getWRectangular();
                break;
        }

        for (int k = 0; k < signal.size(); k++) {
            Complex tmp = new Complex(0.);
            for (int m = 0; m < (N > k + 1 ? k + 1 : N); m++) {
                tmp = tmp.add(signal.get(k - m).multiply(h[m] * w[m]));
            }
            y.add(tmp);
        }

        calcAmplitude();

        return y;
    }

    private void calcAmplitude() {
        List<Complex> fourier = FourierTransformService.BPF(y, true).getFirst();
        ampityde = new ArrayList<>();
        logAmpityde = new ArrayList<>();
        Complex tmp;

        for (int i = 0; i < fourier.size(); i++) {
            tmp = fourier.get(i);
            ampityde.add(new Complex(Math.sqrt(tmp.getReal() * tmp.getReal() + tmp.getImaginary() * tmp.getImaginary())));
            logAmpityde.add(new Complex(20. * Math.log10(ampityde.get(i).abs())));
        }
    }

    public ArrayList<Complex> getAmplitude() {
        return ampityde;
    }

    public ArrayList<Complex> getLogAmplitude() {
        return logAmpityde;
    }

}
