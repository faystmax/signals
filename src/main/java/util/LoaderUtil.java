package util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.math3.complex.Complex;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;

/**
 * @author Amosov Maxim - amosov.m@ext-system.com
 * @since 23.09.2018
 */
@Slf4j
public class LoaderUtil {

    public static ArrayList<Complex> loadSignals(File file) {
        ArrayList<Complex> signals = new ArrayList<>();
        try (BufferedReader fin = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = fin.readLine()) != null) {
                String[] split = StringUtils.split(line, " ");
                for (String s : split) {
                    signals.add(new Complex(Double.parseDouble(s)));
                }
            }
        } catch (Throwable th) {
            log.error("Не удалось загрузить сигналы из файла " + file.getName(), th);
        }
        return signals;
    }
}
