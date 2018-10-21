package old;

import java.util.ArrayList;

public class PrimitivesGenerator {

    public static ArrayList<Double> getSaw(double A, double T, double tau, double N) {
        ArrayList<Double> saw = new ArrayList<Double>();

        double step = A / (N / 2.);
        double value = 0.;
        for (int i = 0; i <= N / 2.; i++) {
            saw.add(value);
            value += step;
        }

        value *= (-1);

        for (int i = (int) (N / 2.); i <= N; i++) {
            saw.add(value);
            value += step;
        }

        return saw;
    }

    public static ArrayList<Double> getAngle(double A, double T, double tau, double N) {
        ArrayList<Double> angle = new ArrayList<Double>();

        double step = A / (N / 2.);
        double value = 0.;
        for (int i = 0; i <= N / 2.; i++) {
            angle.add(value);
            value += step;
        }

        for (int i = (int) (N / 2.); i <= N; i++) {
            angle.add(value);
            value -= step;
        }

        return angle;
    }

    public static ArrayList<Double> getLevels(double A, double T, double tau, double N) {
        ArrayList<Double> levels = new ArrayList<Double>();

        for (int i = 0; i <= 51.; i++) {
            levels.add(10.);
        }

        for (int i = 51; i <= N; i++) {
            levels.add(0.);
        }

        return levels;
    }

}
