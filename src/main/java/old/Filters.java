package old;

import java.util.ArrayList;

public class Filters {

    public static ArrayList<Double> cardioFilter(ArrayList<Double> signal) {
        ArrayList<Double> f = new ArrayList<Double>();

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

    public static ArrayList<Double> reoFilter(ArrayList<Double> signal) {
        ArrayList<Double> f = new ArrayList<Double>();

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

    public static ArrayList<Double> veloFilter(ArrayList<Double> signal) {
        ArrayList<Double> f = new ArrayList<Double>();

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

    public static ArrayList<Double> standartFilter(ArrayList<Double> signal) {
        ArrayList<Double> f = new ArrayList<Double>();

        double ratio = signal.size() / FourierTransform.FREQUENCY;

        for (int i = 0; i < signal.size(); i++) {
            if (0. * ratio <= i && i <= 50. * ratio) {
                f.add(signal.get(i));
            } else {
                f.add(new Double(0));
            }
        }

        return f;
    }

}
