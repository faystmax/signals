package old;

import org.apache.commons.math3.complex.Complex;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;

public class Helper {

    public static double FREQUENCY = 360.0;            //частота дискретизации в Гц

    public static ArrayList<Double> siganlLoader(String fileName) {
        ArrayList<Double> signals = new ArrayList<Double>();
        File f = new File(fileName);
        BufferedReader fin;
        try {
            fin = new BufferedReader(new FileReader(f));
            String line;
            while ((line = fin.readLine()) != null) {
                signals.add(Double.parseDouble(line));
            }
            fin.close();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return signals;
    }

    public static int reverseBit(int x, int n) {
        int b = 0;
        int i = 0;
        while (x != 0) {
            b <<= 1;
            b |= (x & 1);
            x >>= 1;
            i++;
        }
        if (i < n) {
            b <<= n - i;
        }
        return b;
    }

    public static void siganlSaver(ArrayList<Double> signals, String fileName) {
        try (FileWriter writer = new FileWriter(fileName, false)) {
            for (double signal : signals) {
                writer.write(String.valueOf(signal));
                writer.append("\n");
            }

            writer.flush();
        } catch (IOException ex) {

            System.out.println(ex.getMessage());
        }
    }

    public static ArrayList<Complex> doubleToComplex(ArrayList<Double> list) {
        ArrayList<Complex> newList = new ArrayList<Complex>();
        for (Double number : list) {
            newList.add(new Complex(number));
        }
        return newList;
    }

    public static ArrayList<Double> complexToDouble(ArrayList<Complex> list, char param) {
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

                default:
                    break;
            }

        }
        return newList;
    }

    public static XYSeries getSeries(ArrayList<Double> signals, String signalsName, double multiplier) {
        int i = 0;
        XYSeries series = new XYSeries(signalsName);
        for (Double signal : signals) {
            series.add(((double) i) / multiplier, signal);
            i++;
        }

        return series;
    }

    public static void drawSignal(JPanel panel, String title, String xLable, String yLable, XYSeries... series) {
        XYSeriesCollection dataset = new XYSeriesCollection();
        // dataset.get

        for (XYSeries ser : series) {
            dataset.addSeries(ser);
        }

        JFreeChart chart = ChartFactory.createXYLineChart(title, xLable, yLable, dataset, PlotOrientation.VERTICAL, true, true, true);
        // chart.getRenderingHints().;
        chart.getXYPlot().getRenderer().setSeriesPaint(0, Color.blue);

        //final CategoryPlot plot = (CategoryPlot) chart.getPlot();


        ChartPanel chartpanel = new ChartPanel(chart);
        chartpanel.setDomainZoomable(true);

        panel.removeAll();
        panel.add(chartpanel, BorderLayout.CENTER);
        panel.validate();

    }

    public static int getPowerOfTwo(int number) {
        int n = 1;
        while (Math.pow(2, n) <= number) {
            n++;
        }
        return n - 1;
    }

    public static <T> ArrayList<T> listCopy(ArrayList<T> toCopy) {
        ArrayList<T> copy = new ArrayList<>();
        for (T element : toCopy) {
            copy.add(element);
        }
        return copy;
    }

}
