package br.com.chicobentojr.minhaeiro.activity;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TableLayout;

import com.android.volley.VolleyError;

import br.com.chicobentojr.minhaeiro.R;
import br.com.chicobentojr.minhaeiro.models.Categoria;
import br.com.chicobentojr.minhaeiro.utils.MinhaeiroErrorHelper;

public class CategoriaEditarActivity extends AppCompatActivity {

    private Categoria categoria;
    private ImageView imgSelecionada;
    private EditText txtNome;
    private TableLayout tblIcones;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.iniciarLayout();
    }

    public void iniciarLayout() {
        setContentView(R.layout.activity_categoria_editar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        progressDialog = new ProgressDialog(this);
        progressDialog.setCanceledOnTouchOutside(false);

        txtNome = (EditText) findViewById(R.id.txtNome);
        tblIcones = (TableLayout) findViewById(R.id.tblIcones);
        categoria = (Categoria) getIntent().getSerializableExtra("categoria");
        this.setTitle(categoria.nome);
        txtNome.setText(categoria.nome);

        if (categoria.icone_id != 0) {
            String tag = getResources().getResourceEntryName(categoria.icone_id);
            View imgEncontrada = obterViewPorTag(tblIcones, tag);
            if (imgEncontrada != null) {
                imgSelecionada = (ImageView) imgEncontrada;
                imgSelecionada.setColorFilter(Color.parseColor("#8BC34A"));
            }
        }
    }

    public void alterarIcone(View view) {
        categoria.icone_id = this.getResources().getIdentifier(view.getTag().toString(), "drawable", this.getPackageName());

        if (imgSelecionada != null) {
            imgSelecionada.setColorFilter(Color.GRAY);
        }
        ((ImageView) view).setColorFilter(Color.parseColor("#8BC34A"));
        imgSelecionada = (ImageView) view;
    }

    public void editarCategoria(View view) {
        String nome = txtNome.getText().toString();
        if (nome.isEmpty()) {
            txtNome.setError(getString(R.string.nome_vazio_erro));
            txtNome.requestFocus();
        } else {
            categoria.nome = nome;
            progressDialog.setMessage("Editando Categoria...");
            progressDialog.show();

            Categoria.editar(categoria, new Categoria.ApiListener() {
                @Override
                public void sucesso(Categoria categoria) {
                    progressDialog.dismiss();
                    finish();
                }

                @Override
                public void erro(VolleyError erro) {
                    progressDialog.dismiss();
                    MinhaeiroErrorHelper.alertar(erro, CategoriaEditarActivity.this);
                }
            });
        }
    }

    public View obterViewPorTag(ViewGroup pai, Object tag) {
        View view = null;
        View filho;
        int qteIcones = pai.getChildCount();
        for (int i = 0; i < qteIcones; i++) {
            filho = pai.getChildAt(i);
            if (filho instanceof ViewGroup) {
                view = obterViewPorTag((ViewGroup) filho, tag);
                if (view != null) {
                    return view;
                }
            }

            if (filho.getTag() != null && filho.getTag().equals(tag)) {
                return filho;
            }

        }
        return view;
    }
}
