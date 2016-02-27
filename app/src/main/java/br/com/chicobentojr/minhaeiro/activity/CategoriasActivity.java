package br.com.chicobentojr.minhaeiro.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

import br.com.chicobentojr.minhaeiro.R;
import br.com.chicobentojr.minhaeiro.models.Categoria;
import br.com.chicobentojr.minhaeiro.utils.P;

public class CategoriasActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private ArrayList<Categoria> categorias;
    private ArrayAdapter<Categoria> adapter;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_pessoas);
        this.getSupportActionBar().setHomeButtonEnabled(true);

        listView = (ListView) findViewById(R.id.recyclerView);
        categorias = P.getUsuario(P.obter(P.USUARIO_JSON)).Categoria;

        adapter = new ArrayAdapter<Categoria>(this, android.R.layout.simple_list_item_1, categorias);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //Pessoa pessoa = pessoas.get(position);
        Categoria categoria = categorias.get(position);

        Intent intent = new Intent(this,PessoasMovimentacoesActivity.class);

        intent.putExtra("categoria", categoria);

        //startActivity(intent);

    }
}
