package com.ttf.tallertornofumeroerp.utils;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class DecimalFormater {

    public static Double numberFormatter(Double number){
        DecimalFormat decimalFormat = new DecimalFormat("0.00", new DecimalFormatSymbols(Locale.US));
        return Double.valueOf(decimalFormat.format(number));
    }

    public static String numberFormatter(String number){
        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        return decimalFormat.format(number);
    }
}
