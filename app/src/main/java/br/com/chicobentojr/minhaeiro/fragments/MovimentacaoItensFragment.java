package br.com.chicobentojr.minhaeiro.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.Switch;

import com.android.volley.VolleyError;

import java.util.ArrayList;
import java.util.Arrays;

import br.com.chicobentojr.minhaeiro.R;
import br.com.chicobentojr.minhaeiro.adapters.MovimentacaoItemAdapter;
import br.com.chicobentojr.minhaeiro.models.Movimentacao;
import br.com.chicobentojr.minhaeiro.models.MovimentacaoItem;
import br.com.chicobentojr.minhaeiro.models.Pessoa;
import br.com.chicobentojr.minhaeiro.models.Usuario;
import br.com.chicobentojr.minhaeiro.utils.DividerItemDecoration;
import br.com.chicobentojr.minhaeiro.utils.ItemClickSupport;
import br.com.chicobentojr.minhaeiro.utils.MinhaeiroErrorHelper;
import br.com.chicobentojr.minhaeiro.utils.P;
import br.com.chicobentojr.minhaeiro.utils.SpinnerHelper;

/**
 * Created by Francisco on 14/02/2016.
 */
public class MovimentacaoItensFragment extends Fragment implements PopupMenu.OnMenuItemClickListener {

    private Activity listener;
    private Usuario usuario;
    private ArrayList<MovimentacaoItem> itens;
    private MovimentacaoItem itemSelecionado;
    private int itemPosicao;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    private Spinner spnPessoa;
    private EditText txtMovimentacaoValor;
    private EditText txtDescricao;
    private Spinner spnMovimentacaoTipo;
    private Switch swtRealizada;

    private ArrayAdapter<Pessoa> adpPessoa;

    private ProgressDialog progressDialog;

    private AlertDialog itemDialog;

    public MovimentacaoItensFragment() {

    }

    public static MovimentacaoItensFragment newInstance(Activity listener) {
        MovimentacaoItensFragment fragment = new MovimentacaoItensFragment();
        fragment.listener = listener;
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movimentacao_itens, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new DividerItemDecoration(listener, DividerItemDecoration.VERTICAL_LIST));
        layoutManager = new LinearLayoutManager(listener);
        recyclerView.setLayoutManager(layoutManager);

        usuario = P.getUsuarioInstance();
        progressDialog = new ProgressDialog(listener);
        progressDialog.setCanceledOnTouchOutside(false);

        itens = new ArrayList<MovimentacaoItem>(Arrays.asList(((Movimentacao) listener.getIntent().getSerializableExtra("movimentacao")).MovimentacaoItem));
        adapter = new MovimentacaoItemAdapter(itens);
        recyclerView.setAdapter(adapter);

        definirRecyclerViewItemClicks();

        return view;
    }

    public void definirRecyclerViewItemClicks() {
        ItemClickSupport.addTo(recyclerView).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                itemSelecionado = itens.get(position);
                itemPosicao = position;
                abrirAtualizarItemDialog();
            }
        });
        ItemClickSupport.addTo(recyclerView).setOnItemLongClickListener(new ItemClickSupport.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClicked(RecyclerView recyclerView, int position, View v) {
                itemSelecionado = itens.get(position);

                PopupMenu popupMenu = new PopupMenu(listener, v, Gravity.LEFT);
                popupMenu.setOnMenuItemClickListener(MovimentacaoItensFragment.this);
                popupMenu.inflate(R.menu.popup_movimentacao);
                popupMenu.show();
                return true;
            }
        });
    }

    public void adicionarItemAdapter(MovimentacaoItem item) {
        itens.add(0, item);
        adapter.notifyDataSetChanged();
    }

    public void atualizarItemAdapter(MovimentacaoItem item) {
        itens.set(itemPosicao, item);
        adapter.notifyItemChanged(itemPosicao);
    }

    public void abrirAtualizarItemDialog() {
        itemDialog = new AlertDialog.Builder(listener)
                .setTitle("Atualizar Item")
                .setView(R.layout.dialog_movimentacao_item_cadastro)
                .setPositiveButton("Atualizar", null)
                .setNegativeButton("Cancelar", null)
                .create();

        itemDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                Button positiveButton = itemDialog.getButton(DialogInterface.BUTTON_POSITIVE);
                positiveButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        atualizar(v);
                    }
                });
            }
        });
        itemDialog.show();
        this.iniciarItemDialogLayout(itemDialog);
        this.preencherMovimentacaoItem();
    }

    public void iniciarItemDialogLayout(AlertDialog dialog) {
        adpPessoa = new ArrayAdapter(listener, android.R.layout.simple_spinner_item, usuario.Pessoa);
        adpPessoa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnPessoa = (Spinner) dialog.findViewById(R.id.spnPessoa);
        spnPessoa.setAdapter(adpPessoa);

        txtMovimentacaoValor = (EditText) dialog.findViewById(R.id.txtMovimentacaoValor);
        txtDescricao = (EditText) dialog.findViewById(R.id.txtDescricao);
        spnMovimentacaoTipo = (Spinner) dialog.findViewById(R.id.spnMovimentacaoTipo);
        swtRealizada = (Switch) dialog.findViewById(R.id.swtRealizada);

    }

    public void preencherMovimentacaoItem() {
        txtDescricao.setText(itemSelecionado.descricao);
        txtMovimentacaoValor.setText(String.valueOf(itemSelecionado.valor));
        swtRealizada.setChecked(itemSelecionado.realizada);

        int pessoaIndice = SpinnerHelper.getSelectedItemPosition(spnPessoa, itemSelecionado.Pessoa);

        String[] tipos = getResources().getStringArray(R.array.tipo_movimentacao_valor);
        int tipoIndice = Arrays.asList(tipos).indexOf(String.valueOf(itemSelecionado.tipo));

        spnPessoa.setSelection(pessoaIndice);
        spnMovimentacaoTipo.setSelection(tipoIndice);
    }

    public void atualizar(View v) {
        limparErros();

        int pessoa_id = ((Pessoa) spnPessoa.getSelectedItem()).pessoa_id;
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
        } else if (Double.parseDouble(valor) <= 0) {
            txtMovimentacaoValor.setError(getString(R.string.valor_maior_zero_erro));
            focusView = txtMovimentacaoValor;
            valido = false;
        }

        if (!valido) {
            focusView.requestFocus();
        } else {
            itemSelecionado.pessoa_id = pessoa_id;
            itemSelecionado.valor = Double.parseDouble(valor);
            itemSelecionado.descricao = descricao;
            itemSelecionado.tipo = tipo;
            itemSelecionado.realizada = realizada;

            atualizarMovimentacaoItem(itemSelecionado);
        }
    }

    public void limparErros() {
        txtDescricao.setError(null);
        txtMovimentacaoValor.setError(null);
    }

    public void atualizarMovimentacaoItem(MovimentacaoItem item) {
        progressDialog.setMessage("Carregando...");
        progressDialog.show();

        MovimentacaoItem.editar(item, new MovimentacaoItem.ApiListener() {
            @Override
            public void sucesso(MovimentacaoItem item) {
                atualizarItemAdapter(item);
                progressDialog.dismiss();
                itemDialog.dismiss();
            }

            @Override
            public void erro(VolleyError erro) {
                progressDialog.hide();
                MinhaeiroErrorHelper.alertar(erro, listener);
            }
        });
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.act_excluir:
                this.excluirMovimentacaoItem(itemSelecionado);
                return true;
        }
        return false;
    }

    public void excluirMovimentacaoItem(final MovimentacaoItem item) {
        progressDialog.setMessage("Excluindo Movimentação...");
        progressDialog.show();

        MovimentacaoItem.excluir(item, new MovimentacaoItem.ApiListener() {
            @Override
            public void sucesso(MovimentacaoItem item) {
                itens.remove(itemSelecionado);
                adapter.notifyDataSetChanged();
                progressDialog.hide();
            }

            @Override
            public void erro(VolleyError erro) {
                progressDialog.hide();
                MinhaeiroErrorHelper.alertar(erro, listener);
            }
        });
    }
}
