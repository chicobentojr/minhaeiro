package br.com.chicobentojr.minhaeiro.adapters;

import android.content.res.ColorStateList;
import android.graphics.AvoidXfermode;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
            holder.itemView.setAlpha(0.5f);
            holder.imgMovimentacaoIcone.setColorFilter(Color.parseColor("#B6B6B6")) ;
        } else {
            holder.itemView.setAlpha(1f);
            holder.imgMovimentacaoIcone.setColorFilter(Color.parseColor("#8BC34A"));
        }
        if (movimentacao.tipo == 'D') {
            holder.lblMovimentacaoValor.setTextColor(Color.RED);
        } else if (movimentacao.tipo == 'R') {
            holder.lblMovimentacaoValor.setTextColor(Color.parseColor("#1B5E20"));
        }
    }

    @Override
    public int getItemCount() {
        return movimentacoes.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public View itemView;
        public TextView lblMovimentacaoDescricao;
        public TextView lblMovimentacaoData;
        public TextView lblMovimentacaoPessoa;
        public TextView lblMovimentacaoValor;
        public ImageView imgMovimentacaoIcone;

        public ViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            lblMovimentacaoDescricao = (TextView) itemView.findViewById(R.id.lblMovimentacaoDescricao);
            lblMovimentacaoData = (TextView) itemView.findViewById(R.id.lblMovimentacaoData);
            lblMovimentacaoPessoa = (TextView) itemView.findViewById(R.id.lblMovimentacaoPessoa);
            lblMovimentacaoValor = (TextView) itemView.findViewById(R.id.lblMovimentacaoValor);
            imgMovimentacaoIcone = (ImageView) itemView.findViewById(R.id.imgMovimentacaoIcon);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            TextView lblMovimentacaoDescricao = (TextView) v.findViewById(R.id.lblMovimentacaoDescricao);
            Snackbar.make(v, lblMovimentacaoDescricao.getText(), Snackbar.LENGTH_LONG).show();
        }
    }


}
