package br.com.chicobentojr.minhaeiro.models;

import java.io.Serializable;

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
}

