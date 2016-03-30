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

import com.android.volley.VolleyError;

import java.util.ArrayList;

import br.com.chicobentojr.minhaeiro.R;
import br.com.chicobentojr.minhaeiro.adapters.CategoriasAdapter;
import br.com.chicobentojr.minhaeiro.models.Categoria;
import br.com.chicobentojr.minhaeiro.utils.DividerItemDecoration;
import br.com.chicobentojr.minhaeiro.utils.ItemClickSupport;
import br.com.chicobentojr.minhaeiro.utils.MinhaeiroErrorHelper;
import br.com.chicobentojr.minhaeiro.utils.P;

public class CategoriasActivity extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {

    private ArrayList<Categoria> categorias;
    private Categoria categoriaSelecionada;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.iniciarLayout();
    }

    @Override
    protected void onResume() {
        super.onResume();
        this.iniciarLayout();
    }

    public void iniciarLayout() {
        this.setContentView(R.layout.activity_categorias);
        this.getSupportActionBar().setHomeButtonEnabled(true);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        progressDialog = new ProgressDialog(this);
        progressDialog.setCanceledOnTouchOutside(false);

        categorias = P.getUsuarioInstance().Categoria;

        adapter = new CategoriasAdapter(categorias);
        recyclerView.setAdapter(adapter);

        definirRecyclerViewItemClicks();
    }

    public void definirRecyclerViewItemClicks() {
        ItemClickSupport.addTo(recyclerView).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                Categoria categoria = categorias.get(position);
                Intent intent = new Intent(CategoriasActivity.this, CategoriasMovimentacoesActivity.class);
                intent.putExtra("categoria", categoria);
                startActivity(intent);
            }
        });
        ItemClickSupport.addTo(recyclerView).setOnItemLongClickListener(new ItemClickSupport.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClicked(RecyclerView recyclerView, int position, View v) {
                categoriaSelecionada = categorias.get(position);
                PopupMenu popupMenu = new PopupMenu(CategoriasActivity.this, v, Gravity.LEFT);
                popupMenu.setOnMenuItemClickListener(CategoriasActivity.this);
                popupMenu.inflate(R.menu.popup_gerenciar);
                popupMenu.show();
                return true;
            }
        });
    }

    public void abrirExcluirCategoriaDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Excluir " + categoriaSelecionada.nome + "?")
                .setMessage("Tem certeza que deseja prosseguir?\n\nTodas as movimentações desta categoria serão excluídas também")
                .setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        progressDialog.setMessage("Excluindo Pessoa...");
                        progressDialog.show();
                        Categoria.excluir(categoriaSelecionada, new Categoria.ApiListener() {
                            @Override
                            public void sucesso(Categoria categoria) {
                                Categoria.remover(categoria);
                                categorias.remove(categoria);
                                adapter.notifyDataSetChanged();
                                progressDialog.dismiss();
                            }

                            @Override
                            public void erro(VolleyError error) {
                                progressDialog.dismiss();
                                MinhaeiroErrorHelper.alertar(error, CategoriasActivity.this);
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
                abrirExcluirCategoriaDialog();
                return true;
            case R.id.act_editar:
                Intent intent = new Intent(this, CategoriaEditarActivity.class);
                intent.putExtra("categoria", categoriaSelecionada);
                startActivity(intent);
                return true;
        }
        return false;
    }

}
