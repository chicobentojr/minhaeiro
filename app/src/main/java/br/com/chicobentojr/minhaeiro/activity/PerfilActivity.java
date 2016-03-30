package br.com.chicobentojr.minhaeiro.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.VolleyError;

import br.com.chicobentojr.minhaeiro.R;
import br.com.chicobentojr.minhaeiro.models.Usuario;
import br.com.chicobentojr.minhaeiro.utils.MinhaeiroErrorHelper;
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

        Usuario.editar(usuario, new Usuario.ApiListener() {
            @Override
            public void sucesso(Usuario usuario) {
                P.setUsuario(usuario);
                P.conectarUsuario(true);

                progressDialog.hide();
                Snackbar.make(txtNome, "Perfil atualizado com sucesso!", Snackbar.LENGTH_LONG)
                        .setAction("Ok", null)
                        .show();
            }

            @Override
            public void erro(VolleyError erro) {
                progressDialog.hide();
                MinhaeiroErrorHelper.alertar(erro, PerfilActivity.this);
            }
        });
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
