package br.com.chicobentojr.minhaeiro.utils;


import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * Created by Francisco on 08/02/2016.
 */
public class Extensoes {
    public static class STRING {
        public static String toBrazilianDate(String date) {
            String[] values = date.split("/");
            return values[1] + "/" + values[0] + "/" + values[2];
        }

        public static String obterDataCompleta(String data) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                SimpleDateFormat sdf2 = new SimpleDateFormat("dd/MM/yyyy");

                if (data.length() == "dd/MM/yyyy".length()) {
                    return sdf.format(sdf2.parse(data));
                } else {
                    return sdf2.format(sdf.parse(data));
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return data;
        }
    }

    public static class LAYOUT {
        public static String valor(double valor) {
            boolean negativo = valor < 0;
            valor = !negativo ? valor : valor * -1;

            String strValor = String.valueOf(valor);
            String[] array = strValor.split("\\.");

            String decimal = array[1].length() == 2 ? array[1] : array[1] + "0";
            String tempInteiros = array[0];
            String inteiros = "";

            int indice = 0;
            for (int i = tempInteiros.length() - 1; i >= 0; i--) {
                if (indice == 3) {
                    inteiros = tempInteiros.charAt(i) + "." + inteiros;
                    indice = 0;
                } else {
                    inteiros = tempInteiros.charAt(i) + inteiros;
                }
                indice++;
            }

            return (negativo ? "- " : "") + "R$ " + inteiros + "," + decimal;
        }

        public static String data(String data) {
            if (data.length() == "dd/MM/yyyy".length()) {
                return data;
            } else {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                SimpleDateFormat sdf2 = new SimpleDateFormat("dd/MM/yyyy");
                try {

                    return sdf2.format(sdf.parse(data));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            return "";
        }
    }
}
