package br.com.chicobentojr.minhaeiro.utils;

import android.widget.Spinner;

/**
 * Created by Francisco on 14/02/2016.
 */
public class SpinnerHelper {
    public static <T> int getSelectedItemPosition(Spinner spinner, T object) {
        int retorno = -1;
        for (int i = 0; i < spinner.getCount(); i++) {
            T temp = (T) spinner.getItemAtPosition(i);
            if (temp.equals(object)) {
                retorno = i;
            }
        }
        return retorno;
    }
}
