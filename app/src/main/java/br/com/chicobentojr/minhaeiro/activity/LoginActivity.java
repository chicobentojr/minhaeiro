package br.com.chicobentojr.minhaeiro.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import br.com.chicobentojr.minhaeiro.R;
import br.com.chicobentojr.minhaeiro.models.Usuario;
import br.com.chicobentojr.minhaeiro.utils.ApiRoutes;
import br.com.chicobentojr.minhaeiro.utils.AppController;
import br.com.chicobentojr.minhaeiro.utils.MinhaeiroErrorHelper;
import br.com.chicobentojr.minhaeiro.utils.MinhaeiroRetryPolicy;
import br.com.chicobentojr.minhaeiro.utils.Preferencias;

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

    public void entrar(View v){
        limparErros();

        String login = txtLogin.getText().toString();
        String senha = txtSenha.getText().toString();

        boolean valido = true;
        View focusView = null;

        if(login.isEmpty()){
            txtLogin.setError(getString(R.string.login_vazio_erro));
            focusView = txtLogin;
            valido = false;
        }
        else if(senha.isEmpty()){
            txtSenha.setError(getString(R.string.senha_vazio_erro));
            focusView = txtSenha;
            valido = false;
        }

        if (!valido) {
            focusView.requestFocus();
        } else {
            realizarLogin(login, senha);
        }
    }

    public void realizarLogin(String login, String senha) {
        progressDialog.setMessage("Carregando...");
        progressDialog.show();

        final Usuario usuario = new Usuario();

        usuario.login = login;
        usuario.senha = senha;

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                ApiRoutes.Usuario.ENTRAR,
                new JSONObject(usuario.toParams()),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Preferencias.inserir(Preferencias.USUARIO_ID, response.getString("usuario_id"));
                            Preferencias.inserir(Preferencias.USUARIO_NOME, response.getString("nome"));
                            Preferencias.inserir(Preferencias.AUTENTICACAO, response.getString("autenticacao"));
                            Preferencias.conectarUsuario(true);

                            progressDialog.hide();
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.hide();
                MinhaeiroErrorHelper.alertar(error,LoginActivity.this);
            }
        });
        request.setRetryPolicy(MinhaeiroRetryPolicy.getInstance());
        AppController.getInstance().addToRequestQueue(request);
    }

    public void abrirCadastro(View v) {
        startActivity(new Intent(this, CadastroActivity.class));
    }

    public void limparErros(){
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


