package lab1.model;

import lombok.Data;
import org.apache.commons.math3.complex.Complex;

import java.util.List;

/**
 * @author Amosov Maxim - amosov.m@ext-system.com
 * @since 23.09.2018
 */
@Data
public class Signal {

    public static final double FREQUENCY = 360.0;

    private SignalPack signalPack;
    private List<Complex> data;

    public Signal(SignalPack signalPack, List<Complex> data) {
        this.signalPack = signalPack;
        this.data = data;
    }
}
