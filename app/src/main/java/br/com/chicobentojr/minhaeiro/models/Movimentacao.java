package br.com.chicobentojr.minhaeiro.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class Movimentacao implements Serializable {
    public int usuario_id;
    public int movimentacao_id;
    public int categoria_id;
    public int pessoa_id;
    public String movimentacao_data;
    public double valor;
    public String descricao;
    public char tipo;
    public boolean realizada;

    public ArrayList<MovimentacaoItem> MovimentacaoItem;
    public Pessoa Pessoa;
    public Categoria Categoria;

    public HashMap<String,String> toParams(){
        HashMap<String,String> params = new HashMap<>();

        params.put("usuario_id",String.valueOf(this.usuario_id));
        params.put("categoria_id",String.valueOf(this.categoria_id));
        params.put("pessoa_id",String.valueOf(this.pessoa_id));
        params.put("movimentacao_data",this.movimentacao_data);
        params.put("valor",String.valueOf(this.valor));
        params.put("descricao",this.descricao);
        params.put("tipo",String.valueOf(this.tipo));
        params.put("realizada",String.valueOf(this.realizada));

        return params;
    }

}
