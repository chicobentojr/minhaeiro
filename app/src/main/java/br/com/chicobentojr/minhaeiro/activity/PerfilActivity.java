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
    private EditText txtNovaSenha;
    private EditText txtConfirmaSenha;
    private ProgressDialog progressDialog;

    private Usuario usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        txtNome = (EditText) findViewById(R.id.txtNome);
        txtLogin = (EditText) findViewById(R.id.txtLogin);
        txtNovaSenha = (EditText) findViewById(R.id.txtNovaSenha);
        txtConfirmaSenha = (EditText) findViewById(R.id.txtConfirmaSenha);

        txtConfirmaSenha.setOnEditorActionListener(this);

        progressDialog = new ProgressDialog(this);
        progressDialog.setCanceledOnTouchOutside(false);

        usuario = P.getUsuarioInstance();

        txtNome.setText(usuario.nome);
        txtLogin.setText(usuario.login);
    }

    public void atualizar(View v) {
        limparErros();

        String nome = txtNome.getText().toString();
        String login = txtLogin.getText().toString();
        String novaSenha = txtNovaSenha.getText().toString();
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
        } else if (novaSenha.isEmpty()) {
            txtNovaSenha.setError(getString(R.string.nova_senha_vazio_erro));
            focusView = txtNovaSenha;
            valido = false;
        } else if (confirmaSenha.isEmpty()) {
            txtConfirmaSenha.setError(getString(R.string.confirma_senha_vazio_erro));
            focusView = txtConfirmaSenha;
            valido = false;
        } else if (!novaSenha.equals(confirmaSenha)) {
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
            Usuario usuario = new Usuario();
            usuario.nome = nome;
            usuario.login = login;
            usuario.senha = novaSenha;

            atualizarPerfil(usuario);
        }
    }

    public void atualizarPerfil(Usuario usuario) {
        progressDialog.setMessage("Carregando...");
        progressDialog.show();
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.PUT,
                ApiRoutes.montar(P.autenticacao(), "usuario", P.usuario_id()),
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
        AppController.getInstance().addToRequestQueue(request);
    }

    public void limparErros() {
        txtNome.setError(null);
        txtLogin.setError(null);
        txtNovaSenha.setError(null);
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
