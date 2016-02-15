package br.com.chicobentojr.minhaeiro.adapters;


import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import br.com.chicobentojr.minhaeiro.fragments.MovimentacaoDetalheFragment;
import br.com.chicobentojr.minhaeiro.fragments.MovimentacaoItensFragment;

/**
 * Created by Francisco on 14/02/2016.
 */
public class MovimentacaoDetalhePagerAdapter extends FragmentPagerAdapter {
    private Activity listener;
    public MovimentacaoDetalhePagerAdapter(FragmentManager fm,Activity listener) {
        super(fm);
        this.listener = listener;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return MovimentacaoDetalheFragment.newInstance(listener);
            case 1:
                return MovimentacaoItensFragment.newInstance(listener);
        }
        return null;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "Detalhes";
            case 1:
                return "Itens";
        }
        return null;
    }
}
