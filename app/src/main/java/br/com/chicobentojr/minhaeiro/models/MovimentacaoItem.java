package br.com.chicobentojr.minhaeiro.models;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;

public class MovimentacaoItem implements Serializable {
    public int usuario_id;
    public int movimentacao_id;
    public int item_id;
    public int pessoa_id;
    public String item_data;
    public double valor;
    public String descricao;
    public char tipo;
    public boolean realizada;

    public Pessoa Pessoa;

    public HashMap<String,String> toParams(){
        HashMap<String, String> params = new HashMap<>();

        params.put("usuario_id", String.valueOf(this.usuario_id));
        params.put("movimentacao_id", String.valueOf(this.movimentacao_id));
        params.put("item_id", String.valueOf(this.item_id));
        params.put("pessoa_id", String.valueOf(this.pessoa_id));
        params.put("item_data", String.valueOf(this.item_data));
        params.put("valor", String.valueOf(this.valor));
        params.put("descricao", String.valueOf(this.descricao));
        params.put("tipo", String.valueOf(this.tipo));
        params.put("realizada", String.valueOf(this.realizada));

        return params;
    }
}
