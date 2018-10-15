package lab1.controller;

import common.BaseController;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.stage.FileChooser;
import lab1.model.Signal;
import lab1.model.SignalBundle;
import lab1.service.AproxService;
import lab1.service.FilterService;
import lab1.service.FourierTransformService;
import lab1.util.ChartUtil;
import lab1.util.LoaderUtil;
import lab1.util.SignalsGeneratorUtil;
import org.apache.commons.math3.complex.Complex;
import org.apache.commons.math3.util.Pair;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Amosov Maxim - amosov.m@ext-system.com
 * @since 23.09.2018
 */
public class Lab1Controller extends BaseController {

    @FXML private Label fileLabel;
    @FXML private Label descBpfLabel;
    @FXML private Label descBpfnLabel;
    @FXML private Label dpfTimeLabel;
    @FXML private Label bpfTimeLabel;
    @FXML private Label bpfnTimeLabel;
    @FXML private RadioButton lowRadioBtn;
    @FXML private RadioButton highRadioBen;
    @FXML private RadioButton rejectRadioBtn;
    @FXML private RadioButton stripeRadioBtn;
    @FXML private LineChart<Double, Double> mainChart;
    @FXML private LineChart<Double, Double> amplChart;
    @FXML private LineChart<Double, Double> phaseChart;

    private File selectedFile;
    private Signal signal;

    @FXML
    protected void initialize() {
        clear();
        SignalsGeneratorUtil.generate();
    }

    @FXML
    private void openFileChooser() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Выберите файл с сигналом");
        fileChooser.setInitialDirectory(new File("./data/signal"));
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("txt", "*.txt"));
        selectedFile = fileChooser.showOpenDialog(stage);
        fileLabel.setText(selectedFile == null ? "..." : selectedFile.getName());

        loadSignal();
    }

    private void loadSignal() {
        if (selectedFile != null) {
            ArrayList<Complex> signals = LoaderUtil.loadSignals(selectedFile);
            for (String name : SignalBundle.myMap.keySet()) {
                if (selectedFile.getName().contains(name)) {
                    signal = new Signal(SignalBundle.myMap.get(name), signals);
                } else {
                    signal = new Signal(SignalBundle.myMap.get("default"), signals);
                }
            }
            show(signal);
        }
    }

    private void show(Signal signal) {
        clear();
        mainChart.getData().clear();
        ChartUtil.setUp(mainChart, "Оригинал", signal, 'r', Signal.FREQUENCY);
    }

    @FXML
    private void clear() {
        descBpfLabel.setText("");
        descBpfnLabel.setText("");
        amplChart.getData().clear();
        phaseChart.getData().clear();
        dpfTimeLabel.setText(String.format("%02dч %02dм %02dс %04dмс", 0, 0, 0, 0));
        bpfTimeLabel.setText(String.format("%02dч %02dм %02dс %04dмс", 0, 0, 0, 0));
        bpfnTimeLabel.setText(String.format("%02dч %02dм %02dс %04dмс", 0, 0, 0, 0));
    }

    @FXML
    private void dpf() {
        long startTime = System.currentTimeMillis();
        ArrayList<Complex> result = FourierTransformService.DPF(signal.getData(), false);
        long totalTime = System.currentTimeMillis() - startTime;
        dpfTimeLabel.setText(convertSecondsToHMmSs(totalTime));
        ChartUtil.setUp(amplChart, "ДПФ", new Signal(SignalBundle.myMap.get("gz"), result), 'm', result.size() / Signal.FREQUENCY);
        ChartUtil.setUp(phaseChart, "ДПФ", new Signal(SignalBundle.myMap.get("gz"), result), 'p', result.size() / Signal.FREQUENCY);
    }

    @FXML
    private void bpf() {
        long startTime = System.currentTimeMillis();
        Pair<List<Complex>, String> pair = FourierTransformService.BPF(signal.getData(), false);
        long totalTime = System.currentTimeMillis() - startTime;
        descBpfLabel.setText(pair.getSecond());
        bpfTimeLabel.setText(convertSecondsToHMmSs(totalTime));
        ChartUtil.setUp(amplChart, "БПФ", new Signal(SignalBundle.myMap.get("gz"), pair.getFirst()), 'm', pair.getFirst().size() / Signal.FREQUENCY);
        ChartUtil.setUp(phaseChart, "БПФ", new Signal(SignalBundle.myMap.get("gz"), pair.getFirst()), 'p', pair.getFirst().size() / Signal.FREQUENCY);
    }

    @FXML
    private void bpfn() {
        long startTime = System.currentTimeMillis();
        Pair<List<Complex>, String> pair = FourierTransformService.BPFn(signal.getData());
        long totalTime = System.currentTimeMillis() - startTime;
        descBpfnLabel.setText(pair.getSecond());
        bpfnTimeLabel.setText(convertSecondsToHMmSs(totalTime));
        ChartUtil.setUp(amplChart, "БПФn", new Signal(SignalBundle.myMap.get("gz"), pair.getFirst()), 'm', pair.getFirst().size() / Signal.FREQUENCY);
        ChartUtil.setUp(phaseChart, "БПФn", new Signal(SignalBundle.myMap.get("gz"), pair.getFirst()), 'p', pair.getFirst().size() / Signal.FREQUENCY);
    }


    private static String convertSecondsToHMmSs(long mills) {
        long second = (mills / 1000) % 60;
        long minute = (mills / (1000 * 60)) % 60;
        long hour = (mills / (1000 * 60 * 60)) % 24;

        return String.format("%02dч %02dм %02dс %04dмс", hour, minute, second, mills);
    }

    @FXML
    private void aprox() {
        ArrayList<Complex> dpf = FourierTransformService.DPF(signal.getData(), false);
        ArrayList<Complex> aprox5 = FourierTransformService.DPF(AproxService.cut(dpf, 5), true);
        ArrayList<Complex> aprox30 = FourierTransformService.DPF(AproxService.cut(dpf, 30), true);

        ChartUtil.setUp(amplChart, "5 гармоник", new Signal(SignalBundle.myMap.get("gzFull"), aprox5), 'r', aprox5.size() / Signal.FREQUENCY);
        ChartUtil.setUp(phaseChart, "30 гармоник", new Signal(SignalBundle.myMap.get("gzFull"), aprox30), 'r', aprox30.size() / Signal.FREQUENCY);

    }

    @FXML
    private void filter() {
        List<Complex> dpf = FourierTransformService.DPF(signal.getData(), false);
        String filterName = "";
        if (lowRadioBtn.isSelected()) {
            dpf = FilterService.low(dpf);
            filterName = "low";
        } else if (highRadioBen.isSelected()) {
            dpf = FilterService.high(dpf);
            filterName = "high";
        } else if (rejectRadioBtn.isSelected()) {
            dpf = FilterService.reject(dpf);
            filterName = "reject";
        } else if (stripeRadioBtn.isSelected()) {
            dpf = FilterService.stripe(dpf);
            filterName = "stripe";
        }

        ArrayList<Complex> filter = FourierTransformService.DPF(dpf, true);
        ChartUtil.setUp(amplChart, filterName, new Signal(SignalBundle.myMap.get("gzFull"), filter), 'r', Signal.FREQUENCY);
    }
}
