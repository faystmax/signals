package lab1.util;

import javafx.scene.chart.LineChart;
import lab1.model.Signal;
import org.apache.commons.math3.complex.Complex;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Amosov Maxim - amosov.m@ext-system.com
 * @since 23.09.2018
 */
public class ChartUtil {


    public static void setUp(LineChart<Double, Double> chart, String seriesName, Signal signal, char type, double multiplier, int skip) {
        chart.setCreateSymbols(false);
        chart.getXAxis().setLabel(signal.getSignalPack().getNameX());
        chart.getYAxis().setLabel(signal.getSignalPack().getNameY());
        chart.getData().addAll(getSeries(toDouble(signal.getData(), type), seriesName, multiplier, signal.getSignalPack().getWidth(), skip));
    }

    public static void setUp(LineChart<Double, Double> chart, String seriesName, Signal signal, char type, double multiplier) {
        chart.setCreateSymbols(false);
        chart.getXAxis().setLabel(signal.getSignalPack().getNameX());
        chart.getYAxis().setLabel(signal.getSignalPack().getNameY());
        chart.getData().addAll(getSeries(toDouble(signal.getData(), type), seriesName, multiplier, signal.getSignalPack().getWidth(), 0));
    }

    private static LineChart.Series getSeries(ArrayList<Double> signals, String seriesName, double multiplier, double size, int skip) {
        LineChart.Series<Double, Double> series = new LineChart.Series<>();
        series.setName(seriesName);
        skip++;
        for (int i = 0; i < signals.size() * size; i += skip) {
            series.getData().add(new LineChart.Data<>(i / multiplier, signals.get(i)));
        }
        return series;
    }

    private static ArrayList<Double> toDouble(List<Complex> list, char param) {
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
