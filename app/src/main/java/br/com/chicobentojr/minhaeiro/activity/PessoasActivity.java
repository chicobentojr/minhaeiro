package br.com.chicobentojr.minhaeiro.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;

import java.util.ArrayList;

import br.com.chicobentojr.minhaeiro.R;
import br.com.chicobentojr.minhaeiro.adapters.PessoasAdapter;
import br.com.chicobentojr.minhaeiro.models.Pessoa;
import br.com.chicobentojr.minhaeiro.utils.ApiRoutes;
import br.com.chicobentojr.minhaeiro.utils.AppController;
import br.com.chicobentojr.minhaeiro.utils.DividerItemDecoration;
import br.com.chicobentojr.minhaeiro.utils.ItemClickSupport;
import br.com.chicobentojr.minhaeiro.utils.MinhaeiroErrorHelper;
import br.com.chicobentojr.minhaeiro.utils.P;

public class PessoasActivity extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {

    private ArrayList<Pessoa> pessoas;
    private Pessoa pessoaSelecionada;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    private ProgressDialog progressDialog;

    @Override
    protected void onResume() {
        super.onResume();
        this.iniciarLayout();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.iniciarLayout();
    }

    public void iniciarLayout() {
        this.setContentView(R.layout.activity_pessoas);
        this.getSupportActionBar().setHomeButtonEnabled(true);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        progressDialog = new ProgressDialog(this);
        progressDialog.setCanceledOnTouchOutside(false);

        pessoas = P.getUsuarioInstance().Pessoa;

        adapter = new PessoasAdapter(pessoas);
        recyclerView.setAdapter(adapter);

        definirRecyclerViewItemClicks();
    }

    public void definirRecyclerViewItemClicks() {
        ItemClickSupport.addTo(recyclerView).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                Pessoa pessoa = pessoas.get(position);
                Intent intent = new Intent(PessoasActivity.this, PessoasMovimentacoesActivity.class);
                intent.putExtra("pessoa", pessoa);
                startActivity(intent);
            }
        });
        ItemClickSupport.addTo(recyclerView).setOnItemLongClickListener(new ItemClickSupport.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClicked(RecyclerView recyclerView, int position, View v) {
                pessoaSelecionada = pessoas.get(position);
                PopupMenu popupMenu = new PopupMenu(PessoasActivity.this, v, Gravity.LEFT);
                popupMenu.setOnMenuItemClickListener(PessoasActivity.this);
                popupMenu.inflate(R.menu.popup_gerenciar);
                popupMenu.show();
                return true;
            }
        });
    }

    public void abrirPessoaDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Excluir " + pessoaSelecionada.nome + "?")
                .setMessage("Tem certeza que deseja prosseguir?\n\nTodas as movimentações desta pessoa serão excluídas também")
                .setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        progressDialog.setMessage("Excluindo Pessoa...");
                        progressDialog.show();
                        StringRequest request = new StringRequest(
                                Request.Method.DELETE,
                                ApiRoutes.PESSOA.Delete(pessoaSelecionada.pessoa_id),
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        Pessoa pessoaResposta = new Gson().fromJson(response, Pessoa.class);
                                        P.removerPessoa(pessoaResposta);
                                        pessoas.remove(pessoaResposta);
                                        adapter.notifyDataSetChanged();
                                        progressDialog.dismiss();
                                    }
                                },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        progressDialog.dismiss();
                                        MinhaeiroErrorHelper.alertar(error, PessoasActivity.this);
                                    }
                                }
                        );
                        AppController.getInstance().addToRequestQueue(request);
                    }
                })
                .setNegativeButton("Cancelar", null)
                .create()
                .show();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.act_excluir:
                abrirPessoaDialog();
                return true;
        }
        return false;
    }
}
