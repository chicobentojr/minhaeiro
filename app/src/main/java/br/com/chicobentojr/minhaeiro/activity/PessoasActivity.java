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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupMenu;

import com.android.volley.VolleyError;

import java.util.ArrayList;

import br.com.chicobentojr.minhaeiro.R;
import br.com.chicobentojr.minhaeiro.adapters.PessoasAdapter;
import br.com.chicobentojr.minhaeiro.models.Pessoa;
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_adicionar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.act_adicionar:
                this.abrirPessoaAdicionarDialog();
                return true;
        }

        return super.onOptionsItemSelected(item);
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

    public void abrirExcluirPessoaDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Excluir " + pessoaSelecionada.nome + "?")
                .setMessage("Tem certeza que deseja prosseguir?\n\nTodas as movimentações desta pessoa serão excluídas também")
                .setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        progressDialog.setMessage("Excluindo Pessoa...");
                        progressDialog.show();

                        Pessoa.excluir(pessoaSelecionada, new Pessoa.ApiListener() {
                            @Override
                            public void sucesso(Pessoa pessoa) {
                                Pessoa.remover(pessoa);
                                pessoas.remove(pessoa);
                                adapter.notifyDataSetChanged();
                                progressDialog.dismiss();
                            }

                            @Override
                            public void erro(VolleyError erro) {
                                progressDialog.dismiss();
                                MinhaeiroErrorHelper.alertar(erro, PessoasActivity.this);
                            }
                        });
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
                abrirExcluirPessoaDialog();
                return true;
        }
        return false;
    }

    public void abrirPessoaAdicionarDialog() {
        final AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setTitle("Nova Pessoa")
                .setView(R.layout.dialog_pessoa_cadastro)
                .setPositiveButton("Cadastrar", null)
                .setNegativeButton("Cancelar", null)
                .create();

        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                Button positiveButton = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
                positiveButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        EditText txtNome = (EditText) (alertDialog).findViewById(R.id.txtNome);
                        String nome = txtNome.getText().toString();
                        final Pessoa pessoa = new Pessoa();
                        pessoa.nome = nome;
                        if (nome.isEmpty()) {
                            txtNome.setError(getString(R.string.nome_vazio_erro));
                            txtNome.requestFocus();
                        } else {
                            progressDialog.setMessage("Carregando...");
                            progressDialog.show();

                            Pessoa.cadastrar(pessoa, new Pessoa.ApiListener() {
                                @Override
                                public void sucesso(Pessoa pessoa) {
                                    pessoas.add(pessoa);
                                    adapter.notifyDataSetChanged();
                                    alertDialog.dismiss();
                                    progressDialog.dismiss();
                                }

                                @Override
                                public void erro(VolleyError erro) {
                                    progressDialog.dismiss();
                                    MinhaeiroErrorHelper.alertar(erro, PessoasActivity.this);
                                }
                            });
                        }
                    }
                });
            }
        });
        alertDialog.show();
    }
}
