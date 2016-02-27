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

    public MovimentacaoItem[] MovimentacaoItem;
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

    public static ArrayList<Movimentacao> filtrarPorPessoa(ArrayList<Movimentacao> movimentacoes,Pessoa pessoa){
        ArrayList<Movimentacao> retorno = new ArrayList<>();
        if(movimentacoes != null) {
            for (Movimentacao movimentacao : movimentacoes) {
                if (movimentacao.Pessoa.pessoa_id == pessoa.pessoa_id) {
                    retorno.add(movimentacao);
                }
            }
        }
        return retorno;
    }

    public static ArrayList<Movimentacao> filtrarPorCategoria(ArrayList<Movimentacao> movimentacoes,Categoria categoria){
        ArrayList<Movimentacao> retorno = new ArrayList<>();
        if(movimentacoes != null) {
            for (Movimentacao movimentacao : movimentacoes) {
                if (movimentacao.categoria_id == categoria.categoria_id) {
                    retorno.add(movimentacao);
                }
            }
        }
        return retorno;
    }

    public static double obterSaldo(ArrayList<Movimentacao> movimentacoes){
        double saldo = 0;
        if(movimentacoes != null) {
            for (Movimentacao movimentacao : movimentacoes) {
                if (movimentacao.tipo == 'C') {
                    saldo+=movimentacao.valor;
                }else if(movimentacao.tipo == 'D'){
                    saldo-=movimentacao.valor;
                }
            }
        }
        return saldo;
    }


}
