package br.com.chicobentojr.minhaeiro.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import br.com.chicobentojr.minhaeiro.R;
import br.com.chicobentojr.minhaeiro.adapters.MovimentacaoDetalhePagerAdapter;

public class MovimentacaoDetalheActivity extends AppCompatActivity {

    private MovimentacaoDetalhePagerAdapter pagerAdapter;

    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movimentacao_detalhe);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        pagerAdapter = new MovimentacaoDetalhePagerAdapter(getSupportFragmentManager(),this);

        viewPager = (ViewPager) findViewById(R.id.container);
        viewPager.setAdapter(pagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
    }
}
