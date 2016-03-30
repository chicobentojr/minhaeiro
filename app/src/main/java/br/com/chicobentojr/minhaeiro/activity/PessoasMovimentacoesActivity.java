package br.com.chicobentojr.minhaeiro.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.VolleyError;

import java.util.ArrayList;

import br.com.chicobentojr.minhaeiro.R;
import br.com.chicobentojr.minhaeiro.adapters.MovimentacaoAdapter;
import br.com.chicobentojr.minhaeiro.models.Movimentacao;
import br.com.chicobentojr.minhaeiro.models.Pessoa;
import br.com.chicobentojr.minhaeiro.utils.DividerItemDecoration;
import br.com.chicobentojr.minhaeiro.utils.Extensoes;
import br.com.chicobentojr.minhaeiro.utils.MinhaeiroErrorHelper;
import br.com.chicobentojr.minhaeiro.utils.P;

public class PessoasMovimentacoesActivity extends AppCompatActivity {

    private Pessoa pessoa;
    private ArrayList<Movimentacao> movimentacoes;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    private ProgressDialog progressDialog;
    private TextView lblSaldo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.iniciarLayout();
    }

    public void iniciarLayout() {
        setContentView(R.layout.activity_pessoas_movimentacoes);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        pessoa = (Pessoa) getIntent().getSerializableExtra("pessoa");
        movimentacoes = P.getUsuarioInstance().Movimentacao;
        lblSaldo = (TextView) findViewById(R.id.lblSaldo);
        progressDialog = new ProgressDialog(this);
        progressDialog.setCanceledOnTouchOutside(false);

        if (pessoa != null) {
            this.setTitle(pessoa.nome);
            recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
            recyclerView.setHasFixedSize(true);
            recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
            layoutManager = new LinearLayoutManager(this);
            recyclerView.setLayoutManager(layoutManager);

            movimentacoes = Movimentacao.filtrarPorPessoa(movimentacoes, pessoa);

            adapter = new MovimentacaoAdapter(movimentacoes);
            recyclerView.setAdapter(adapter);

            lblSaldo.setText("Saldo: " + Extensoes.LAYOUT.valor(Movimentacao.obterSaldo(movimentacoes)));
        }
    }

    public void abrirEditarPessoaDialog(View view) {
        final AlertDialog alertDialog = new AlertDialog.Builder(PessoasMovimentacoesActivity.this)
                .setTitle("Editar Pessoa")
                .setView(R.layout.dialog_categoria_cadastro)
                .setPositiveButton("Editar", null)
                .setNegativeButton("Cancelar", null)
                .create();

        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                Button positiveButton = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
                final EditText txtNome = (EditText) (alertDialog).findViewById(R.id.txtNome);
                txtNome.setText(pessoa.nome);
                positiveButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String nome = txtNome.getText().toString();
                        if (nome.isEmpty()) {
                            txtNome.setError(getString(R.string.nome_vazio_erro));
                            txtNome.requestFocus();
                        } else {
                            pessoa.nome = nome;
                            progressDialog.setMessage("Editando Pessoa...");
                            progressDialog.show();

                            Pessoa.editar(pessoa, new Pessoa.ApiListener() {
                                @Override
                                public void sucesso(Pessoa pessoa) {
                                    Pessoa.editar(pessoa);
                                    alertDialog.dismiss();
                                    progressDialog.dismiss();
                                    finish();
                                }

                                @Override
                                public void erro(VolleyError erro) {
                                    progressDialog.dismiss();
                                    MinhaeiroErrorHelper.alertar(erro, PessoasMovimentacoesActivity.this);
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
