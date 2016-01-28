package br.com.chicobentojr.minhaeiro.models;

import java.io.Serializable;
import java.util.Date;

public class Movimentacao implements Serializable {
    public int usuario_id;
    public int movimentacao_id;
    public int pessoa_id;
    public Date movimentacao_data;
    public double valor;
    public String observacao;
    public char tipo;
    public boolean realizada;
}
