package lab3.service;

import java.util.List;

/**
 * @author Amosov Maxim - amosov.m@ext-system.com
 * @since 27.09.18
 */
public class FilterService {

    public static List<Double> low(List<Double> signal, int val) {
        for (int i = val; i < signal.size() - val; i++) {
            signal.set(i, 0.);
        }
        return signal;
    }

    public static List<Double> high(List<Double> signal, int val) {
        for (int i = 0; i < val; i++) {
            signal.set(i, (double) 0);
        }
        for (int i = signal.size() - val; i < signal.size(); i++) {
            signal.set(i, (double) 0);
        }
        return signal;
    }

    public static List<Double> reject(List<Double> signal, int val) {
        for (int i = 98; i < 192; i++) {
            signal.set(i, (double) 0);
        }
        return signal;
    }

    public static List<Double> stripe(List<Double> signal) {
        for (int i = 0; i < 49; i++) {
            signal.set(i, (double) 0);
        }
        for (int i = 50; i < signal.size(); i++) {
            signal.set(i, (double) 0);
        }
        return signal;
    }
}
