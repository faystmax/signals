package model;

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
        myMap = Collections.unmodifiableMap(aMap);
    }
}
