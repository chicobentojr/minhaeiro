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

    public static Categoria obter(int categoria_id) {
        Categoria c = new Categoria();
        for (Categoria categoria : P.getUsuarioInstance().Categoria) {
            if (categoria.categoria_id == categoria_id) {
                c = categoria;
                break;
            }
        }
        return c;
    }

    public static Categoria cadastrar(Categoria categoria) {
        Usuario usuario = P.getUsuarioInstance();
        ArrayList<Categoria> categorias = usuario.Categoria;

        categoria.categoria_id = categorias.size() > 0 ? categorias.get(categorias.size() - 1).categoria_id + 1 : 1;
        usuario.Categoria.add(categoria);
        P.setUsuario(usuario);

        return categoria;
    }

    public static Categoria editar(Categoria categoria) {
        Usuario usuario = P.getUsuarioInstance();
        Categoria c = null;
        for (int i = 0, qtd = usuario.Categoria.size(); i < qtd; i++) {
            c = usuario.Categoria.get(i);
            if (c.categoria_id == categoria.categoria_id) {
                usuario.Categoria.set(i, categoria);
                c = usuario.Categoria.get(i);
                break;
            }
        }
        P.setUsuario(usuario);
        return c;
    }

    public static void excluir(Categoria categoria) {
        Usuario usuario = P.getUsuarioInstance();
        Categoria c;
        for (int i = 0, qtd = usuario.Categoria.size(); i < qtd; i++) {
            c = usuario.Categoria.get(i);
            if (c.categoria_id == categoria.categoria_id) {
                usuario.Categoria.remove(c);
                break;
            }
        }
        P.setUsuario(usuario);
    }

    public interface ApiListener {
        void sucesso(Categoria categoria);

        void erro(VolleyError erro);
    }

    public static void cadastrar(final Categoria categoria, final ApiListener listener) {
        String url = ApiRoutes.CATEGORIA.Post();
        final int metodo = Request.Method.POST;
        final JsonObjectRequest request = new JsonObjectRequest(
                metodo,
                url,
                new JSONObject(categoria.toParams()),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Categoria resposta = new Gson().fromJson(response.toString(), Categoria.class);
                        Categoria.cadastrar(categoria);
                        listener.sucesso(resposta);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error instanceof NoConnectionError) {
                            Categoria c = Categoria.cadastrar(categoria);
                            Requisicao.adicionar(new Requisicao(metodo,Requisicao.CATEGORIA,new Gson().toJson(c)));
                            listener.sucesso(c);
                        } else {
                            listener.erro(error);
                        }
                    }
                }
        );
        AppController.getInstance().addToRequestQueue(request);
    }

    public static void editar(final Categoria categoria, final ApiListener listener) {
        String url = ApiRoutes.CATEGORIA.Put(categoria.categoria_id);
        final int metodo = Request.Method.PUT;
        JsonObjectRequest request = new JsonObjectRequest(
                metodo,
                url,
                new JSONObject(categoria.toParams()),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Categoria resposta = new Gson().fromJson(response.toString(), Categoria.class);
                        Categoria.editar(categoria);
                        listener.sucesso(resposta);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error instanceof NoConnectionError) {
                            Categoria c = Categoria.editar(categoria);
                            Requisicao.adicionar(new Requisicao(metodo, Requisicao.CATEGORIA, new Gson().toJson(c)));
                            listener.sucesso(c);
                        } else {
                            listener.erro(error);
                        }
                    }
                }
        );
        AppController.getInstance().addToRequestQueue(request);
    }

    public static void excluir(final Categoria categoria, final ApiListener listener) {
        String url = ApiRoutes.CATEGORIA.Delete(categoria.categoria_id);
        final int metodo = Request.Method.DELETE;
        StringRequest request = new StringRequest(
                metodo,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Categoria resposta = new Gson().fromJson(response, Categoria.class);
                        Categoria.excluir(categoria);
                        listener.sucesso(resposta);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error instanceof NoConnectionError) {
                            Categoria.excluir(categoria);
                            Requisicao.adicionar(new Requisicao(metodo, Requisicao.CATEGORIA, new Gson().toJson(categoria)));
                            listener.sucesso(categoria);
                        } else {
                            listener.erro(error);
                        }
                    }
                }
        );
        AppController.getInstance().addToRequestQueue(request);
    }
}
