package lab1.service;

import org.apache.commons.math3.complex.Complex;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Amosov Maxim - amosov.m@ext-system.com
 * @since 28.09.2018
 */
public class AproxService {

    public static ArrayList<Complex> cut(List<Complex> signal, int garmonicCount) {
        ArrayList<Complex> res = new ArrayList<>();
        for (int i = 0; i < signal.size(); i++) {
            res.add(new Complex(0));
            if (i < garmonicCount || i > signal.size() - 1 - garmonicCount) {
                res.set(i, signal.get(i));
            }
        }
        return res;
    }
}
