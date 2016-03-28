package br.com.chicobentojr.minhaeiro.models;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

import br.com.chicobentojr.minhaeiro.utils.P;

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

    public HashMap<String, String> toParams() {
        HashMap<String, String> params = new HashMap<>();

        params.put("usuario_id", String.valueOf(this.usuario_id));
        params.put("categoria_id", String.valueOf(this.categoria_id));
        params.put("pessoa_id", String.valueOf(this.pessoa_id));
        params.put("movimentacao_data", this.movimentacao_data);
        params.put("valor", String.valueOf(this.valor));
        params.put("descricao", this.descricao);
        params.put("tipo", String.valueOf(this.tipo));
        params.put("realizada", String.valueOf(this.realizada));

        return params;
    }

    public static Movimentacao adicionar(Movimentacao movimentacao) {
        Usuario usuario = P.getUsuarioInstance();
        ArrayList<Movimentacao> movimentacoes = usuario.Movimentacao;

        movimentacao.movimentacao_id = movimentacoes.size() > 0 ? movimentacoes.get(movimentacoes.size() - 1).movimentacao_id + 1 : 1;
        usuario.Movimentacao.add(movimentacao);
        P.setUsuario(usuario);

        return movimentacao;
    }

    public static ArrayList<Movimentacao> filtrarPorPessoa(ArrayList<Movimentacao> movimentacoes, Pessoa pessoa) {
        ArrayList<Movimentacao> retorno = new ArrayList<>();
        if (movimentacoes != null) {
            for (Movimentacao movimentacao : movimentacoes) {
                if (movimentacao.Pessoa.pessoa_id == pessoa.pessoa_id) {
                    retorno.add(movimentacao);
                }
            }
        }
        return retorno;
    }

    public static ArrayList<Movimentacao> filtrarPorCategoria(ArrayList<Movimentacao> movimentacoes, Categoria categoria) {
        ArrayList<Movimentacao> retorno = new ArrayList<>();
        if (movimentacoes != null) {
            for (Movimentacao movimentacao : movimentacoes) {
                if (movimentacao.categoria_id == categoria.categoria_id) {
                    retorno.add(movimentacao);
                }
            }
        }
        return retorno;
    }

    public static ArrayList<Movimentacao> filtrarPendentes(ArrayList<Movimentacao> movimentacoes) {
        ArrayList<Movimentacao> retorno = new ArrayList<>();
        if (movimentacoes != null) {
            for (Movimentacao movimentacao : movimentacoes) {
                if (!movimentacao.realizada) {
                    retorno.add(movimentacao);
                }
            }
        }
        return retorno;
    }

    public static ArrayList<Movimentacao> filtrarPendentes(ArrayList<Movimentacao> movimentacoes, Pessoa pessoa) {
        movimentacoes = Movimentacao.filtrarPorPessoa(movimentacoes, pessoa);
        return Movimentacao.filtrarPendentes(movimentacoes);
    }

    public static ArrayList<Movimentacao> filtrarPorPeriodo(ArrayList<Movimentacao> movimentacoes, Calendar periodo) {
        ArrayList<Movimentacao> retorno = new ArrayList<>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        if (movimentacoes != null) {
            for (Movimentacao movimentacao : movimentacoes) {
                try {
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(dateFormat.parse(movimentacao.movimentacao_data));
                    if (calendar.get(Calendar.YEAR) == periodo.get(Calendar.YEAR) && calendar.get(Calendar.MONTH) == periodo.get(Calendar.MONTH)) {
                        retorno.add(movimentacao);
                    }

                } catch (ParseException e) {
                    e.printStackTrace();
                    continue;
                }
            }
        }
        return retorno;
    }

    public static ArrayList<Calendar> obterPeriodos(ArrayList<Movimentacao> movimentacoes) {
        ArrayList<Calendar> retorno = new ArrayList<>();
        ArrayList<Integer[]> periodos = new ArrayList<Integer[]>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        int ano = 0;
        int mes = 0;
        boolean valido = true;
        for (Movimentacao movimentacao : movimentacoes) {
            try {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(dateFormat.parse(movimentacao.movimentacao_data));
                ano = calendar.get(Calendar.YEAR);
                mes = calendar.get(Calendar.MONTH);
                for (Integer[] periodo : periodos) {
                    if (periodo[0] == ano && periodo[1] == mes) {
                        valido = false;
                        break;
                    }
                }
                if (valido) {
                    periodos.add(new Integer[]{ano, mes});
                    retorno.add(calendar);
                }
                valido = true;

            } catch (ParseException e) {
                e.printStackTrace();
                continue;
            }
        }

        Collections.sort(retorno, new Comparator<Calendar>() {
            @Override
            public int compare(Calendar lhs, Calendar rhs) {
                int flag = 0;
                if (lhs.getTimeInMillis() < rhs.getTimeInMillis()) {
                    flag = -1;
                } else {
                    if (lhs.getTimeInMillis() > rhs.getTimeInMillis()) {
                        flag = 1;
                    }
                }
                return flag;
            }
        });

        return retorno;
    }

    public static double obterSaldo(ArrayList<Movimentacao> movimentacoes) {
        double saldo = 0;
        if (movimentacoes != null) {
            for (Movimentacao movimentacao : movimentacoes) {
                if (movimentacao.tipo == 'R') {
                    saldo += movimentacao.valor;
                } else if (movimentacao.tipo == 'D') {
                    saldo -= movimentacao.valor;
                }
            }
        }
        return saldo;
    }


}
