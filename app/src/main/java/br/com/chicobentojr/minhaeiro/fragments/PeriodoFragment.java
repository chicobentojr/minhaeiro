package br.com.chicobentojr.minhaeiro.fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;

import br.com.chicobentojr.minhaeiro.R;
import br.com.chicobentojr.minhaeiro.adapters.MovimentacaoAdapter;
import br.com.chicobentojr.minhaeiro.adapters.MovimentacaoItemAdapter;
import br.com.chicobentojr.minhaeiro.models.Movimentacao;
import br.com.chicobentojr.minhaeiro.models.MovimentacaoItem;
import br.com.chicobentojr.minhaeiro.utils.DividerItemDecoration;
import br.com.chicobentojr.minhaeiro.utils.P;

/**
 * Created by Francisco on 23/03/2016.
 */
public class PeriodoFragment extends Fragment {

    private Activity listener;
    private Calendar periodo;
    private ArrayList<Movimentacao> movimentacoes;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;

    public PeriodoFragment() {
    }

    public static PeriodoFragment newInstance(Activity listener, Calendar periodo) {
        PeriodoFragment fragment = new PeriodoFragment();
        fragment.listener = listener;
        fragment.periodo = periodo;
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_periodo, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new DividerItemDecoration(listener, DividerItemDecoration.VERTICAL_LIST));
        recyclerView.setLayoutManager(new LinearLayoutManager(listener));

        movimentacoes = Movimentacao.filtrarPorPeriodo(P.getUsuarioInstance().Movimentacao, periodo);

        adapter = new MovimentacaoAdapter(movimentacoes);
        recyclerView.setAdapter(adapter);

        return view;
    }
}
