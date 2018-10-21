package lab3.util;

import javafx.scene.chart.LineChart;
import lab3.model.SignalSimple;

import java.util.List;

/**
 * @author Amosov Maxim - amosov.m@ext-system.com
 * @since 23.09.2018
 */
public class ChartUtil {


    public static void setUp(LineChart<Double, Double> chart, String seriesName, SignalSimple signal, double multiplier, int skip) {
        chart.setCreateSymbols(false);
        chart.getXAxis().setLabel(signal.getSignalPack().getNameX());
        chart.getYAxis().setLabel(signal.getSignalPack().getNameY());
        chart.getData().addAll(getSeries(signal.getData(), seriesName, multiplier, signal.getSignalPack().getWidth(), skip));
    }

    public static void setUp(LineChart<Double, Double> chart, String seriesName, SignalSimple signal, double multiplier) {
        chart.setCreateSymbols(false);
        chart.getXAxis().setLabel(signal.getSignalPack().getNameX());
        chart.getYAxis().setLabel(signal.getSignalPack().getNameY());
        chart.getData().addAll(getSeries(signal.getData(), seriesName, multiplier, signal.getSignalPack().getWidth(), 0));
    }

    private static LineChart.Series getSeries(List<Double> signals, String seriesName, double multiplier, double size, int skip) {
        LineChart.Series<Double, Double> series = new LineChart.Series<>();
        series.setName(seriesName);
        skip++;
        for (int i = 0; i < signals.size() * size; i += skip) {
            series.getData().add(new LineChart.Data<>(i / multiplier, signals.get(i)));
        }
        return series;
    }

}
