package lab4.controller;

import common.BaseController;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import lab1.model.SignalBundle;
import lab1.service.FilterService;
import lab1.util.LoaderUtil;
import lab2.wav.WavUtil;
import lab3.model.SignalSimple;
import lab3.util.ChartUtil;
import lab4.service.WaveletService;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Amosov Maxim - amosov.m@ext-system.com
 * @since 05.11.2018
 */
@Slf4j
public class Lab4Controller extends BaseController {

    private static final double FREQ = 360;


    @FXML private Label fileLabel;
    @FXML private TextField maxTextBox;
    @FXML private TextField minTextBox;
    @FXML private TextField scaleTextBox;
    @FXML private RadioButton harraRadio;
    @FXML private RadioButton dobeshRadio;
    @FXML private CheckBox filterCheckbox;
    @FXML private CheckBox lowCheckbox;
    @FXML private CheckBox highCheckbox;
    @FXML private LineChart<Double, Double> mainChart;
    @FXML private LineChart<Double, Double> transformChart;
    @FXML private LineChart<Double, Double> resultChart;

    private File selectedFile;
    private SignalSimple signal;

    @FXML
    private void openFileChooser() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Выберите Смгнал");
        fileChooser.setInitialDirectory(new File("./data/signal"));
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("txt,wav", "*.txt", "*.wav"));
        selectedFile = fileChooser.showOpenDialog(stage);
        fileLabel.setText(selectedFile == null ? "..." : selectedFile.getName());

        loadSignal();
    }

    private void loadSignal() {
        if (selectedFile != null) {


            ArrayList<Double> signals = LoaderUtil.loadSignalsSimple(selectedFile);
            for (String name : SignalBundle.myMap.keySet()) {
                if (selectedFile.getName().contains("wav")) {
                    signal = WavUtil.loadD(selectedFile);
                } else if (selectedFile.getName().contains(name)) {
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
        ChartUtil.setUp(mainChart, "Оригинал", signal, FREQ, selectedFile.getName().contains("wav") ? 100 : 0);
    }


    @FXML
    private void clear() {
        transformChart.getData().clear();
        resultChart.getData().clear();
    }

    @FXML
    private void show() {

        String name = harraRadio.isSelected() ? "Хаара" : "Добеши d4";
        int scale = Integer.parseInt(scaleTextBox.getText());
        List<Double> transform = harraRadio.isSelected() ?
                WaveletService.getHaarTransform(signal.getData(), scale) :
                WaveletService.getDaubechiesTransform(signal.getData(), scale);


        List<Double> result = new ArrayList<>(transform);
        if (filterCheckbox.isSelected()) {
            result = WaveletService.filterMinMax(result, Integer.parseInt(minTextBox.getText()), Integer.parseInt(maxTextBox.getText()));
        }
        if (lowCheckbox.isSelected()) {
            FilterService.lowD(result, selectedFile.getName().contains("wav") ? 44100 : 360, Integer.parseInt(minTextBox.getText()));
        }

        if (highCheckbox.isSelected()) {
            FilterService.highD(result, selectedFile.getName().contains("wav") ? 44100 : 360, Integer.parseInt(minTextBox.getText()));
        }

        result = harraRadio.isSelected() ?
                WaveletService.getHaarInverseTransform(result, scale) :
                WaveletService.getDaubechiesInverseTransform(result, scale);

        ChartUtil.setUp(transformChart, name, new SignalSimple(SignalBundle.myMap.get("gz"), transform), FREQ, selectedFile.getName().contains("wav") ? 100 : 0);
        ChartUtil.setUp(resultChart, name, new SignalSimple(SignalBundle.myMap.get("default"), result), FREQ, selectedFile.getName().contains("wav") ? 100 : 0);
    }

}
