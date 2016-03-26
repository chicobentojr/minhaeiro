package br.com.chicobentojr.minhaeiro.adapters;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import br.com.chicobentojr.minhaeiro.R;
import br.com.chicobentojr.minhaeiro.models.Categoria;
import br.com.chicobentojr.minhaeiro.models.Movimentacao;
import br.com.chicobentojr.minhaeiro.models.Pessoa;
import br.com.chicobentojr.minhaeiro.utils.Extensoes;
import br.com.chicobentojr.minhaeiro.utils.P;

/**
 * Created by Francisco on 27/02/2016.
 */
public class CategoriasAdapter extends RecyclerView.Adapter<CategoriasAdapter.ViewHolder> {

    private ArrayList<Categoria> categorias;

    public CategoriasAdapter(ArrayList<Categoria> categorias) {
        this.categorias = categorias;
    }

    @Override
    public CategoriasAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_item_categorias, parent, false);

        return new ViewHolder(layout);
    }

    @Override
    public void onBindViewHolder(CategoriasAdapter.ViewHolder holder, int position) {

        Categoria categoria = categorias.get(position);
        ArrayList<Movimentacao> movimentacoes = Movimentacao.filtrarPorCategoria(P.getUsuarioInstance().Movimentacao, categoria);

        double saldo = Movimentacao.obterSaldo(movimentacoes);

        if (saldo < 0) {
            holder.lblSaldo.setTextColor(Color.RED);
        } else if (saldo >= 0) {
            holder.lblSaldo.setTextColor(Color.parseColor("#1B5E20"));
        }

        holder.lblDescricao.setText(categoria.nome);
        holder.lblSaldo.setText(Extensoes.LAYOUT.valor(saldo));

        if (categoria.icone_id != 0) {
            holder.imgIcone.setImageResource(categoria.icone_id);
        } else {
            holder.imgIcone.setImageResource(R.drawable.categorias_ic_padrao);
        }
    }

    @Override
    public int getItemCount() {
        return categorias.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public View itemView;
        public ImageView imgIcone;
        public TextView lblDescricao;
        public TextView lblSaldo;

        public ViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            imgIcone = (ImageView) itemView.findViewById(R.id.imgIcone);
            lblDescricao = (TextView) itemView.findViewById(R.id.lblDescricao);
            lblSaldo = (TextView) itemView.findViewById(R.id.lblSaldo);
        }
    }

}
