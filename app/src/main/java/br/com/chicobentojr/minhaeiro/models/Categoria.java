package br.com.chicobentojr.minhaeiro.models;

import java.io.Serializable;
import java.util.HashMap;

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

    @Override
    public boolean equals(Object o) {
        Categoria c = (Categoria) o;
        return this.usuario_id == c.usuario_id && this.categoria_id == c.categoria_id;
    }

    public HashMap<String, String> toParams() {
        HashMap<String, String> params = new HashMap<>();

        params.put("usuario_id", String.valueOf(this.usuario_id));
        params.put("categoria_id", String.valueOf(this.categoria_id));
        params.put("nome", String.valueOf(this.nome));
        params.put("icone_id", String.valueOf(this.icone_id));
        return params;
    }

}
