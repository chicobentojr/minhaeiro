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
}

