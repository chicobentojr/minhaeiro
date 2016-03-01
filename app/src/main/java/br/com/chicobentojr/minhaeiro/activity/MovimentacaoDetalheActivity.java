package br.com.chicobentojr.minhaeiro.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;

import org.json.JSONObject;

import br.com.chicobentojr.minhaeiro.R;
import br.com.chicobentojr.minhaeiro.adapters.MovimentacaoDetalhePagerAdapter;
import br.com.chicobentojr.minhaeiro.fragments.MovimentacaoItensFragment;
import br.com.chicobentojr.minhaeiro.models.Categoria;
import br.com.chicobentojr.minhaeiro.models.Movimentacao;
import br.com.chicobentojr.minhaeiro.models.MovimentacaoItem;
import br.com.chicobentojr.minhaeiro.models.Pessoa;
import br.com.chicobentojr.minhaeiro.models.Usuario;
import br.com.chicobentojr.minhaeiro.utils.ApiRoutes;
import br.com.chicobentojr.minhaeiro.utils.AppController;
import br.com.chicobentojr.minhaeiro.utils.MinhaeiroErrorHelper;
import br.com.chicobentojr.minhaeiro.utils.P;

public class MovimentacaoDetalheActivity extends AppCompatActivity {

    public static int FRG_DETALHES_INDEX = 0;
    public static int FRG_ITENS_INDEX = 1;

    private MovimentacaoItensFragment itensFragment;

    private Usuario usuario;
    private Movimentacao movimentacao;
    private int item_posicao;

    private Spinner spnCategoria;
    private Spinner spnPessoa;
    private EditText txtMovimentacaoData;
    private EditText txtMovimentacaoValor;
    private EditText txtDescricao;
    private Spinner spnMovimentacaoTipo;
    private Switch swtRealizada;

    private ArrayAdapter<Categoria> adpCategoria;
    private ArrayAdapter<Pessoa> adpPessoa;

    private ProgressDialog progressDialog;
    private AlertDialog itemDialog;
    private FloatingActionButton fabCadastrarItem;

    private MovimentacaoDetalhePagerAdapter pagerAdapter;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movimentacao_detalhe);
        this.iniciarLayout();
    }

    public void iniciarLayout() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        viewPager = (ViewPager) findViewById(R.id.container);
        fabCadastrarItem = (FloatingActionButton) findViewById(R.id.fabCadastrarItem);
        pagerAdapter = new MovimentacaoDetalhePagerAdapter(getSupportFragmentManager(), this);

        viewPager.setAdapter(pagerAdapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                alternarBotao(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        usuario = P.getUsuarioInstance();

        progressDialog = new ProgressDialog(this);
        progressDialog.setCanceledOnTouchOutside(false);
    }

    public void iniciarDialogLayout(AlertDialog dialog) {
        movimentacao = (Movimentacao) getIntent().getSerializableExtra("movimentacao");
        item_posicao = getIntent().getIntExtra("item_posicao", -1);

        adpPessoa = new ArrayAdapter(this, android.R.layout.simple_spinner_item, usuario.Pessoa);
        adpPessoa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnPessoa = (Spinner) dialog.findViewById(R.id.spnPessoa);
        spnPessoa.setAdapter(adpPessoa);

        txtMovimentacaoValor = (EditText) dialog.findViewById(R.id.txtMovimentacaoValor);
        txtDescricao = (EditText) dialog.findViewById(R.id.txtDescricao);
        spnMovimentacaoTipo = (Spinner) dialog.findViewById(R.id.spnMovimentacaoTipo);
        swtRealizada = (Switch) dialog.findViewById(R.id.swtRealizada);

    }

    public void abrirCadastroItemDialog(View view) {
        itemDialog = new AlertDialog.Builder(this)
                .setTitle("Novo Item")
                .setView(R.layout.dialog_movimentacao_item_cadastro)
                .setPositiveButton("Cadastrar", null)
                .setNegativeButton("Cancelar", null)
                .create();

        itemDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                Button positiveButton = itemDialog.getButton(DialogInterface.BUTTON_POSITIVE);
                positiveButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        cadastrar(v);
                    }
                });
            }
        });
        itemDialog.show();
        this.iniciarDialogLayout(itemDialog);
    }

    public void cadastrar(View v) {
        limparErros();

        int pessoa_id = ((Pessoa) spnPessoa.getSelectedItem()).pessoa_id;
        //String movimentacao_data = txtMovimentacaoData.getText().toString();
        String valor = txtMovimentacaoValor.getText().toString();
        String descricao = txtDescricao.getText().toString();
        char tipo = getResources().getStringArray(R.array.tipo_movimentacao_valor)[spnMovimentacaoTipo.getSelectedItemPosition()].charAt(0);
        boolean realizada = swtRealizada.isChecked();

        boolean valido = true;
        View focusView = null;

        if (descricao.isEmpty()) {
            txtDescricao.setError(getString(R.string.descricao_vazio_erro));
            focusView = txtDescricao;
            valido = false;
        } else if (valor.isEmpty()) {
            txtMovimentacaoValor.setError(getString(R.string.valor_vazio_erro));
            focusView = txtMovimentacaoValor;
            valido = false;
        } else if (Double.parseDouble(valor) <= 0) {
            txtMovimentacaoValor.setError(getString(R.string.valor_maior_zero_erro));
            focusView = txtMovimentacaoValor;
            valido = false;
        }

        if (!valido) {
            focusView.requestFocus();
        } else {
            MovimentacaoItem item = new MovimentacaoItem();

            item.pessoa_id = pessoa_id;
            //item.item_data = Extensoes.STRING.toBrazilianDate(movimentacao_data);
            item.valor = Double.parseDouble(valor);
            item.descricao = descricao;
            item.tipo = tipo;
            item.realizada = realizada;

            cadastrarMovimentacaoItem(item);
        }
    }

    public void limparErros() {
        txtDescricao.setError(null);
        txtMovimentacaoValor.setError(null);
    }

    public void cadastrarMovimentacaoItem(MovimentacaoItem item) {
        progressDialog.setMessage("Carregando...");
        progressDialog.show();

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                ApiRoutes.MOVIMENTACAO_ITEM.Post(movimentacao.movimentacao_id),
                new JSONObject(item.toParams()),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        MovimentacaoItem itemResposta = new Gson().fromJson(response.toString(), MovimentacaoItem.class);
                        MovimentacaoItensFragment fragment = (MovimentacaoItensFragment) pagerAdapter.getItem(FRG_ITENS_INDEX);
                        fragment.adicionarItemAdapter(itemResposta);
                        progressDialog.dismiss();
                        itemDialog.dismiss();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.hide();
                MinhaeiroErrorHelper.alertar(error, MovimentacaoDetalheActivity.this);
            }
        });
        AppController.getInstance().addToRequestQueue(request);
    }

    public void alternarBotao(int fragmentPosicao) {
        if (fragmentPosicao == 0) {
            fabCadastrarItem.hide();
        } else if (fragmentPosicao == 1) {
            fabCadastrarItem.show();
        }
    }
}
