package br.com.chicobentojr.minhaeiro.activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import br.com.chicobentojr.minhaeiro.R;
import br.com.chicobentojr.minhaeiro.models.Movimentacao;

public class MovimentacaoDetalheActivity extends AppCompatActivity {

    Movimentacao movimentacao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movimentacao_detalhe);

        movimentacao = (Movimentacao) getIntent().getSerializableExtra("movimentacao");

        this.setTitle(movimentacao.descricao);

        this.iniciarLayout();
    }

    public void iniciarLayout() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new BtnFloatingOnClick());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public class BtnFloatingOnClick implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            Snackbar.make(v, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        }
    }
}
