package br.com.chicobentojr.minhaeiro.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import java.util.ArrayList;

import br.com.chicobentojr.minhaeiro.R;
import br.com.chicobentojr.minhaeiro.adapters.MovimentacaoAdapter;
import br.com.chicobentojr.minhaeiro.models.Categoria;
import br.com.chicobentojr.minhaeiro.models.Movimentacao;
import br.com.chicobentojr.minhaeiro.utils.DividerItemDecoration;
import br.com.chicobentojr.minhaeiro.utils.Extensoes;
import br.com.chicobentojr.minhaeiro.utils.P;

public class CategoriasMovimentacoesActivity extends AppCompatActivity {

    private Categoria categoria;
    private ArrayList<Movimentacao> movimentacoes;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    private TextView lblSaldo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.iniciarLayout();
    }

    public void iniciarLayout() {
        setContentView(R.layout.activity_categorias_movimentacoes);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        categoria = (Categoria) getIntent().getSerializableExtra("categoria");
        movimentacoes = P.getUsuarioInstance().Movimentacao;

        lblSaldo = (TextView) findViewById(R.id.lblSaldo);

        if (categoria != null) {
            this.setTitle(categoria.nome);
            recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
            recyclerView.setHasFixedSize(true);
            recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
            layoutManager = new LinearLayoutManager(this);
            recyclerView.setLayoutManager(layoutManager);

            movimentacoes = Movimentacao.filtrarPorCategoria(movimentacoes, categoria);

            adapter = new MovimentacaoAdapter(movimentacoes);
            recyclerView.setAdapter(adapter);

            lblSaldo.setText("Saldo: " + Extensoes.LAYOUT.valor(Movimentacao.obterSaldo(movimentacoes)));
        }
    }
}
