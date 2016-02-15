package br.com.chicobentojr.minhaeiro.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import br.com.chicobentojr.minhaeiro.R;
import br.com.chicobentojr.minhaeiro.models.Movimentacao;

/**
 * Created by Francisco on 14/02/2016.
 */
public class MovimentacaoItensFragment extends Fragment {

    private Activity listener;

    public MovimentacaoItensFragment(){

    }
    public static MovimentacaoItensFragment newInstance(Activity listener){
        MovimentacaoItensFragment fragment = new MovimentacaoItensFragment();
        fragment.listener = listener;
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movimentacao_itens, container, false);
        //RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        return view;
    }
}
