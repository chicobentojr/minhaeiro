package br.com.chicobentojr.minhaeiro.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
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

public class LoginActivity extends AppCompatActivity implements TextView.OnEditorActionListener {

    private EditText txtLogin;
    private EditText txtSenha;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        txtLogin = (EditText) findViewById(R.id.txtLogin);
        txtSenha = (EditText) findViewById(R.id.txtSenha);

        txtSenha.setOnEditorActionListener(this);

        progressDialog = new ProgressDialog(this);
        progressDialog.setCanceledOnTouchOutside(false);
    }

    public void entrar(View v) {
        limparErros();

        String login = txtLogin.getText().toString();
        String senha = txtSenha.getText().toString();

        boolean valido = true;
        View focusView = null;

        if (login.isEmpty()) {
            txtLogin.setError(getString(R.string.login_vazio_erro));
            focusView = txtLogin;
            valido = false;
        } else if (senha.isEmpty()) {
            txtSenha.setError(getString(R.string.senha_vazio_erro));
            focusView = txtSenha;
            valido = false;
        }

        if (!valido) {
            focusView.requestFocus();
        } else {
            Usuario usuario = new Usuario();
            usuario.login = login;
            usuario.senha = senha;

            realizarLogin(usuario);
        }
    }

    public void realizarLogin(Usuario usuario) {
        progressDialog.setMessage("Carregando...");
        progressDialog.show();
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                ApiRoutes.USUARIO.Entrar(),
                new JSONObject(usuario.toParams()),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Gson gson = new Gson();
                        Usuario usuarioResposta = gson.fromJson(response.toString(), Usuario.class);
                        P.setUsuario(usuarioResposta);
                        P.inserir(P.USUARIO_JSON,response.toString());
                        P.conectarUsuario(true);

                        progressDialog.hide();
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.hide();
                MinhaeiroErrorHelper.alertar(error, LoginActivity.this);
            }
        });
        request.setRetryPolicy(MinhaeiroRetryPolicy.getInstance());
        AppController.getInstance().addToRequestQueue(request);
    }

    public void abrirCadastro(View v) {
        startActivity(new Intent(this, CadastroActivity.class));
    }

    public void limparErros() {
        txtLogin.setError(null);
        txtSenha.setError(null);
    }

    @Override
    public void onBackPressed() {
        finish();
        System.exit(0);
    }

    @Override
    public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
        int id = textView.getId();
        switch (id) {
            case R.id.txtSenha:
                this.entrar(textView);
                return true;
        }
        return false;
    }
}


