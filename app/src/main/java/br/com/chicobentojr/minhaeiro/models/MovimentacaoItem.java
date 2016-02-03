package br.com.chicobentojr.minhaeiro.models;

import java.io.Serializable;
import java.util.Date;

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
}
