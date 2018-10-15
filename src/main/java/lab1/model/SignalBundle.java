package lab1.model;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Amosov Maxim - amosov.m@ext-system.com
 * @since 23.09.2018
 */
public class SignalBundle {

    public static final Map<String, SignalPack> myMap;

    static {
        Map<String, SignalPack> aMap = new HashMap<>();
        aMap.put("cardio", new SignalPack("сек", "мВ"));
        aMap.put("reo", new SignalPack("сек", "мОм"));
        aMap.put("velo", new SignalPack("сек", "мВ"));
        aMap.put("spiro", new SignalPack("сек", "л"));
        aMap.put("default", new SignalPack("сек", ""));
        aMap.put("gz", new SignalPack("Гц", "", 0.5));
        aMap.put("gz02", new SignalPack("Гц", "", 0.2));
        aMap.put("gz005", new SignalPack("Гц", "", 0.05));
        aMap.put("gz004", new SignalPack("Гц", "", 0.04));
        aMap.put("gz003", new SignalPack("Гц", "", 0.03));
        aMap.put("gz001", new SignalPack("Гц", "", 0.01));
        aMap.put("gz01", new SignalPack("Гц", "", 0.1));
        aMap.put("gzFull", new SignalPack("ceк", "", 1));
        myMap = Collections.unmodifiableMap(aMap);
    }
}
