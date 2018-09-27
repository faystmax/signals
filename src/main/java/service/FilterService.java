package service;

import model.Signal;
import org.apache.commons.math3.complex.Complex;

import java.util.ArrayList;

/**
 * @author Amosov Maxim - amosov.m@ext-system.com
 * @since 27.09.18
 */
public class FilterService {

    public static ArrayList<Complex> lowLevels(ArrayList<Complex> signal) {
        ArrayList<Complex> f = new ArrayList<>();
        double ratio = signal.size() / Signal.FREQUENCY;

        for (int i = 0; i < signal.size(); i++) {
            if (200. * ratio <= i && i <= 250. * ratio) {
                //if(6 <= i && i <= 359){
                f.add(signal.get(i));
            } else {
                f.add(new Complex(0));
            }
        }

        return f;
    }
}
