package br.com.chicobentojr.minhaeiro.utils;


/**
 * Created by Francisco on 08/02/2016.
 */
public class Extensoes {
    public static class STRING {
        public static String toBrazilianDate(String date){
            String[] values = date.split("/");
            return values[1] + "/" + values[0] + "/" + values[2];
        }
    }
}
