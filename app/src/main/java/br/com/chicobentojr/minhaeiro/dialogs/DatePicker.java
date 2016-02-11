package br.com.chicobentojr.minhaeiro.dialogs;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;

import java.util.Calendar;

/**
 * Created by Francisco on 07/02/2016.
 */
public class DatePicker extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    public interface DataFornecidaListener{
        public void preencherData(Calendar calendario);
    }

    private DataFornecidaListener listener;

    public DatePicker(DataFornecidaListener listener){
        this.listener = listener;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Calendar calendario = Calendar.getInstance();

        int dia = calendario.get(Calendar.DAY_OF_MONTH);
        int mes = calendario.get(Calendar.MONTH);
        int ano = calendario.get(Calendar.YEAR);

        return new DatePickerDialog(getActivity(),this,ano,mes,dia);
    }

    @Override
    public void onDateSet(android.widget.DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        Calendar calendario = Calendar.getInstance();

        calendario.set(Calendar.DAY_OF_MONTH,dayOfMonth);
        calendario.set(Calendar.MONTH, monthOfYear);
        calendario.set(Calendar.YEAR, year);

        listener.preencherData(calendario);
    }


}
