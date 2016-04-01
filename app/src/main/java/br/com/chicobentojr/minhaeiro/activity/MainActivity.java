package br.com.chicobentojr.minhaeiro.activity;

import android.app.ProgressDialog;
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
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.android.volley.VolleyError;

import java.util.ArrayList;

import br.com.chicobentojr.minhaeiro.R;
import br.com.chicobentojr.minhaeiro.adapters.MovimentacaoAdapter;
import br.com.chicobentojr.minhaeiro.models.Movimentacao;
import br.com.chicobentojr.minhaeiro.models.Usuario;
import br.com.chicobentojr.minhaeiro.utils.DividerItemDecoration;
import br.com.chicobentojr.minhaeiro.utils.ItemClickSupport;
import br.com.chicobentojr.minhaeiro.utils.MinhaeiroErrorHelper;
import br.com.chicobentojr.minhaeiro.utils.P;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, PopupMenu.OnMenuItemClickListener {

    private TextView lblUsuarioNome;
    private TextView lblUsuarioLogin;
    private View navHeader;

    private ProgressDialog progressDialog;
    private ArrayList<Movimentacao> movimentacoes;
    private Movimentacao movimentacaoSelecionada;

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
    protected void onResume() {
        super.onResume();
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
            case R.id.nav_pendentes:
                startActivity(new Intent(this, PendentesActivity.class));
                break;
            case R.id.nav_periodo:
                startActivity(new Intent(this, PeriodoActivity.class));
                break;
            case R.id.nav_exportar:
                if (P.exportarDados()) {
                    Snackbar.make(recyclerView, "Dados exportados com Sucesso!", Snackbar.LENGTH_SHORT).show();
                } else {
                    Snackbar.make(recyclerView, "Erro na exportação dos dados!", Snackbar.LENGTH_SHORT).show();
                }
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
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(MainActivity.this, MovimentacaoCadastroActivity.class), P.REQUEST.MOVIMENTACAO_CADASTRO);
            }
        });

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

        progressDialog = new ProgressDialog(this);
        progressDialog.setCanceledOnTouchOutside(false);

        definirRecyclerViewItemClicks();
    }

    public void carregarMovimentacoes() {
        progressDialog.setMessage("Carregando Movimentações...");
        progressDialog.show();

        Usuario.listar(new Usuario.ApiListener() {
            @Override
            public void sucesso(Usuario usuario) {
                movimentacoes = usuario.Movimentacao;
                adapter = new MovimentacaoAdapter(movimentacoes);
                recyclerView.setAdapter(adapter);
                progressDialog.hide();
            }

            @Override
            public void erro(VolleyError erro) {
                progressDialog.hide();
                MinhaeiroErrorHelper.alertar(erro, MainActivity.this);
            }
        });
    }

    public void excluirMovimentacao(final Movimentacao movimentacao) {
        progressDialog.setMessage("Excluindo Movimentação...");
        progressDialog.show();

        Movimentacao.excluir(movimentacao, new Movimentacao.ApiListener() {
            @Override
            public void sucesso(Movimentacao movimentacao) {
                movimentacoes.remove(movimentacao);
                adapter.notifyDataSetChanged();
                progressDialog.hide();
            }

            @Override
            public void erro(VolleyError erro) {
                progressDialog.hide();
                MinhaeiroErrorHelper.alertar(erro, MainActivity.this);
            }
        });
    }

    public void sair() {
        P.limpar();
        startActivity(new Intent(this, LoginActivity.class));
    }

    public void definirRecyclerViewItemClicks() {
        ItemClickSupport.addTo(recyclerView).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                Movimentacao movimentacao = movimentacoes.get(position);

                Intent intent = new Intent(MainActivity.this, MovimentacaoDetalheActivity.class);

                intent.putExtra("movimentacao", movimentacao);
                intent.putExtra("item_posicao", position);

                startActivityForResult(intent, P.REQUEST.MOVIMENTACAO_ATUALIZACAO);
            }
        });

        ItemClickSupport.addTo(recyclerView).setOnItemLongClickListener(new ItemClickSupport.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClicked(RecyclerView recyclerView, int position, View v) {
                movimentacaoSelecionada = movimentacoes.get(position);

                PopupMenu popupMenu = new PopupMenu(MainActivity.this, v, Gravity.LEFT);
                popupMenu.setOnMenuItemClickListener(MainActivity.this);
                popupMenu.inflate(R.menu.popup_movimentacao);
                popupMenu.show();
                return true;
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == P.REQUEST.MOVIMENTACAO_CADASTRO) {
            if (resultCode == RESULT_OK && data != null) {
                adapter.notifyDataSetChanged();
                Snackbar.make(recyclerView, "Movimentação cadastrada com sucesso!", Snackbar.LENGTH_LONG).show();
            }
        } else if (requestCode == P.REQUEST.MOVIMENTACAO_ATUALIZACAO) {
            if (resultCode == RESULT_OK && data != null) {
                Movimentacao movimentacao = (Movimentacao) data.getSerializableExtra("movimentacao");
                int item_posicao = data.getIntExtra("item_posicao", -1);
                movimentacoes.set(item_posicao, movimentacao);
                adapter.notifyItemChanged(item_posicao);
                Snackbar.make(recyclerView, "Movimentação atualizada com sucesso!", Snackbar.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.act_excluir:
                this.excluirMovimentacao(movimentacaoSelecionada);
                return true;
        }
        return false;
    }

}
