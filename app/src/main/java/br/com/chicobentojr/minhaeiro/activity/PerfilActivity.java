package br.com.chicobentojr.minhaeiro.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import br.com.chicobentojr.minhaeiro.R;
import br.com.chicobentojr.minhaeiro.models.Usuario;
import br.com.chicobentojr.minhaeiro.utils.ApiRoutes;
import br.com.chicobentojr.minhaeiro.utils.AppController;
import br.com.chicobentojr.minhaeiro.utils.MinhaeiroErrorHelper;
import br.com.chicobentojr.minhaeiro.utils.MinhaeiroRetryPolicy;
import br.com.chicobentojr.minhaeiro.utils.P;

public class PerfilActivity extends AppCompatActivity implements TextView.OnEditorActionListener {

    private EditText txtNome;
    private EditText txtLogin;
    private EditText txtSenha;
    private EditText txtConfirmaSenha;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        txtNome = (EditText) findViewById(R.id.txtNome);
        txtLogin = (EditText) findViewById(R.id.txtLogin);
        txtSenha = (EditText) findViewById(R.id.txtSenha);
        txtConfirmaSenha = (EditText) findViewById(R.id.txtConfirmaSenha);

        txtConfirmaSenha.setOnEditorActionListener(this);

        progressDialog = new ProgressDialog(this);
        progressDialog.setCanceledOnTouchOutside(false);
    }

    public void atualizar(View v) {
        limparErros();

        String nome = txtNome.getText().toString();
        String login = txtLogin.getText().toString();
        String senha = txtSenha.getText().toString();
        String confirmaSenha = txtConfirmaSenha.getText().toString();

        boolean valido = true;
        View focusView = null;

        if (nome.isEmpty()) {
            txtNome.setError(getString(R.string.nome_vazio_erro));
            focusView = txtNome;
            valido = false;
        } else if (login.isEmpty()) {
            txtLogin.setError(getString(R.string.login_vazio_erro));
            focusView = txtLogin;
            valido = false;
        } else if (senha.isEmpty()) {
            txtSenha.setError(getString(R.string.senha_vazio_erro));
            focusView = txtSenha;
            valido = false;
        } else if (confirmaSenha.isEmpty()) {
            txtConfirmaSenha.setError(getString(R.string.confirma_senha_vazio_erro));
            focusView = txtConfirmaSenha;
            valido = false;
        } else if (!senha.equals(confirmaSenha)) {
            txtConfirmaSenha.setError(getString(R.string.confirma_senha_invalida_erro));
            focusView = txtConfirmaSenha;
            valido = false;
        }

        if (!valido) {
            focusView.requestFocus();
        } else {
            View view = this.getCurrentFocus();
            if (view != null) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
            atualizarPerfil(nome, login, senha);
        }
    }

    public void atualizarPerfil(String nome, String login, String senha) {
        progressDialog.setMessage("Carregando...");
        progressDialog.show();

        Usuario usuario = new Usuario();

        usuario.nome = nome;
        usuario.login = login;
        usuario.senha = senha;

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.PUT,
                ApiRoutes.montar(P.autenticacao(), "usuario", P.usuario_id().toString()),
                new JSONObject(usuario.toParams()),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        Gson gson = new Gson();
                        Usuario usuarioResposta = gson.fromJson(response.toString(), Usuario.class);
                        P.setUsuario(usuarioResposta);

                        P.conectarUsuario(true);

                        progressDialog.hide();
                        Snackbar.make(txtNome, "Perfil atualizado com sucesso!", Snackbar.LENGTH_LONG)
                                .setAction("Ok", null)
                                .show();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.hide();
                MinhaeiroErrorHelper.alertar(error, PerfilActivity.this);
            }
        });
        request.setRetryPolicy(MinhaeiroRetryPolicy.getInstance());
        AppController.getInstance().addToRequestQueue(request);
    }

    public void limparErros() {
        txtNome.setError(null);
        txtLogin.setError(null);
        txtSenha.setError(null);
        txtConfirmaSenha.setError(null);
    }

    @Override
    public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
        int id = textView.getId();
        switch (id) {
            case R.id.txtConfirmaSenha:
                this.atualizar(textView);
                return true;
        }
        return false;
    }
}
