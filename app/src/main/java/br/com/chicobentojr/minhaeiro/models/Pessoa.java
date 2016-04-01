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
import java.util.HashMap;

import br.com.chicobentojr.minhaeiro.utils.ApiRoutes;
import br.com.chicobentojr.minhaeiro.utils.AppController;
import br.com.chicobentojr.minhaeiro.utils.P;

public class Pessoa implements Serializable {
    public int usuario_id;
    public int pessoa_id;
    public String nome;

    @Override
    public String toString() {
        return this.nome;
    }

    @Override
    public boolean equals(Object o) {
        Pessoa p = (Pessoa) o;
        return this.usuario_id == p.usuario_id && this.pessoa_id == p.pessoa_id;
    }

    public HashMap<String, String> toParams() {
        HashMap<String, String> params = new HashMap<>();

        params.put("usuario_id", String.valueOf(this.usuario_id));
        params.put("pessoa_id", String.valueOf(this.pessoa_id));
        params.put("nome", String.valueOf(this.nome));
        return params;
    }

    public static ArrayList<Pessoa> filtrarPendentes(ArrayList<Movimentacao> movimentacoes) {
        ArrayList<Pessoa> retorno = new ArrayList<>();
        if (movimentacoes != null) {
            for (Movimentacao movimentacao : movimentacoes) {
                if (!movimentacao.realizada) {
                    if (!retorno.contains(movimentacao.Pessoa)) {
                        retorno.add(movimentacao.Pessoa);
                    }
                }
            }
        }
        return retorno;
    }

    public static Pessoa obter(int pessoa_id) {
        Pessoa p = new Pessoa();
        for (Pessoa pessoa : P.getUsuarioInstance().Pessoa) {
            if (pessoa.pessoa_id == pessoa_id) {
                p = pessoa;
                break;
            }
        }
        return p;
    }

    public static Pessoa cadastrar(Pessoa pessoa) {
        Usuario usuario = P.getUsuarioInstance();
        ArrayList<Pessoa> pessoas = usuario.Pessoa;

        pessoa.pessoa_id = pessoas.size() > 0 ? pessoas.get(pessoas.size() - 1).pessoa_id + 1 : 1;
        usuario.Pessoa.add(pessoa);
        P.setUsuario(usuario);

        return pessoa;
    }

    public static Pessoa editar(Pessoa pessoa) {
        Usuario usuario = P.getUsuarioInstance();
        Pessoa p = null;
        for (int i = 0, qtd = usuario.Pessoa.size(); i < qtd; i++) {
            p = usuario.Pessoa.get(i);
            if (p.pessoa_id == pessoa.pessoa_id) {
                usuario.Pessoa.set(i, pessoa);
                p = usuario.Pessoa.get(i);
                break;
            }
        }
        P.setUsuario(usuario);

        return p;
    }

    public static void excluir(Pessoa pessoa) {
        Usuario usuario = P.getUsuarioInstance();
        Pessoa p;
        for (int i = 0, qtd = usuario.Pessoa.size(); i < qtd; i++) {
            p = usuario.Pessoa.get(i);
            if (p.pessoa_id == pessoa.pessoa_id) {
                usuario.Pessoa.remove(p);
                break;
            }
        }
        P.setUsuario(usuario);
    }

    public interface ApiListener {
        void sucesso(Pessoa pessoa);

        void erro(VolleyError erro);
    }

    public static void excluir(final Pessoa pessoa, final ApiListener listener) {
        String url = ApiRoutes.PESSOA.Delete(pessoa.pessoa_id);
        final int metodo = Request.Method.DELETE;
        StringRequest request = new StringRequest(
                metodo,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Pessoa resposta = new Gson().fromJson(response, Pessoa.class);
                        listener.sucesso(resposta);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error instanceof NoConnectionError) {
                            Pessoa.excluir(pessoa);
                            Requisicao.adicionar(new Requisicao(metodo, Requisicao.PESSOA, new Gson().toJson(pessoa)));
                            listener.sucesso(pessoa);
                        } else {
                            listener.erro(error);
                        }
                    }
                }
        );
        AppController.getInstance().addToRequestQueue(request);
    }

    public static void cadastrar(final Pessoa pessoa, final ApiListener listener) {
        String url = ApiRoutes.PESSOA.Post();
        final int metodo = Request.Method.POST;
        JsonObjectRequest request = new JsonObjectRequest(
                metodo,
                url,
                new JSONObject(pessoa.toParams()),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Pessoa resposta = new Gson().fromJson(response.toString(), Pessoa.class);
                        listener.sucesso(resposta);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error instanceof NoConnectionError) {
                            Pessoa p = Pessoa.cadastrar(pessoa);
                            Requisicao.adicionar(new Requisicao(metodo, Requisicao.PESSOA, new Gson().toJson(p)));
                            listener.sucesso(p);
                        } else {
                            listener.erro(error);
                        }
                    }
                }
        );
        AppController.getInstance().addToRequestQueue(request);
    }

    public static void editar(final Pessoa pessoa, final ApiListener listener) {
        String url = ApiRoutes.PESSOA.Put(pessoa.pessoa_id);
        final int metodo = Request.Method.PUT;
        JsonObjectRequest request = new JsonObjectRequest(
                metodo,
                url,
                new JSONObject(pessoa.toParams()),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Pessoa resposta = new Gson().fromJson(response.toString(), Pessoa.class);
                        listener.sucesso(resposta);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error instanceof NoConnectionError) {
                            Pessoa p = Pessoa.editar(pessoa);
                            Requisicao.adicionar(new Requisicao(metodo, Requisicao.PESSOA, new Gson().toJson(p)));
                            listener.sucesso(p);
                        } else {
                            listener.erro(error);
                        }
                    }
                }
        );
        AppController.getInstance().addToRequestQueue(request);
    }

}

