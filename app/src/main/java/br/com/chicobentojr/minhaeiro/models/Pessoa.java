package br.com.chicobentojr.minhaeiro.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import br.com.chicobentojr.minhaeiro.utils.P;

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

    public static void editar(Pessoa pessoa) {
        Usuario usuario = P.getUsuarioInstance();
        Pessoa p;
        for (int i = 0, qtd = usuario.Pessoa.size(); i < qtd; i++) {
            p = usuario.Pessoa.get(i);
            if (p.pessoa_id == pessoa.pessoa_id) {
                usuario.Pessoa.set(i, pessoa);
                break;
            }
        }
        P.setUsuario(usuario);
    }

    public static void remover(Pessoa pessoa) {
        Usuario usuario = P.getUsuarioInstance();
        Pessoa p;
        for (int i = 0, qtd = usuario.Pessoa.size(); i < qtd; i++) {
            p = usuario.Pessoa.get(i);
            if (p.pessoa_id == pessoa.pessoa_id) {
                usuario.Pessoa.remove(p);
                break;
            }
        }
        P.setUsuario(usuario);
    }
}

