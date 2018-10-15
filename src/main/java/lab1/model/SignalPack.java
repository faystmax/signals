package lab1.model;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author Amosov Maxim - amosov.m@ext-system.com
 * @since 23.09.2018
 */
@Data
@AllArgsConstructor
public class SignalPack {

    private String nameX;
    private String nameY;
    private double width = 1;

    public SignalPack(String nameX, String nameY) {
        this.nameX = nameX;
        this.nameY = nameY;
    }
}
