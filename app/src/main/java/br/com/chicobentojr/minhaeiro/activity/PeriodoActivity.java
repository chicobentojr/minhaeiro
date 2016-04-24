package br.com.chicobentojr.minhaeiro.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import java.util.ArrayList;
import java.util.Calendar;

import br.com.chicobentojr.minhaeiro.R;
import br.com.chicobentojr.minhaeiro.adapters.PeriodoPagerAdapter;
import br.com.chicobentojr.minhaeiro.models.Movimentacao;
import br.com.chicobentojr.minhaeiro.utils.P;

public class PeriodoActivity extends AppCompatActivity {

    private PeriodoPagerAdapter pagerAdapter;
    private ViewPager viewPager;
    private TabLayout tabLayout;

    private ArrayList<Calendar> periodos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_periodo);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        viewPager = (ViewPager) findViewById(R.id.container);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
    }

    @Override
    protected void onResume() {
        super.onResume();
        carregarPeriodos();
        abrirPeriodoMaisRecente();
    }

    public void carregarPeriodos() {
        periodos = Movimentacao.obterPeriodos(P.getUsuarioInstance().Movimentacao);
        pagerAdapter = new PeriodoPagerAdapter(getSupportFragmentManager(), periodos, this);
        viewPager.setAdapter(pagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    public void abrirPeriodoMaisRecente() {
        viewPager.setCurrentItem(periodos.size() - 1);
    }
}
