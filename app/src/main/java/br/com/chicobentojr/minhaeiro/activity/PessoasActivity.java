package br.com.chicobentojr.minhaeiro.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toolbar;

import java.util.ArrayList;

import br.com.chicobentojr.minhaeiro.R;
import br.com.chicobentojr.minhaeiro.models.Pessoa;
import br.com.chicobentojr.minhaeiro.utils.P;

public class PessoasActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private ArrayList<Pessoa> pessoas;
    private ArrayAdapter<Pessoa> adapter;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_pessoas);
        this.getSupportActionBar().setHomeButtonEnabled(true);

        listView = (ListView) findViewById(R.id.listView);
        pessoas = P.getUsuario(P.obter(P.USUARIO_JSON)).Pessoa;

        adapter = new ArrayAdapter<Pessoa>(this, android.R.layout.simple_list_item_1, pessoas);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Pessoa pessoa = pessoas.get(position);

        Intent intent = new Intent(this,PessoasMovimentacoesActivity.class);

        intent.putExtra("pessoa",pessoa);

        startActivity(intent);

    }
}
