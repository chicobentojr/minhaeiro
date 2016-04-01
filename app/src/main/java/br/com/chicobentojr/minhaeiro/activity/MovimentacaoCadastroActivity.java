package br.com.chicobentojr.minhaeiro.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;

import com.android.volley.VolleyError;

import java.util.Calendar;

import br.com.chicobentojr.minhaeiro.R;
import br.com.chicobentojr.minhaeiro.dialogs.DatePicker;
import br.com.chicobentojr.minhaeiro.models.Categoria;
import br.com.chicobentojr.minhaeiro.models.Movimentacao;
import br.com.chicobentojr.minhaeiro.models.Pessoa;
import br.com.chicobentojr.minhaeiro.models.Usuario;
import br.com.chicobentojr.minhaeiro.utils.Extensoes;
import br.com.chicobentojr.minhaeiro.utils.MinhaeiroErrorHelper;
import br.com.chicobentojr.minhaeiro.utils.P;
import br.com.chicobentojr.minhaeiro.utils.SpinnerHelper;

public class MovimentacaoCadastroActivity extends AppCompatActivity implements DatePicker.DataFornecidaListener {

    private Usuario usuario;
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


    @Override
    public void preencherData(Calendar calendario) {
        String data = "";
        int dia = calendario.get(Calendar.DAY_OF_MONTH);
        int mes = calendario.get(Calendar.MONTH) + 1;
        int ano = calendario.get(Calendar.YEAR);

        data = String.format("%02d", dia) + "/" + String.format("%02d", mes) + "/" + ano;

        txtMovimentacaoData.setText(data);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.iniciarLayout();
        this.preencherData(Calendar.getInstance());
    }

    public void iniciarLayout() {
        setContentView(R.layout.activity_movimentacao_cadastro);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        usuario = P.getUsuarioInstance();

        adpCategoria = new ArrayAdapter(this, android.R.layout.simple_spinner_item, usuario.Categoria);
        adpCategoria.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnCategoria = (Spinner) findViewById(R.id.spnCategoria);
        spnCategoria.setAdapter(adpCategoria);

        adpPessoa = new ArrayAdapter(this, android.R.layout.simple_spinner_item, usuario.Pessoa);
        adpPessoa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnPessoa = (Spinner) findViewById(R.id.spnPessoa);
        spnPessoa.setAdapter(adpPessoa);

        txtMovimentacaoData = (EditText) findViewById(R.id.txtMovimentacaoData);
        txtMovimentacaoValor = (EditText) findViewById(R.id.txtMovimentacaoValor);
        txtDescricao = (EditText) findViewById(R.id.txtDescricao);
        spnMovimentacaoTipo = (Spinner) findViewById(R.id.spnMovimentacaoTipo);
        swtRealizada = (Switch) findViewById(R.id.swtRealizada);

        progressDialog = new ProgressDialog(this);
        progressDialog.setCanceledOnTouchOutside(false);
    }

    public void cadastrar(View v) {
        limparErros();

        String movimentacao_data = txtMovimentacaoData.getText().toString();
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
        } else if (movimentacao_data.isEmpty()) {
            txtMovimentacaoData.setError(getString(R.string.data_vazio_erro));
            focusView = txtMovimentacaoData;
            valido = false;
        } else if (Double.parseDouble(valor) <= 0) {
            txtMovimentacaoValor.setError(getString(R.string.valor_maior_zero_erro));
            focusView = txtMovimentacaoValor;
            valido = false;
        }

        if (!valido) {
            focusView.requestFocus();
        } else {
            Movimentacao movimentacao = new Movimentacao();

            movimentacao.Categoria = ((Categoria) spnCategoria.getSelectedItem());
            movimentacao.Pessoa = ((Pessoa) spnPessoa.getSelectedItem());
            movimentacao.categoria_id = movimentacao.Categoria.categoria_id;
            movimentacao.pessoa_id = movimentacao.Pessoa.pessoa_id;
            movimentacao.movimentacao_data = Extensoes.STRING.toBrazilianDate(movimentacao_data);
            movimentacao.valor = Double.parseDouble(valor);
            movimentacao.descricao = descricao;
            movimentacao.tipo = tipo;
            movimentacao.realizada = realizada;

            cadastrarMovimentacao(movimentacao);
        }
    }

    public void cadastrarMovimentacao(final Movimentacao movimentacao) {
        progressDialog.setMessage("Carregando...");
        progressDialog.show();

        Movimentacao.cadastrar(movimentacao, new Movimentacao.ApiListener() {
            @Override
            public void sucesso(Movimentacao movimentacao) {
                progressDialog.hide();
                Intent intentResposta = new Intent(MovimentacaoCadastroActivity.this, MainActivity.class);
                intentResposta.putExtra("movimentacao", movimentacao);
                setResult(RESULT_OK, intentResposta);
                finish();
            }

            @Override
            public void erro(VolleyError erro) {
                progressDialog.hide();
                MinhaeiroErrorHelper.alertar(erro, MovimentacaoCadastroActivity.this);
            }
        });
    }

    public void limparErros() {
        txtMovimentacaoData.setError(null);
        txtMovimentacaoValor.setError(null);
        txtDescricao.setError(null);
    }

    public void abrirDatePicker(View view) {
        DatePicker datePicker = new DatePicker(this);
        datePicker.show(getFragmentManager(), "DatePicker");
    }

    public void abrirCadastrarCategoriaDialog(View view) {
        final AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setTitle("Nova Categoria")
                .setView(R.layout.dialog_categoria_cadastro)
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
                        Categoria categoria = new Categoria();
                        categoria.nome = nome;
                        if (nome.isEmpty()) {
                            txtNome.setError(getString(R.string.nome_vazio_erro));
                            txtNome.requestFocus();
                        } else {
                            progressDialog.setMessage("Carregando...");
                            progressDialog.show();

                            Categoria.cadastrar(categoria, new Categoria.ApiListener() {
                                @Override
                                public void sucesso(Categoria categoria) {
                                    adpCategoria.notifyDataSetChanged();
                                    spnCategoria.setSelection(SpinnerHelper.getSelectedItemPosition(spnCategoria, categoria));
                                    alertDialog.dismiss();
                                    progressDialog.dismiss();
                                }

                                @Override
                                public void erro(VolleyError erro) {
                                    progressDialog.dismiss();
                                    MinhaeiroErrorHelper.alertar(erro, MovimentacaoCadastroActivity.this);
                                }
                            });
                        }
                    }
                });
            }
        });
        alertDialog.show();
    }

    public void abrirCadastrarPessoaDialog(View view) {
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
                                    adpPessoa.notifyDataSetChanged();
                                    spnPessoa.setSelection(SpinnerHelper.getSelectedItemPosition(spnPessoa, pessoa));
                                    alertDialog.dismiss();
                                    progressDialog.dismiss();
                                }

                                @Override
                                public void erro(VolleyError erro) {
                                    progressDialog.dismiss();
                                    MinhaeiroErrorHelper.alertar(erro, MovimentacaoCadastroActivity.this);
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

