package lab3.model;

import lab1.model.SignalPack;
import lombok.Data;

import java.util.List;

/**
 * @author Amosov Maxim - amosov.m@ext-system.com
 * @since 21.10.2018
 */
@Data
public class SignalSimple {

    public static final double FREQUENCY = 360.0;

    private SignalPack signalPack;
    private List<Double> data;

    public SignalSimple(SignalPack signalPack, List<Double> data) {
        this.signalPack = signalPack;
        this.data = data;
    }
}
