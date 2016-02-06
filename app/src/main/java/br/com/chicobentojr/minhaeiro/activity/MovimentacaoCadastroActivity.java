package br.com.chicobentojr.minhaeiro.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;

import org.json.JSONObject;

import br.com.chicobentojr.minhaeiro.R;
import br.com.chicobentojr.minhaeiro.models.Categoria;
import br.com.chicobentojr.minhaeiro.models.Movimentacao;
import br.com.chicobentojr.minhaeiro.models.Pessoa;
import br.com.chicobentojr.minhaeiro.models.Usuario;
import br.com.chicobentojr.minhaeiro.utils.ApiRoutes;
import br.com.chicobentojr.minhaeiro.utils.AppController;
import br.com.chicobentojr.minhaeiro.utils.MinhaeiroErrorHelper;
import br.com.chicobentojr.minhaeiro.utils.MinhaeiroRetryPolicy;
import br.com.chicobentojr.minhaeiro.utils.P;

public class MovimentacaoCadastroActivity extends AppCompatActivity {

    private Usuario usuario;
    private Spinner spnCategoria;
    private Spinner spnPessoa;
    private EditText txtMovimentacaoData;
    private EditText txtMovimentacaoValor;
    private EditText txtDescricao;
    private Spinner spnMovimentacaoTipo;
    private Switch swtRealizada;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movimentacao_cadastro);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        usuario = P.getUsuario(P.obter(P.USUARIO_JSON));

        spnCategoria = (Spinner) findViewById(R.id.spnCategoria);
        spnCategoria.setAdapter(new ArrayAdapter(this,android.R.layout.simple_spinner_item,usuario.Categoria));
        spnPessoa = (Spinner) findViewById(R.id.spnPessoa);
        spnPessoa.setAdapter(new ArrayAdapter(this,android.R.layout.simple_spinner_item,usuario.Pessoa));

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

        int categoria_id = ((Categoria)spnCategoria.getSelectedItem()).categoria_id;
        int pessoa_id = ((Pessoa)spnPessoa.getSelectedItem()).pessoa_id;
        String movimentacao_data = txtMovimentacaoData.getText().toString();
        double valor = Double.parseDouble(txtMovimentacaoValor.getText().toString());
        String descricao = txtDescricao.getText().toString();
        char tipo = getResources().getStringArray(R.array.tipo_movimentacao_valor)[spnMovimentacaoTipo.getSelectedItemPosition()].charAt(0);
        boolean realizada = swtRealizada.isChecked();

        boolean valido = true;
        View focusView = null;

        if (movimentacao_data.isEmpty()) {
            txtMovimentacaoData.setError("A Data é obrigatória");
            focusView = txtMovimentacaoData;
            valido = false;
        } else if (String.valueOf(valor).isEmpty() || valor <= 0) {
            txtMovimentacaoValor.setError("O Valor é obrigatório");
            focusView = txtMovimentacaoValor;
            valido = false;
        } else if (descricao.isEmpty()) {
            txtDescricao.setError("A Descrição é obrigatória");
            focusView = txtDescricao;
            valido = false;
        } else if (String.valueOf(tipo).isEmpty()) {
            //txtMovimentacaoTipo.setError("O Tipo é obrigatório");
            //focusView = txtMovimentacaoTipo;
            valido = false;
        } else if (String.valueOf(realizada).isEmpty()) {
            //txtRealizada.setError("Esse campo é obrigatório");
            //focusView = txtRealizada;
            valido = false;
        }

        if (!valido) {
            focusView.requestFocus();
        } else {
            Movimentacao movimentacao = new Movimentacao();

            movimentacao.categoria_id = categoria_id;
            movimentacao.pessoa_id = pessoa_id;
            movimentacao.movimentacao_data = movimentacao_data;
            movimentacao.valor = valor;
            movimentacao.descricao = descricao;
            movimentacao.tipo = tipo;
            movimentacao.realizada = realizada;

            cadastrarMovimentacao(movimentacao);
        }
    }

    public void cadastrarMovimentacao(Movimentacao movimentacao){
        progressDialog.setMessage("Carregando...");
        progressDialog.show();

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                ApiRoutes.montar(P.autenticacao(),"movimentacao",P.usuario_id()),
                new JSONObject(movimentacao.toParams()),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Gson gson = new Gson();
                        Movimentacao movimentacaoResposta = gson.fromJson(response.toString(), Movimentacao.class);

                        progressDialog.hide();
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.hide();
                MinhaeiroErrorHelper.alertar(error, MovimentacaoCadastroActivity.this);
            }
        });
        AppController.getInstance().addToRequestQueue(request);
    }

    public void limparErros() {
        txtMovimentacaoData.setError(null);
        txtMovimentacaoValor.setError(null);
        txtDescricao.setError(null);
    }

}

