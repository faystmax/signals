package controller;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.chart.LineChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import lombok.Setter;
import model.Signal;
import model.SignalBundle;
import org.apache.commons.math3.complex.Complex;
import service.FourierTransformService;
import util.ChartUtil;
import util.LoaderUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Amosov Maxim - amosov.m@ext-system.com
 * @since 23.09.2018
 */
public class MainController {

    @FXML private LineChart mainChart;
    @FXML private LineChart amplChart;
    @FXML private LineChart phaseChart;
    @FXML private Button dpfButton;
    @FXML private Button bpfButton;
    @FXML private Button clearBtn;
    @FXML private Label fileLabel;
    @FXML private Label dpfTimeLabel;
    @FXML private Label bpfTimeLabel;
    @FXML private Label bpfnTimeLabel;

    @Setter private Stage stage;
    private File selectedFile;
    private Signal signal;

    @FXML
    protected void initialize() {
        fileLabel.setAlignment(Pos.CENTER_RIGHT);
        dpfTimeLabel.setAlignment(Pos.CENTER_RIGHT);
        bpfTimeLabel.setAlignment(Pos.CENTER_RIGHT);
        clear();
    }

    @FXML
    private void openFileChooser() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Выберите файл с сигналом");
        fileChooser.setInitialDirectory(new File("./signals"));
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
        List<Complex> result = FourierTransformService.BFT(signal.getData());
        long totalTime = System.currentTimeMillis() - startTime;
        bpfTimeLabel.setText(convertSecondsToHMmSs(totalTime));
        ChartUtil.setUp(amplChart, "БПФ", new Signal(SignalBundle.myMap.get("gz"), result), 'm', result.size() / Signal.FREQUENCY);
        ChartUtil.setUp(phaseChart, "БПФ", new Signal(SignalBundle.myMap.get("gz"), result), 'p', result.size() / Signal.FREQUENCY);
    }

    @FXML
    private void bpfn() {
        long startTime = System.currentTimeMillis();
        List<Complex> result = FourierTransformService.BPFn(signal.getData());
        long totalTime = System.currentTimeMillis() - startTime;
        bpfnTimeLabel.setText(convertSecondsToHMmSs(totalTime));
        ChartUtil.setUp(amplChart, "БПФn", new Signal(SignalBundle.myMap.get("gz"), result), 'm', result.size() / Signal.FREQUENCY);
        ChartUtil.setUp(phaseChart, "БПФn", new Signal(SignalBundle.myMap.get("gz"), result), 'p', result.size() / Signal.FREQUENCY);
    }


    public static String convertSecondsToHMmSs(long mills) {
        long second = (mills / 1000) % 60;
        long minute = (mills / (1000 * 60)) % 60;
        long hour = (mills / (1000 * 60 * 60)) % 24;

        return String.format("%02dч %02dм %02dс %04dмс", hour, minute, second, mills);
    }

}
