package br.com.chicobentojr.minhaeiro.adapters;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import br.com.chicobentojr.minhaeiro.fragments.PeriodoFragment;

/**
 * Created by Francisco on 23/03/2016.
 */
public class PeriodoPagerAdapter extends FragmentPagerAdapter {

    private Activity listener;
    private ArrayList<Calendar> periodos;

    public PeriodoPagerAdapter(FragmentManager fm, ArrayList<Calendar> periodos, Activity listener) {
        super(fm);
        this.periodos = periodos;
        this.listener = listener;
    }

    @Override
    public Fragment getItem(int position) {
        return PeriodoFragment.newInstance(listener, periodos.get(position));
    }

    @Override
    public int getCount() {
        return periodos.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        Calendar periodo = periodos.get(position);
        return periodo.getDisplayName(Calendar.MONTH, Calendar.SHORT, new Locale("pt", "BR")) + "/" + periodo.get(Calendar.YEAR);
    }
}

