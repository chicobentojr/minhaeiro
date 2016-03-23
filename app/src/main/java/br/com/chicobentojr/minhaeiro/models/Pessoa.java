package br.com.chicobentojr.minhaeiro.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class Pessoa implements Serializable {
    public int usuario_id;
    public int pessoa_id;
    public String nome;

    @Override
    public String toString() {
        return this.nome;
    }

    @Override
    public boolean equals(Object o) {
        Pessoa p = (Pessoa) o;
        return this.usuario_id == p.usuario_id && this.pessoa_id == p.pessoa_id;
    }

    public HashMap<String, String> toParams() {
        HashMap<String, String> params = new HashMap<>();

        params.put("usuario_id", String.valueOf(this.usuario_id));
        params.put("pessoa_id", String.valueOf(this.pessoa_id));
        params.put("nome", String.valueOf(this.nome));
        return params;
    }

    public static ArrayList<Pessoa> filtrarPendentes(ArrayList<Movimentacao> movimentacoes) {
        ArrayList<Pessoa> retorno = new ArrayList<>();
        if (movimentacoes != null) {
            for (Movimentacao movimentacao : movimentacoes) {
                if (!movimentacao.realizada) {
                    if (!retorno.contains(movimentacao.Pessoa)) {
                        retorno.add(movimentacao.Pessoa);
                    }
                }
            }
        }
        return retorno;
    }
}

