package br.com.chicobentojr.minhaeiro.utils;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

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
            Calendar calendar = Calendar.getInstance();
            Calendar agora = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            SimpleDateFormat sdf2 = new SimpleDateFormat("dd/MM/yyyy");

            try {
                if (data.length() == "dd/MM/yyyy".length()) {
                    calendar.setTime(sdf2.parse(data));
                } else {
                    calendar.setTime(sdf.parse(data));
                }

                int ano = calendar.get(Calendar.YEAR);
                int mes = calendar.get(Calendar.MONTH) + 1;
                int dia = calendar.get(Calendar.DAY_OF_MONTH);

                int anoAgora = agora.get(Calendar.YEAR);
                int mesAgora = agora.get(Calendar.MONTH) + 1;
                int diaAgora = agora.get(Calendar.DAY_OF_MONTH);

                if (ano == anoAgora && mes == mesAgora) {
                    if (dia == diaAgora)
                        return "Hoje";
                    else if (dia == diaAgora - 1)
                        return "Ontem";
                    else if (dia >= diaAgora) {
                        return  "Daqui a " + (dia - diaAgora) + " dias";
                    }
                    else if (dia >= diaAgora - 7)
                        return diaAgora - dia + " dias atrás";
                    else if (dia >= diaAgora - 7 * 4)
                        return "Semanas atrás";
                } else if (ano == anoAgora && mes == mesAgora - 1) {
                    return "Mês passado";
                } else {
                    return sdf2.format(calendar.getTime());
                }

            } catch (ParseException e) {
                e.printStackTrace();
            }
            return "";
        }
    }
}
