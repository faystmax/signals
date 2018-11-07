package lab1.service;

import org.apache.commons.math3.complex.Complex;

import java.util.List;

/**
 * @author Amosov Maxim - amosov.m@ext-system.com
 * @since 27.09.18
 */
public class FilterService {

    public static void low(List<Complex> signal, int freq, int val) {
        val = calcVal(signal.size(), freq, val);
        for (int i = val; i < signal.size() - val; i++) {
            signal.set(i, new Complex(0));
        }
    }

    public static void lowD(List<Double> signal, int freq, int val) {
        val = calcVal(signal.size(), freq, val);
        for (int i = val; i < signal.size() - val; i++) {
            signal.set(i, (double) 0);
        }
    }

    public static void high(List<Complex> signal, int freq, int val) {
        val = calcVal(signal.size(), freq, val);
        for (int i = 0; i < val; i++) {
            signal.set(i, new Complex(0));
        }
        for (int i = signal.size() - val; i < signal.size(); i++) {
            signal.set(i, new Complex(0));
        }
    }

    public static void highD(List<Double> signal, int freq, int val) {
        val = calcVal(signal.size(), freq, val);
        for (int i = 0; i < val; i++) {
            signal.set(i, 0.);
        }
        for (int i = signal.size() - val; i < signal.size(); i++) {
            signal.set(i, 0.);
        }
    }

    private static int calcVal(int size, int freq, int val) {
        double step = 1. / (size / freq);
        return (int) (val / step);
    }

    public static List<Complex> low(List<Complex> signal) {
        for (int i = 30; i < signal.size() - 30; i++) {
            signal.set(i, new Complex(0));
        }
        return signal;
    }

    public static List<Complex> high(List<Complex> signal) {
        for (int i = 0; i < 24; i++) {
            signal.set(i, new Complex(0));
        }
        for (int i = signal.size() - 24; i < signal.size(); i++) {
            signal.set(i, new Complex(0));
        }
        return signal;
    }

    public static List<Complex> reject(List<Complex> signal) {
        for (int i = 98; i < 192; i++) {
            signal.set(i, new Complex(0));
        }
        return signal;
    }

    public static List<Complex> stripe(List<Complex> signal) {
        for (int i = 0; i < 49; i++) {
            signal.set(i, new Complex(0));
        }
        for (int i = 50; i < signal.size(); i++) {
            signal.set(i, new Complex(0));
        }
        return signal;
    }
}
