package br.com.chicobentojr.minhaeiro.adapters;

import android.graphics.Color;
import android.graphics.Paint;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import br.com.chicobentojr.minhaeiro.R;
import br.com.chicobentojr.minhaeiro.models.Movimentacao;
import br.com.chicobentojr.minhaeiro.utils.Extensoes;

/**
 * Created by Francisco on 01/02/2016.
 */
public class MovimentacaoAdapter extends RecyclerView.Adapter<MovimentacaoAdapter.ViewHolder> {

    private ArrayList<Movimentacao> movimentacoes;

    public MovimentacaoAdapter(ArrayList<Movimentacao> movimentacoes) {
        this.movimentacoes = movimentacoes;
    }

    @Override
    public MovimentacaoAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_item_movimentacao, parent, false);

        return new ViewHolder(layout);
    }

    @Override
    public void onBindViewHolder(MovimentacaoAdapter.ViewHolder holder, int position) {

        Movimentacao movimentacao = movimentacoes.get(position);

        holder.lblMovimentacaoDescricao.setText(movimentacao.descricao);
        holder.lblMovimentacaoData.setText(Extensoes.LAYOUT.data(movimentacao.movimentacao_data));
        holder.lblMovimentacaoPessoa.setText(String.valueOf(movimentacao.Pessoa.nome));
        holder.lblMovimentacaoValor.setText(Extensoes.LAYOUT.valor(movimentacao.valor));

        if (movimentacao.realizada) {
            holder.lblMovimentacaoDescricao.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        }
        if (movimentacao.tipo == 'D') {
            holder.lblMovimentacaoValor.setTextColor(Color.RED);
        }
    }

    @Override
    public int getItemCount() {
        return movimentacoes.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView lblMovimentacaoDescricao;
        public TextView lblMovimentacaoData;
        public TextView lblMovimentacaoPessoa;
        public TextView lblMovimentacaoValor;

        public ViewHolder(View itemView) {
            super(itemView);
            lblMovimentacaoDescricao = (TextView) itemView.findViewById(R.id.lblMovimentacaoDescricao);
            lblMovimentacaoData = (TextView) itemView.findViewById(R.id.lblMovimentacaoData);
            lblMovimentacaoPessoa = (TextView) itemView.findViewById(R.id.lblMovimentacaoPessoa);
            lblMovimentacaoValor = (TextView) itemView.findViewById(R.id.lblMovimentacaoValor);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            TextView lblMovimentacaoDescricao = (TextView) v.findViewById(R.id.lblMovimentacaoDescricao);
            Snackbar.make(v, lblMovimentacaoDescricao.getText(), Snackbar.LENGTH_LONG).show();
        }
    }


}
