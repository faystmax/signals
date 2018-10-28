package lab3.controller;

import common.BaseController;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import lab1.model.SignalBundle;
import lab1.service.AproxService;
import lab1.util.LoaderUtil;
import lab3.model.SignalSimple;
import lab3.service.FilterService;
import lab3.service.UolshAdamarService;
import lab3.util.ChartUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Amosov Maxim - amosov.m@ext-system.com
 * @since 23.09.2018
 */
@Slf4j
public class Lab3Controller extends BaseController {

    private static final double FREQ = 360;

    @FXML private Label fileLabel;
    @FXML private TextField garmTextBox;
    @FXML private RadioButton uolshRadio;
    @FXML private RadioButton adamarRadio;
    @FXML private LineChart<Double, Double> mainChart;
    @FXML private LineChart<Double, Double> amplChart;
    @FXML private LineChart<Double, Double> phaseChart;
    @FXML private LineChart<Double, Double> inverseChart;

    private File selectedFile;
    private SignalSimple signal;

    @FXML
    private void openFileChooser() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Выберите Смгнал");
        fileChooser.setInitialDirectory(new File("./data/signal"));
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("txt", "*.txt"));
        selectedFile = fileChooser.showOpenDialog(stage);
        fileLabel.setText(selectedFile == null ? "..." : selectedFile.getName());

        loadSignal();
    }

    private void loadSignal() {
        if (selectedFile != null) {
            ArrayList<Double> signals = LoaderUtil.loadSignalsSimple(selectedFile);
            for (String name : SignalBundle.myMap.keySet()) {
                if (selectedFile.getName().contains(name)) {
                    signal = new SignalSimple(SignalBundle.myMap.get(name), signals);
                } else {
                    signal = new SignalSimple(SignalBundle.myMap.get("default"), signals);
                }
            }
            show(signal);
        }
    }

    private void show(SignalSimple signal) {
        clear();
        mainChart.getData().clear();
        ChartUtil.setUp(mainChart, "Оригинал", signal, FREQ);
    }


    @FXML
    private void clear() {
        amplChart.getData().clear();
        phaseChart.getData().clear();
        phaseChart.getData().clear();
        inverseChart.getData().clear();
    }

    @FXML
    private void show() {

        String name = uolshRadio.isSelected() ? "Уолш" : "Адамар";
        ArrayList<Double> res = uolshRadio.isSelected() ?
                UolshAdamarService.getUolshTransform(signal.getData(), false) :
                UolshAdamarService.getAdamarTransform(signal.getData(), false);


        List<Double> inverse;
        if (selectedFile.getName().contains("cardio")) {
//            inverse = FourierTransform.filter(res, FourierTransform.CARDIO);
            inverse = FilterService.reject(res, Integer.parseInt(garmTextBox.getText()));
        } else if (selectedFile.getName().contains("reo")) {
//            inverse = FourierTransform.filter(res, FourierTransform.REO);
            inverse = FilterService.low(res, Integer.parseInt(garmTextBox.getText()));
        } else if (selectedFile.getName().contains("velo")) {
//            inverse = FourierTransform.filter(res, FourierTransform.VELO);
            inverse = FilterService.high(res, Integer.parseInt(garmTextBox.getText()));
        } else {
//            inverse = UolshAdamarHelper.getAprox(res, Integer.parseInt(minTextBox.getText()), Integer.parseInt(maxTextBox.getText()));
            inverse = AproxService.cutD(res, Integer.parseInt(garmTextBox.getText()));
        }
        inverse = uolshRadio.isSelected() ?
                UolshAdamarService.getUolshTransform(inverse, true) :
                UolshAdamarService.getAdamarTransform(inverse, true);

        ChartUtil.setUp(amplChart, name, new SignalSimple(SignalBundle.myMap.get("gz"), UolshAdamarService.getAmplitude(res)), 1);
        ChartUtil.setUp(phaseChart, name, new SignalSimple(SignalBundle.myMap.get("gz"), UolshAdamarService.getPhase(res)), 1);
        ChartUtil.setUp(inverseChart, name + "_" + garmTextBox.getText(), new SignalSimple(SignalBundle.myMap.get("default"), inverse), FREQ);
    }

}
