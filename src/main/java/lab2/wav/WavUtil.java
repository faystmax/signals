package lab2.wav;

import lab1.model.Signal;
import lab1.model.SignalBundle;
import lab2.controller.Lab2Controller;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.math3.complex.Complex;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Amosov Maxim - amosov.m@ext-system.com
 * @since 13.10.2018
 */
@Slf4j
public class WavUtil {

    public static Signal load(File selectedFile) {
        try {
            WavFile wavFile = WavFile.openWavFile(selectedFile);
            wavFile.display();

            ArrayList<Complex> signals = new ArrayList<>(1000);
            double[] buffer = new double[1000];
            int framesRead;

            do {
                framesRead = wavFile.readFrames(buffer, 1000);
                for (int i = 0; i < framesRead; i++) {
                    signals.add(new Complex(buffer[i]));
                }
            }
            while (framesRead != 0);
            return new Signal(SignalBundle.myMap.get("gzFull"), signals);
        } catch (Exception e) {
            log.error("Error while loading wav File!", e);
        }
        return null;
    }


    public static void save(List<Complex> data, String filename) {
        try {
            File file = new File("./data/music/result/" + filename);
            file.getParentFile().mkdirs();
            file.createNewFile();
            WavFile wavFile = WavFile.newWavFile(file, 2, data.size(), 16, Lab2Controller.FREQ);
            double[][] buffer = new double[2][1000];
            int frameCounter = 0;
            while (frameCounter < data.size()) {
                long remaining = wavFile.getFramesRemaining();
                int toWrite = (remaining > 1000) ? 1000 : (int) remaining;

                // Fill the buffer, one tone per channel
                for (int s = 0; s < toWrite; s++, frameCounter++) {
//                    buffer[s] = data.get(frameCounter).getReal();
                    buffer[0][s] = data.get(frameCounter).getReal();
                    buffer[1][s] = data.get(frameCounter).getReal();
                }
                wavFile.writeFrames(buffer, toWrite);
            }

            wavFile.close();
        } catch (Exception e) {
            log.error("Error while saving file " + filename, e);
        }
    }
}
