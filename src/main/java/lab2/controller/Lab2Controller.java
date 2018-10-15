package lab2.controller;

import common.BaseController;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.FileChooser;
import lab1.model.Signal;
import lab1.model.SignalBundle;
import lab1.service.FilterService;
import lab1.service.FourierTransformService;
import lab1.util.ChartUtil;
import lab2.filter.Filter;
import lab2.wav.WavUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.math3.complex.Complex;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Amosov Maxim - amosov.m@ext-system.com
 * @since 23.09.2018
 */
@Slf4j
public class Lab2Controller extends BaseController {

    public static int FREQ = 44100;
    private static int SKIP = 200;

    @FXML private Label fileLabel;
    @FXML private TextField highTextBox;
    @FXML private TextField lowTextBox;
    @FXML private TextField fTextbox;
    @FXML private TextField nTextbox;

    @FXML private RadioButton rectRadioBtn;
    @FXML private RadioButton hammingRadioBtn;
    @FXML private RadioButton hanningRadioBtn;
    @FXML private RadioButton bartletRadioBtn;
    @FXML private RadioButton blackmanRadioBtn;

    @FXML private LineChart<Double, Double> mainChart;
    @FXML private LineChart<Double, Double> amplChart;
    @FXML private LineChart<Double, Double> phaseChart;
    @FXML private LineChart<Double, Double> lowChart;
    @FXML private LineChart<Double, Double> highChart;
    @FXML private LineChart<Double, Double> chart1;
    @FXML private LineChart<Double, Double> chart2;
    @FXML private LineChart<Double, Double> chart3;
    @FXML private LineChart<Double, Double> chart4;

    private File selectedFile;
    private Signal signal;
    private MediaPlayer mediaPlayerHigh;
    private MediaPlayer mediaPlayerLow;

    @FXML
    private void openFileChooser() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Выберите wav File");
        fileChooser.setInitialDirectory(new File("./data/music"));
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("wav", "*.wav"));
        selectedFile = fileChooser.showOpenDialog(stage);
        fileLabel.setText(selectedFile == null ? "..." : selectedFile.getName());

        signal = WavUtil.load(selectedFile);
        show(signal);
    }

    private void show(Signal signal) {
        clear();
        mainChart.getData().clear();
        ChartUtil.setUp(mainChart, "Оригинал", signal, 'r', FREQ, 100);
    }

    @FXML
    private void clear() {
        amplChart.getData().clear();
        phaseChart.getData().clear();
        phaseChart.getData().clear();
        lowChart.getData().clear();
        highChart.getData().clear();
        chart1.getData().clear();
        chart2.getData().clear();
        chart3.getData().clear();
        chart4.getData().clear();
    }

    @FXML
    private void bpf() {
        List<Complex> bpf = FourierTransformService.BPF(signal.getData(), false).getFirst();
        ChartUtil.setUp(amplChart, "БПФ", new Signal(SignalBundle.myMap.get("gz"), bpf), 'm', bpf.size() / FREQ, SKIP);
        ChartUtil.setUp(phaseChart, "БПФ", new Signal(SignalBundle.myMap.get("gz"), bpf), 'p', bpf.size() / FREQ, SKIP);
    }

    @FXML
    private void high() {
        List<Complex> data = FourierTransformService.BPF(signal.getData(), false).getFirst();
        FilterService.high(data, FREQ, Integer.parseInt(highTextBox.getText()));
        List<Complex> res = FourierTransformService.BPF(data, true).getFirst();
        ChartUtil.setUp(highChart, highTextBox.getText(), new Signal(SignalBundle.myMap.get("gzFull"), res), 'r', FREQ, SKIP);
        WavUtil.save(res, "high_" + selectedFile.getName());
        playHigh();
    }

    @FXML
    private void low() {
        List<Complex> data = FourierTransformService.BPF(signal.getData(), false).getFirst();
        FilterService.low(data, FREQ, Integer.parseInt(lowTextBox.getText()));
        List<Complex> res = FourierTransformService.BPF(data, true).getFirst();
        ChartUtil.setUp(lowChart, lowTextBox.getText(), new Signal(SignalBundle.myMap.get("gzFull"), res), 'r', FREQ, SKIP);
        WavUtil.save(res, "low_" + selectedFile.getName());
        playLow();
    }

    @FXML
    private void playHigh() {
        Media hit = new Media(new File("data/music/result/" + "high_" + selectedFile.getName()).toURI().toString());
        stopHigh();
        mediaPlayerHigh = new MediaPlayer(hit);
        mediaPlayerHigh.play();
    }

    @FXML
    private void stopHigh() {
        if (mediaPlayerHigh != null) {
            mediaPlayerHigh.stop();
        }
    }

    @FXML
    private void playLow() {
        Media hit = new Media(new File("data/music/result/" + "low_" + selectedFile.getName()).toURI().toString());
        stopLow();
        mediaPlayerLow = new MediaPlayer(hit);
        mediaPlayerLow.play();
    }

    @FXML
    private void stopLow() {
        if (mediaPlayerLow != null) {
            mediaPlayerLow.stop();
        }
    }

    @FXML
    private void show() {
        Filter filter = new Filter(FREQ, Integer.parseInt(nTextbox.getText()), Integer.parseInt(fTextbox.getText()), signal.getData());
        ArrayList<Complex> fSignal = filter.getY(getType(), false);
        ChartUtil.setUp(chart1, getType(), new Signal(SignalBundle.myMap.get("default"), fSignal), 'r', FREQ, SKIP);
    }

    public void afc() {
        ArrayList<Complex> idealSignal = getIdealSignal(20000);

        Filter filter = new Filter(FREQ, Integer.parseInt(nTextbox.getText()), Integer.parseInt(fTextbox.getText()), idealSignal);
        ArrayList<Complex> fSignal = filter.getY(getType(), false);
        ArrayList<Complex> ampl = filter.getAmplitude();
        ArrayList<Complex> lAmpl = filter.getLogAmplitude();

        ChartUtil.setUp(chart2, getType(), new Signal(SignalBundle.myMap.get("gz004"), fSignal), 'r', 1);
        ChartUtil.setUp(chart3, getType(), new Signal(SignalBundle.myMap.get("gz01"), ampl), 'r', 1);
        ChartUtil.setUp(chart4, getType(), new Signal(SignalBundle.myMap.get("gz01"), lAmpl), 'r', 1);
    }

    private String getType() {
        if (rectRadioBtn.isSelected()) {
            return "Rectangular";
        } else if (hammingRadioBtn.isSelected()) {
            return "Hamming";
        } else if (hanningRadioBtn.isSelected()) {
            return "Hanning";
        } else if (bartletRadioBtn.isSelected()) {
            return "Bartlett";
        } else if (blackmanRadioBtn.isSelected()) {
            return "Blackman";
        }
        return "";
    }

    private static ArrayList<Complex> getIdealSignal(int N) {
        ArrayList<Complex> signals = new ArrayList<>();
        signals.add(new Complex(1000));
        for (int i = 1; i < N; i++) {
            signals.add(new Complex(0));
        }
        return signals;
    }
}
