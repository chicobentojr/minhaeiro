package br.com.chicobentojr.minhaeiro.adapters;

import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import br.com.chicobentojr.minhaeiro.R;
import br.com.chicobentojr.minhaeiro.models.Movimentacao;

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
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        SimpleDateFormat sdf2 = new SimpleDateFormat("dd/MM/yyyy");
        Movimentacao movimentacao = movimentacoes.get(position);
        holder.lblMovimentacaoDescricao.setText(movimentacao.descricao);
        try {
            holder.lblMovimentacaoData.setText(sdf2.format(sdf.parse(movimentacao.movimentacao_data)));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        holder.lblMovimentacaoPessoa.setText(String.valueOf(movimentacao.Pessoa.nome));
        holder.lblMovimentacaoValor.setText("R$ "+movimentacao.valor);
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
            Snackbar.make(v,lblMovimentacaoDescricao.getText(),Snackbar.LENGTH_LONG).show();
        }
    }


}
