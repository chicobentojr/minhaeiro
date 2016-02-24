package br.com.chicobentojr.minhaeiro.fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

import br.com.chicobentojr.minhaeiro.R;
import br.com.chicobentojr.minhaeiro.activity.MainActivity;
import br.com.chicobentojr.minhaeiro.dialogs.DatePicker;
import br.com.chicobentojr.minhaeiro.models.Categoria;
import br.com.chicobentojr.minhaeiro.models.Movimentacao;
import br.com.chicobentojr.minhaeiro.models.Pessoa;
import br.com.chicobentojr.minhaeiro.models.Usuario;
import br.com.chicobentojr.minhaeiro.utils.ApiRoutes;
import br.com.chicobentojr.minhaeiro.utils.AppController;
import br.com.chicobentojr.minhaeiro.utils.Extensoes;
import br.com.chicobentojr.minhaeiro.utils.MinhaeiroErrorHelper;
import br.com.chicobentojr.minhaeiro.utils.P;
import br.com.chicobentojr.minhaeiro.utils.SpinnerHelper;

/**
 * Created by Francisco on 14/02/2016.
 */
public class MovimentacaoDetalheFragment extends Fragment implements DatePicker.DataFornecidaListener {

    private Activity listener;

    private Usuario usuario;
    private Movimentacao movimentacao;
    private int item_posicao;

    private Spinner spnCategoria;
    private Spinner spnPessoa;
    private EditText txtMovimentacaoData;
    private EditText txtMovimentacaoValor;
    private EditText txtDescricao;
    private Spinner spnMovimentacaoTipo;
    private Switch swtRealizada;

    private Button btnAtualizar;
    private ImageButton btnData;

    private ArrayAdapter<Categoria> adpCategoria;
    private ArrayAdapter<Pessoa> adpPessoa;
    private ProgressDialog progressDialog;

    public MovimentacaoDetalheFragment() {

    }

    public static MovimentacaoDetalheFragment newInstance(Activity listener) {
        MovimentacaoDetalheFragment fragment = new MovimentacaoDetalheFragment();
        fragment.listener = listener;
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_movimentacao_detalhe, container, false);
        fragmentView = this.iniciarLayout(fragmentView);
        this.preencherMovimentacao();
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            calendar.setTime(sdf.parse(movimentacao.movimentacao_data));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        this.preencherData(calendar);
        return fragmentView;
    }

    public View iniciarLayout(View view) {

        usuario = P.getUsuario(P.obter(P.USUARIO_JSON));

        adpCategoria = new ArrayAdapter(listener, android.R.layout.simple_spinner_item, usuario.Categoria);
        adpCategoria.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnCategoria = (Spinner) view.findViewById(R.id.spnCategoria);
        spnCategoria.setAdapter(adpCategoria);

        adpPessoa = new ArrayAdapter(listener, android.R.layout.simple_spinner_item, usuario.Pessoa);
        adpPessoa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnPessoa = (Spinner) view.findViewById(R.id.spnPessoa);
        spnPessoa.setAdapter(adpPessoa);

        txtMovimentacaoData = (EditText) view.findViewById(R.id.txtMovimentacaoData);
        txtMovimentacaoValor = (EditText) view.findViewById(R.id.txtMovimentacaoValor);
        txtDescricao = (EditText) view.findViewById(R.id.txtDescricao);
        spnMovimentacaoTipo = (Spinner) view.findViewById(R.id.spnMovimentacaoTipo);
        swtRealizada = (Switch) view.findViewById(R.id.swtRealizada);

        btnAtualizar = (Button) view.findViewById(R.id.btnAtualizar);
        btnAtualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                atualizar(v);
            }
        });

        btnData = (ImageButton) view.findViewById(R.id.btnData);
        btnData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirDatePicker(v);
            }
        });

        progressDialog = new ProgressDialog(listener);
        progressDialog.setCanceledOnTouchOutside(false);

        return view;
    }

    public void atualizar(View v) {
        limparErros();

        int categoria_id = ((Categoria) spnCategoria.getSelectedItem()).categoria_id;
        int pessoa_id = ((Pessoa) spnPessoa.getSelectedItem()).pessoa_id;
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

            movimentacao.categoria_id = categoria_id;
            movimentacao.pessoa_id = pessoa_id;
            movimentacao.movimentacao_data = Extensoes.STRING.toBrazilianDate(movimentacao_data);
            movimentacao.valor = Double.parseDouble(valor);
            movimentacao.descricao = descricao;
            movimentacao.tipo = tipo;
            movimentacao.realizada = realizada;

            atualizarMovimentacao(movimentacao);
        }
    }

    public void atualizarMovimentacao(Movimentacao movimentacao) {
        progressDialog.setMessage("Carregando...");
        progressDialog.show();

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.PUT,
                ApiRoutes.MOVIMENTACAO.Put(movimentacao.movimentacao_id),
                new JSONObject(movimentacao.toParams()),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Gson gson = new Gson();
                        Movimentacao movimentacaoResposta = gson.fromJson(response.toString(), Movimentacao.class);

                        progressDialog.hide();
                        Intent intentResposta = new Intent(getContext(), MainActivity.class);
                        intentResposta.putExtra("movimentacao", movimentacaoResposta);
                        intentResposta.putExtra("item_posicao", item_posicao);
                        listener.setResult(Activity.RESULT_OK, intentResposta);
                        listener.finish();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.hide();
                MinhaeiroErrorHelper.alertar(error, listener);
            }
        });
        AppController.getInstance().addToRequestQueue(request);
    }

    public void limparErros() {
        txtMovimentacaoData.setError(null);
        txtMovimentacaoValor.setError(null);
        txtDescricao.setError(null);
    }

    public void abrirDatePicker(View view) {
        DatePicker datePicker = new DatePicker(this);
        datePicker.show(listener.getFragmentManager(), "DatePicker");
    }

    public void preencherMovimentacao() {
        movimentacao = (Movimentacao) listener.getIntent().getSerializableExtra("movimentacao");
        item_posicao = listener.getIntent().getIntExtra("item_posicao", -1);

        listener.setTitle(movimentacao.descricao);

        txtDescricao.setText(movimentacao.descricao);
        txtMovimentacaoValor.setText(String.valueOf(movimentacao.valor));
        txtMovimentacaoData.setText(Extensoes.LAYOUT.data(movimentacao.movimentacao_data));
        swtRealizada.setChecked(movimentacao.realizada);

        int categoriaIndice = SpinnerHelper.getSelectedItemPosition(spnCategoria, movimentacao.Categoria);
        int pessoaIndice = SpinnerHelper.getSelectedItemPosition(spnPessoa, movimentacao.Pessoa);

        String[] tipos = getResources().getStringArray(R.array.tipo_movimentacao_valor);
        int tipoIndice = Arrays.asList(tipos).indexOf(String.valueOf(movimentacao.tipo));

        spnCategoria.setSelection(categoriaIndice);
        spnPessoa.setSelection(pessoaIndice);
        spnMovimentacaoTipo.setSelection(tipoIndice);
    }

    @Override
    public void preencherData(Calendar calendario) {
        String data = "";
        int dia = calendario.get(Calendar.DAY_OF_MONTH);
        int mes = calendario.get(Calendar.MONTH) + 1;
        int ano = calendario.get(Calendar.YEAR);

        data = String.format("%02d", dia) + "/" + String.format("%02d", mes) + "/" + ano;

        txtMovimentacaoData.setText(data);
    }
}
