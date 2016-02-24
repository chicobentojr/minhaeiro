package br.com.chicobentojr.minhaeiro.adapters;

import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import br.com.chicobentojr.minhaeiro.R;
import br.com.chicobentojr.minhaeiro.models.MovimentacaoItem;
import br.com.chicobentojr.minhaeiro.utils.Extensoes;

/**
 * Created by Francisco on 15/02/2016.
 */
public class MovimentacaoItemAdapter extends RecyclerView.Adapter<MovimentacaoItemAdapter.ViewHolder> {

    private ArrayList<MovimentacaoItem> itens;

    public MovimentacaoItemAdapter(ArrayList<MovimentacaoItem> itens) {
        this.itens = itens;
    }

    @Override
    public MovimentacaoItemAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_item_movimentacao_item, parent, false);

        return new ViewHolder(layout);
    }

    @Override
    public void onBindViewHolder(MovimentacaoItemAdapter.ViewHolder holder, int position) {

        MovimentacaoItem item = itens.get(position);

        holder.lblMovimentacaoDescricao.setText(item.descricao);
        holder.lblMovimentacaoData.setText(Extensoes.LAYOUT.data(item.item_data));
        holder.lblMovimentacaoPessoa.setText(String.valueOf(item.Pessoa.nome));
        holder.lblMovimentacaoValor.setText(Extensoes.LAYOUT.valor(item.valor));

        if (item.realizada) {
            holder.itemView.setAlpha(0.5f);
        } else {
            holder.itemView.setAlpha(1f);
        }
        if (item.tipo == 'D') {
            holder.lblMovimentacaoValor.setTextColor(Color.RED);
        } else if (item.tipo == 'C') {
            holder.lblMovimentacaoValor.setTextColor(Color.parseColor("#1B5E20"));
        }
    }

    @Override
    public int getItemCount() {
        return itens.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public View itemView;
        public TextView lblMovimentacaoDescricao;
        public TextView lblMovimentacaoData;
        public TextView lblMovimentacaoPessoa;
        public TextView lblMovimentacaoValor;

        public ViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
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
