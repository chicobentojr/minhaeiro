package br.com.chicobentojr.minhaeiro.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.VolleyError;

import br.com.chicobentojr.minhaeiro.R;
import br.com.chicobentojr.minhaeiro.models.Usuario;
import br.com.chicobentojr.minhaeiro.utils.MinhaeiroErrorHelper;
import br.com.chicobentojr.minhaeiro.utils.P;

public class CadastroActivity extends AppCompatActivity implements TextView.OnEditorActionListener {

    private EditText txtNome;
    private EditText txtLogin;
    private EditText txtSenha;
    private EditText txtConfirmaSenha;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        txtNome = (EditText) findViewById(R.id.txtNome);
        txtLogin = (EditText) findViewById(R.id.txtLogin);
        txtSenha = (EditText) findViewById(R.id.txtSenha);
        txtConfirmaSenha = (EditText) findViewById(R.id.txtConfirmaSenha);

        txtConfirmaSenha.setOnEditorActionListener(this);

        progressDialog = new ProgressDialog(this);
        progressDialog.setCanceledOnTouchOutside(false);
    }

    public void cadastrar(View v) {
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
            Usuario usuario = new Usuario();
            usuario.nome = nome;
            usuario.login = login;
            usuario.senha = senha;

            realizarCadastro(usuario);
        }
    }

    public void realizarCadastro(Usuario usuario) {
        progressDialog.setMessage("Carregando...");
        progressDialog.show();

        Usuario.cadastrar(usuario, new Usuario.ApiListener() {
            @Override
            public void sucesso(Usuario usuario) {
                P.setUsuario(usuario);
                P.conectarUsuario(true);

                progressDialog.hide();
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }

            @Override
            public void erro(VolleyError erro) {
                progressDialog.hide();
                MinhaeiroErrorHelper.alertar(erro, CadastroActivity.this);
            }
        });
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
                this.cadastrar(textView);
                return true;
        }
        return false;
    }
}
