package util;

import javafx.scene.chart.LineChart;
import model.Signal;
import org.apache.commons.math3.complex.Complex;

import java.util.ArrayList;

/**
 * @author Amosov Maxim - amosov.m@ext-system.com
 * @since 23.09.2018
 */
public class ChartUtil {


    public static void setUp(LineChart chart, String title, Signal signal, char type, double multiplier) {
        chart.setCreateSymbols(false);
        chart.getXAxis().setLabel(signal.getSignalPack().getNameX());
        chart.getYAxis().setLabel(signal.getSignalPack().getNameY());
        chart.getData().addAll(getSeries(toDouble(signal.getData(), type), title, multiplier));
//        ((NumberAxis)chart.getXAxis()).
//        chart.getXAxis().setAutoRanging(false);
//        ((NumberAxis)chart.getXAxis()).setLowerBound(0);
//        ((NumberAxis)chart.getXAxis()).setUpperBound(2);
    }

    public static LineChart.Series getSeries(ArrayList<Double> signals, String signalsName, double multiplier) {
        LineChart.Series series = new LineChart.Series();
        series.setName(signalsName);
        int i = 0;
        for (Double value : signals) {
            series.getData().add(new LineChart.Data(i / multiplier, value));
            i++;
        }

        return series;
    }


    public static ArrayList<Double> toDouble(ArrayList<Complex> list, char param) {
        ArrayList<Double> newList = new ArrayList<Double>();
        for (Complex number : list) {
            switch (param) {
                case 'a':
                    newList.add(number.abs());
                    break;
                case 'r':
                    newList.add(number.getReal());
                    break;
                case 'i':
                    newList.add(number.getImaginary());
                    break;
                case 'm':
                    newList.add(Math.hypot(number.getReal(), number.getImaginary()));
                    break;
                case 'p':
                    newList.add(Math.atan2(number.getImaginary(), number.getReal()));
                    break;
            }
        }
        return newList;
    }
}