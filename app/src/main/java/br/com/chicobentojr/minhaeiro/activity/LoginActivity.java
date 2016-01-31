package br.com.chicobentojr.minhaeiro.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import org.json.JSONException;
import org.json.JSONObject;
import br.com.chicobentojr.minhaeiro.R;
import br.com.chicobentojr.minhaeiro.utils.ApiRoutes;
import br.com.chicobentojr.minhaeiro.utils.AppController;
import br.com.chicobentojr.minhaeiro.utils.Preferencias;
import br.com.chicobentojr.minhaeiro.models.Usuario;

public class LoginActivity extends AppCompatActivity {

    private EditText txtLogin;
    private EditText txtSenha;
    private View progressView;
    private View loginFormView;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        txtLogin = (EditText) findViewById(R.id.txtLogin);
        txtSenha = (EditText) findViewById(R.id.txtSenha);

        progressDialog = new ProgressDialog(this);
//        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
//            @Override
//            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
//                if (id == R.id.login || id == EditorInfo.IME_NULL) {
//                    attemptLogin();
//                    return true;
//                }
//                return false;
//            }
//        });

        progressView = findViewById(R.id.login_progress);
        loginFormView = findViewById(R.id.login_form);
    }

    public void entrar(View v){
        progressDialog.setMessage("Carregando...");
        progressDialog.show();

        final Usuario usuario = new Usuario();

        usuario.login = txtLogin.getText().toString();
        usuario.senha = txtSenha.getText().toString();

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
                },  new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.hide();
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
        request.setRetryPolicy(new DefaultRetryPolicy(60000,DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(request);
    }

    public void cadastrar(View v) {
        progressDialog.setMessage("Carregando...");
        progressDialog.show();

        final Usuario usuario = new Usuario();

        usuario.nome = txtLogin.getText().toString();
        usuario.login = txtLogin.getText().toString();
        usuario.senha = txtSenha.getText().toString();

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                ApiRoutes.Usuario.CADASTRAR,
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
                },new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.hide();
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                    }
            });
        request.setRetryPolicy(new DefaultRetryPolicy(60000,DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(request);
    }
}


