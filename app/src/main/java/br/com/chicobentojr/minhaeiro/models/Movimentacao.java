package br.com.chicobentojr.minhaeiro.models;

import java.io.Serializable;
import java.util.ArrayList;

public class Movimentacao implements Serializable {
    public int usuario_id;
    public int movimentacao_id;
    public int pessoa_id;
    public String movimentacao_data;
    public double valor;
    public String descricao;
    public char tipo;
    public boolean realizada;

    public ArrayList<MovimentacaoItem> MovimentacaoItem;
    public Pessoa Pessoa;
    public Categoria Categoria;

}
