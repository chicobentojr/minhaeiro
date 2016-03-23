package br.com.chicobentojr.minhaeiro.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;

import br.com.chicobentojr.minhaeiro.R;
import br.com.chicobentojr.minhaeiro.adapters.CategoriasAdapter;
import br.com.chicobentojr.minhaeiro.models.Categoria;
import br.com.chicobentojr.minhaeiro.utils.DividerItemDecoration;
import br.com.chicobentojr.minhaeiro.utils.ItemClickSupport;
import br.com.chicobentojr.minhaeiro.utils.P;

public class CategoriasActivity extends AppCompatActivity {

    private ArrayList<Categoria> categorias;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_categorias);
        this.getSupportActionBar().setHomeButtonEnabled(true);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        categorias = P.getUsuarioInstance().Categoria;

        adapter = new CategoriasAdapter(categorias);
        recyclerView.setAdapter(adapter);

        definirRecyclerViewItemClicks();
    }

    public void definirRecyclerViewItemClicks(){
        ItemClickSupport.addTo(recyclerView).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                Categoria categoria = categorias.get(position);
                Intent intent = new Intent(CategoriasActivity.this, CategoriasMovimentacoesActivity.class);
                intent.putExtra("categoria", categoria);
                startActivity(intent);
            }
        });
    }
}
