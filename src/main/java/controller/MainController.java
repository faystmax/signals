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
    @FXML private Label fileLabel;

    @Setter private Stage stage;
    private File selectedFile;
    private Signal signal;

    @FXML
    protected void initialize() {
        fileLabel.setAlignment(Pos.CENTER_RIGHT);
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
        ChartUtil.setUp(mainChart, "Оригинал", signal, 'r', Signal.FREQUENCY);
    }

    private void clear() {
        mainChart.getData().clear();
        amplChart.getData().clear();
        phaseChart.getData().clear();
    }

    @FXML
    private void dpf() {
        ArrayList<Complex> dpf = FourierTransformService.DPF(signal.getData());
        ChartUtil.setUp(amplChart, "Амплитуда", new Signal(SignalBundle.myMap.get("gz"), dpf), 'm', dpf.size() / Signal.FREQUENCY);
        ChartUtil.setUp(phaseChart, "Фаза", new Signal(SignalBundle.myMap.get("gz"), dpf), 'p', dpf.size() / Signal.FREQUENCY);
    }

    @FXML
    private void bpf() {
//        ArrayList<Complex> dpf = FourierTransformService.DPF(signal.getData());
//        ChartUtil.setUp(amplChart, "Амплитуда", new Signal(SignalBundle.myMap.get("gz"), dpf), 'm', dpf.size() / Signal.FREQUENCY);
//        ChartUtil.setUp(phaseChart, "Фаза", new Signal(SignalBundle.myMap.get("gz"), dpf), 'p', dpf.size() / Signal.FREQUENCY);
    }

}
