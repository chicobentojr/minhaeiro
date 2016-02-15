package br.com.chicobentojr.minhaeiro.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Arrays;

import br.com.chicobentojr.minhaeiro.R;
import br.com.chicobentojr.minhaeiro.adapters.MovimentacaoItemAdapter;
import br.com.chicobentojr.minhaeiro.models.Movimentacao;
import br.com.chicobentojr.minhaeiro.models.MovimentacaoItem;
import br.com.chicobentojr.minhaeiro.utils.DividerItemDecoration;

/**
 * Created by Francisco on 14/02/2016.
 */
public class MovimentacaoItensFragment extends Fragment {

    private Activity listener;
    private ArrayList<MovimentacaoItem> itens;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

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

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new DividerItemDecoration(listener, DividerItemDecoration.VERTICAL_LIST));
        layoutManager = new LinearLayoutManager(listener);
        recyclerView.setLayoutManager(layoutManager);

        itens = new ArrayList<MovimentacaoItem>(Arrays.asList(((Movimentacao) listener.getIntent().getSerializableExtra("movimentacao")).MovimentacaoItem));
        adapter = new MovimentacaoItemAdapter(itens);
        recyclerView.setAdapter(adapter);

        return view;
    }
}
