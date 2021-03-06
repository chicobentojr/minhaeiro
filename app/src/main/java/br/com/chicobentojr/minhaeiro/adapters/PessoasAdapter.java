package br.com.chicobentojr.minhaeiro.adapters;

import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.text.method.MovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.util.ArrayList;

import br.com.chicobentojr.minhaeiro.R;
import br.com.chicobentojr.minhaeiro.models.Movimentacao;
import br.com.chicobentojr.minhaeiro.models.Pessoa;
import br.com.chicobentojr.minhaeiro.utils.Extensoes;
import br.com.chicobentojr.minhaeiro.utils.P;

/**
 * Created by Francisco on 27/02/2016.
 */
public class PessoasAdapter extends RecyclerView.Adapter<PessoasAdapter.ViewHolder> {

    private ArrayList<Pessoa> pessoas;

    public PessoasAdapter(ArrayList<Pessoa> pessoas) {
        this.pessoas = pessoas;
    }

    @Override
    public PessoasAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_item_pessoas, parent, false);

        return new ViewHolder(layout);
    }

    @Override
    public void onBindViewHolder(PessoasAdapter.ViewHolder holder, int position) {

        Pessoa pessoa = pessoas.get(position);
        ArrayList<Movimentacao> movimentacoes = Movimentacao.filtrarPorPessoa(P.getUsuarioInstance().Movimentacao,pessoa);

        double saldo = Movimentacao.obterSaldo(movimentacoes);

        if (saldo < 0) {
            holder.lblSaldo.setTextColor(Color.RED);
        } else if (saldo >= 0) {
            holder.lblSaldo.setTextColor(Color.parseColor("#1B5E20"));
        }

        holder.lblNome.setText(pessoa.nome);
        holder.lblSaldo.setText(Extensoes.LAYOUT.valor(saldo));
    }

    @Override
    public int getItemCount() {
        return pessoas.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public View itemView;
        public TextView lblNome;
        public TextView lblSaldo;

        public ViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            lblNome = (TextView) itemView.findViewById(R.id.lblNome);
            lblSaldo = (TextView) itemView.findViewById(R.id.lblSaldo);
        }
    }

}
