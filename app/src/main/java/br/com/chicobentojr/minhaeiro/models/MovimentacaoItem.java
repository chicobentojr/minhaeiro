package br.com.chicobentojr.minhaeiro.models;

import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import br.com.chicobentojr.minhaeiro.utils.ApiRoutes;
import br.com.chicobentojr.minhaeiro.utils.AppController;
import br.com.chicobentojr.minhaeiro.utils.P;

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

    public HashMap<String, String> toParams() {
        HashMap<String, String> params = new HashMap<>();

        params.put("usuario_id", String.valueOf(this.usuario_id));
        params.put("movimentacao_id", String.valueOf(this.movimentacao_id));
        params.put("item_id", String.valueOf(this.item_id));
        params.put("pessoa_id", String.valueOf(this.pessoa_id));
        //params.put("item_data", String.valueOf(this.item_data));
        params.put("valor", String.valueOf(this.valor));
        params.put("descricao", String.valueOf(this.descricao));
        params.put("tipo", String.valueOf(this.tipo));
        params.put("realizada", String.valueOf(this.realizada));

        return params;
    }

    public static MovimentacaoItem cadastrar(MovimentacaoItem item) {
        Usuario usuario = P.getUsuarioInstance();
        Movimentacao m = Movimentacao.obter(item.movimentacao_id);

        ArrayList<MovimentacaoItem> itens = new ArrayList<MovimentacaoItem>();

        if (m != null && m.MovimentacaoItem != null) {
            itens = new ArrayList<MovimentacaoItem>(Arrays.asList(m.MovimentacaoItem));
        }

        item.item_id = itens.size() > 0 ? itens.get(0).item_id + 1 : 1;

        itens.add(item);

        m.MovimentacaoItem = itens.toArray(new MovimentacaoItem[itens.size()]);

        usuario.Movimentacao.set(usuario.Movimentacao.indexOf(m), m);
        P.setUsuario(usuario);

        return item;
    }

    public static MovimentacaoItem editar(MovimentacaoItem item) {
        Usuario usuario = P.getUsuarioInstance();
        Movimentacao m = Movimentacao.obter(item.movimentacao_id);
        MovimentacaoItem mi = null;

        MovimentacaoItem[] itens = m.MovimentacaoItem;

        for (int i = 0, qtd = itens.length; i < qtd; i++) {
            mi = itens[i];
            if (mi.item_id == item.item_id) {
                itens[i] = item;
                mi = itens[i];
                break;
            }
        }

        m.MovimentacaoItem = itens;
        usuario.Movimentacao.set(usuario.Movimentacao.indexOf(m), m);
        P.setUsuario(usuario);

        return mi;
    }

    public static void excluir(MovimentacaoItem item) {
        Usuario usuario = P.getUsuarioInstance();
        Movimentacao m = Movimentacao.obter(item.movimentacao_id);
        MovimentacaoItem mi = null;

        MovimentacaoItem[] itens = m.MovimentacaoItem;
        MovimentacaoItem[] novosItens = new MovimentacaoItem[itens.length - 1];
        int contador = 0;
        for (int i = 0, qtd = itens.length; i < qtd; i++) {
            mi = itens[i];
            if (mi.item_id != item.item_id) {
                novosItens[contador++] = mi;
            }
        }
        usuario.Movimentacao.set(usuario.Movimentacao.indexOf(m), m);
        P.setUsuario(usuario);
    }

    public interface ApiListener {
        void sucesso(MovimentacaoItem item);

        void erro(VolleyError erro);
    }

    public static void cadastrar(final MovimentacaoItem item, final ApiListener listener) {
        final String url = ApiRoutes.MOVIMENTACAO_ITEM.Post(item.movimentacao_id);
        final int metodo = Request.Method.POST;
        JsonObjectRequest request = new JsonObjectRequest(
                metodo,
                url,
                new JSONObject(item.toParams()),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        MovimentacaoItem resposta = new Gson().fromJson(response.toString(), MovimentacaoItem.class);
                        MovimentacaoItem.cadastrar(item);
                        listener.sucesso(resposta);

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error instanceof NoConnectionError) {
                    MovimentacaoItem i = MovimentacaoItem.cadastrar(item);
                    Requisicao.adicionar(new Requisicao(metodo, Requisicao.MOVIMENTACAO_ITEM, new Gson().toJson(i)));
                    listener.sucesso(i);
                } else {
                    listener.erro(error);
                }
            }
        });
        AppController.getInstance().addToRequestQueue(request);
    }

    public static void editar(final MovimentacaoItem item, final ApiListener listener) {
        final String url = ApiRoutes.MOVIMENTACAO_ITEM.Put(item.movimentacao_id, item.item_id);
        final int metodo = Request.Method.PUT;
        JsonObjectRequest request = new JsonObjectRequest(
                metodo,
                url,
                new JSONObject(item.toParams()),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        MovimentacaoItem resposta = new Gson().fromJson(response.toString(), MovimentacaoItem.class);
                        MovimentacaoItem.editar(item);
                        listener.sucesso(resposta);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error instanceof NoConnectionError) {
                    MovimentacaoItem i = MovimentacaoItem.editar(item);
                    Requisicao.adicionar(new Requisicao(metodo, Requisicao.MOVIMENTACAO_ITEM, new Gson().toJson(i)));
                    listener.sucesso(i);
                } else {
                    listener.erro(error);
                }
            }
        });
        AppController.getInstance().addToRequestQueue(request);
    }

    public static void excluir(final MovimentacaoItem item, final ApiListener listener) {
        final String url = ApiRoutes.MOVIMENTACAO_ITEM.Delete(item.movimentacao_id, item.item_id);
        final int metodo = Request.Method.DELETE;
        StringRequest request = new StringRequest(
                metodo,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        MovimentacaoItem.excluir(item);
                        listener.sucesso(null);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error instanceof NoConnectionError) {
                    MovimentacaoItem.excluir(item);
                    Requisicao.adicionar(new Requisicao(metodo, Requisicao.MOVIMENTACAO_ITEM, new Gson().toJson(item)));
                    listener.sucesso(item);
                } else {
                    listener.erro(error);
                }
            }
        }
        );
        AppController.getInstance().addToRequestQueue(request);
    }
}
