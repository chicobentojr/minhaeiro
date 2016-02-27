package br.com.chicobentojr.minhaeiro.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;

import br.com.chicobentojr.minhaeiro.R;
import br.com.chicobentojr.minhaeiro.adapters.MovimentacaoAdapter;
import br.com.chicobentojr.minhaeiro.listeners.RecyclerItemClickListener;
import br.com.chicobentojr.minhaeiro.models.Movimentacao;
import br.com.chicobentojr.minhaeiro.models.Usuario;
import br.com.chicobentojr.minhaeiro.utils.ApiRoutes;
import br.com.chicobentojr.minhaeiro.utils.AppController;
import br.com.chicobentojr.minhaeiro.utils.DividerItemDecoration;
import br.com.chicobentojr.minhaeiro.utils.MinhaeiroErrorHelper;
import br.com.chicobentojr.minhaeiro.utils.P;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private TextView lblUsuarioNome;
    private TextView lblUsuarioLogin;
    private View navHeader;

    private ProgressDialog progressDialog;
    private ArrayList<Movimentacao> movimentacoes;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (P.usuarioConectado()) {
            this.iniciarLayout();
            this.carregarMovimentacoes();
        } else {
            this.finish();
            startActivity(new Intent(this, LoginActivity.class));
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        this.carregarMovimentacoes();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.act_atualizar:
                this.carregarMovimentacoes();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_categorias:
                startActivity(new Intent(this, CategoriasActivity.class));
                break;
            case R.id.nav_pessoas:
                startActivity(new Intent(this, PessoasActivity.class));
                break;
            case R.id.nav_perfil:
                startActivity(new Intent(this, PerfilActivity.class));
                break;
            case R.id.nav_sair:
                this.sair();
                break;
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void iniciarLayout() {
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.btn_floating);
        fab.setOnClickListener(new BtnFloatingOnClick());

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navHeader = navigationView.getHeaderView(0);
        navigationView.setNavigationItemSelectedListener(this);
        lblUsuarioNome = (TextView) navHeader.findViewById(R.id.lblUsuarioNome);
        lblUsuarioLogin = (TextView) navHeader.findViewById(R.id.lblUsuarioLogin);
        lblUsuarioNome.setText(P.nome());
        lblUsuarioLogin.setText(P.login());

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void OnItemClick(View view, int position) {
                        Movimentacao movimentacao = movimentacoes.get(position);

                        Intent intent = new Intent(MainActivity.this, MovimentacaoDetalheActivity.class);

                        intent.putExtra("movimentacao", movimentacao);
                        intent.putExtra("item_posicao",position);

                        startActivityForResult(intent, P.REQUEST.MOVIMENTACAO_ATUALIZACAO);
                    }
                })
        );

        progressDialog = new ProgressDialog(this);
        progressDialog.setCanceledOnTouchOutside(false);
    }

    public void carregarMovimentacoes() {
        progressDialog.setMessage("Carregando Movimentações...");
        progressDialog.show();
        StringRequest request = new StringRequest(
                ApiRoutes.USUARIO.Get(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Gson gson = new Gson();
                        Usuario usuarioResposta = new Gson().fromJson(response,Usuario.class);
                        movimentacoes = usuarioResposta.Movimentacao;
                        adapter = new MovimentacaoAdapter(movimentacoes);
                        recyclerView.setAdapter(adapter);

                        P.inserir(P.USUARIO_JSON,response);

                        progressDialog.hide();

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.hide();
                MinhaeiroErrorHelper.alertar(error, MainActivity.this);
            }
        }
        );
        AppController.getInstance().addToRequestQueue(request);
    }

    public void sair() {
        P.limpar();
        startActivity(new Intent(this, LoginActivity.class));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == P.REQUEST.MOVIMENTACAO_CADASTRO) {
            if (resultCode == RESULT_OK && data != null) {
                Movimentacao movimentacao = (Movimentacao) data.getSerializableExtra("movimentacao");
                movimentacoes.add(0, movimentacao);
                adapter.notifyDataSetChanged();
                Snackbar.make(recyclerView, "Movimentação cadastrada com sucesso!", Snackbar.LENGTH_LONG).show();
            }
        } else if(requestCode == P.REQUEST.MOVIMENTACAO_ATUALIZACAO) {
            if (resultCode == RESULT_OK && data != null) {
                Movimentacao movimentacao = (Movimentacao) data.getSerializableExtra("movimentacao");
                int item_posicao = data.getIntExtra("item_posicao",-1);
                movimentacoes.set(item_posicao, movimentacao);
                adapter.notifyItemChanged(item_posicao);
                Snackbar.make(recyclerView, "Movimentação atualizada com sucesso!", Snackbar.LENGTH_LONG).show();
            }
        }
    }

    private class BtnFloatingOnClick implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            /*Snackbar.make(v, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();*/
            startActivityForResult(new Intent(MainActivity.this, MovimentacaoCadastroActivity.class), P.REQUEST.MOVIMENTACAO_CADASTRO);
        }
    }
}
