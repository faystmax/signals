package lab1.util;

import lombok.extern.slf4j.Slf4j;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author Amosov Maxim - amosov.m@ext-system.com
 * @since 23.09.2018
 */
@Slf4j
public class SignalsGeneratorUtil {

    private static final String SAW = "data/signal/Пила.txt";
    private static final String ANGLE = "data/signal/Треугольный.txt";
    private static final String RECTANGLE = "data/signal/Прямоугольный.txt";

    private static final double maxY = 255;
    private static final double maxX = 2;
    private static final double period = 2;
    private static final double freq = 360;
    private static final double half = maxX / period;

    public static void generate() {
        try {
            log.info("Гененируем тестовые сигналы!");
            generateSaw();
            generateAngle();
            generateRectangle();
        } catch (Exception ex) {
            log.error("Ошибка генерации тестовых сигналов!");
        }
    }

    private static void generateSaw() throws IOException {
        PrintWriter pw = new PrintWriter(new FileWriter(SAW));
        for (int i = 0; i < maxX / period; i++) {
            double step = maxY / (freq / half);
            double value = 0.;
            for (int j = 0; j < freq / half; j++) {
                pw.write(value + "");
                pw.println();
                value += step;
            }
            value = value * -1;
            for (int j = 0; j < freq / half; j++) {
                pw.write(value + "");
                pw.println();
                value += step;
            }
        }
        pw.close();
    }

    private static void generateAngle() throws IOException {
        PrintWriter pw = new PrintWriter(new FileWriter(ANGLE));
        for (int i = 0; i < maxX / period; i++) {
            double step = maxY / (freq / half);
            double value = 0.;
            for (int j = 0; j < freq / half; j++) {
                pw.write(value + "");
                pw.println();
                value += step;
            }
            for (int j = 0; j < freq / half; j++) {
                pw.write(value + "");
                pw.println();
                value -= step;
            }
        }
        pw.close();
    }

    private static void generateRectangle() throws IOException {
        PrintWriter pw = new PrintWriter(new FileWriter(RECTANGLE));
        for (int i = 0; i < maxX / period; i++) {
            for (int j = 0; j < freq / half; j++) {
                pw.write(maxY + "");
                pw.println();
            }
            for (int j = 0; j < freq / half; j++) {
                pw.write(0 + "");
                pw.println();
            }
        }
        pw.close();
    }
}
