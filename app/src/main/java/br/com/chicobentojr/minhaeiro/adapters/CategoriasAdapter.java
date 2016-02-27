package br.com.chicobentojr.minhaeiro.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import br.com.chicobentojr.minhaeiro.R;
import br.com.chicobentojr.minhaeiro.models.Categoria;
import br.com.chicobentojr.minhaeiro.models.Pessoa;

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

        holder.lblDescricao.setText(categoria.nome);
        //holder.lblSaldo.setText(Extensoes.LAYOUT.valor(pessoa.obterSaldo(movimentacoes)));
    }

    @Override
    public int getItemCount() {
        return categorias.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public View itemView;
        public ImageView lblIcone;
        public TextView lblDescricao;
        public TextView lblSaldo;

        public ViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            lblIcone = (ImageView) itemView.findViewById(R.id.lblIcone);
            lblDescricao = (TextView) itemView.findViewById(R.id.lblDescricao);
            lblSaldo = (TextView) itemView.findViewById(R.id.lblSaldo);
        }
    }

}
