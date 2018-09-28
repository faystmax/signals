package service;

import org.apache.commons.math3.complex.Complex;

import java.util.ArrayList;

/**
 * @author Amosov Maxim - amosov.m@ext-system.com
 * @since 27.09.18
 */
public class FilterService {

    public static ArrayList<Complex> low(ArrayList<Complex> signal) {
        for (int i = 30; i < signal.size() - 30; i++) {
            signal.set(i, new Complex(0));
        }
        return signal;
    }

    public static ArrayList<Complex> high(ArrayList<Complex> signal) {
        for (int i = 0; i < 24; i++) {
            signal.set(i, new Complex(0));
        }
        for (int i = signal.size() - 24; i < signal.size(); i++) {
            signal.set(i, new Complex(0));
        }
        return signal;
    }

    public static ArrayList<Complex> reject(ArrayList<Complex> signal) {
        for (int i = 98; i < 192; i++) {
            signal.set(i, new Complex(0));
        }
        return signal;

    }

    public static ArrayList<Complex> stripe(ArrayList<Complex> signal) {
        for (int i = 0; i < 49; i++) {
            signal.set(i, new Complex(0));
        }
        for (int i = 50; i < signal.size(); i++) {
            signal.set(i, new Complex(0));
        }
        return signal;
    }
}
