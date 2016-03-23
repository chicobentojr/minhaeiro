package br.com.chicobentojr.minhaeiro.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;

import br.com.chicobentojr.minhaeiro.R;
import br.com.chicobentojr.minhaeiro.adapters.CategoriasAdapter;
import br.com.chicobentojr.minhaeiro.adapters.PendentesAdapter;
import br.com.chicobentojr.minhaeiro.models.Categoria;
import br.com.chicobentojr.minhaeiro.models.Pessoa;
import br.com.chicobentojr.minhaeiro.utils.DividerItemDecoration;
import br.com.chicobentojr.minhaeiro.utils.ItemClickSupport;
import br.com.chicobentojr.minhaeiro.utils.P;

public class PendentesActivity extends AppCompatActivity {

    private ArrayList<Pessoa> pessoas;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_pendentes);
        this.getSupportActionBar().setHomeButtonEnabled(true);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        pessoas = Pessoa.filtrarPendentes(P.getUsuarioInstance().Movimentacao);

        adapter = new PendentesAdapter(pessoas);
        recyclerView.setAdapter(adapter);

        definirRecyclerViewItemClicks();
    }

    public void definirRecyclerViewItemClicks(){
        ItemClickSupport.addTo(recyclerView).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                Pessoa pessoa = pessoas.get(position);
                Intent intent = new Intent(PendentesActivity.this, PendentesMovimentacoesActivity.class);
                intent.putExtra("pessoa", pessoa);
                startActivity(intent);
            }
        });
    }
}
