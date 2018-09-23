package old;

import org.apache.commons.math3.complex.Complex;

import java.util.ArrayList;

public class Filters {

    public static ArrayList<Complex> cardioFilter(ArrayList<Complex> signal) {
        System.out.println("old.Filters.cardioFilter()");
        ArrayList<Complex> f = new ArrayList<Complex>();

        double ratio = signal.size() / FourierTransform.FREQUENCY;

        for (int i = 0; i < signal.size(); i++) {
            if (24. * ratio <= i && i <= 26. * ratio) {
                f.add(signal.get((int) ((24. - 1) * ratio)));
            } else if ((360. - 50.) * ratio <= i && i <= (360. - 49.) * ratio) {
                f.add(signal.get((int) ((49. - 1) * ratio)));
            } else {
                f.add(signal.get(i));
            }
        }

        return f;
    }

    public static ArrayList<Complex> reoFilter(ArrayList<Complex> signal) {
        System.out.println("old.Filters.reoFilter()");
        ArrayList<Complex> f = new ArrayList<Complex>();

        double ratio = signal.size() / FourierTransform.FREQUENCY;

        for (int i = 0; i < signal.size(); i++) {
            if (22. * ratio <= i && i <= (360. - 22.) * ratio) {
                f.add(signal.get((int) ((21. - 1) * ratio)));
            } else {
                f.add(signal.get(i));
            }
        }

        return f;
    }

    public static ArrayList<Complex> veloFilter(ArrayList<Complex> signal) {
        System.out.println("old.Filters.veloFilter()");
        ArrayList<Complex> f = new ArrayList<Complex>();

        double ratio = signal.size() / FourierTransform.FREQUENCY;

        for (int i = 0; i < signal.size(); i++) {
            if (0. * ratio <= i && i <= 1. * ratio) {
                f.add(signal.get((int) (2. * ratio)));
            } else if ((360. - 1.) * ratio <= i && i <= 360. * ratio) {
                f.add(signal.get((int) ((360. - 1) * ratio)));
            } else {
                f.add(signal.get(i));
            }
        }

        return f;
    }

    public static ArrayList<Complex> standartFilter(ArrayList<Complex> signal) {
        System.out.println("old.Filters.standartFilter()");
        ArrayList<Complex> f = new ArrayList<Complex>();

        double ratio = signal.size() / FourierTransform.FREQUENCY;

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
