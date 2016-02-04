package br.com.chicobentojr.minhaeiro.models;

import java.io.Serializable;

/**
 * Created by Francisco on 02/02/2016.
 */
public class Categoria implements Serializable {
    public int usuario_id;
    public int categoria_id;
    public String nome;
    public int icone_id;

    @Override
    public String toString() {
        return this.nome;
    }
}
