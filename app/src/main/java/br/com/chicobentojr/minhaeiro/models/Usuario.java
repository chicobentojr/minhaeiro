package br.com.chicobentojr.minhaeiro.models;

import com.android.volley.NoConnectionError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import br.com.chicobentojr.minhaeiro.adapters.MovimentacaoAdapter;
import br.com.chicobentojr.minhaeiro.utils.ApiRoutes;
import br.com.chicobentojr.minhaeiro.utils.AppController;
import br.com.chicobentojr.minhaeiro.utils.MinhaeiroErrorHelper;
import br.com.chicobentojr.minhaeiro.utils.P;

public class Usuario implements Serializable {
    public int usuario_id;
    public String nome;
    public String login;
    public String senha;
    public String autenticacao;

    public ArrayList<Pessoa> Pessoa;
    public ArrayList<Movimentacao> Movimentacao;
    public ArrayList<Categoria> Categoria;

    public HashMap<String, String> toParams() {
        HashMap<String, String> params = new HashMap<>();

        params.put("usuario_id", String.valueOf(this.usuario_id));
        params.put("nome", this.nome);
        params.put("login", this.login);
        params.put("senha", this.senha);
        params.put("autenticacao", this.autenticacao);

        return params;
    }

    public static Usuario fromJson(JSONObject jsonObject) throws JSONException {
        Usuario retorno = new Usuario();

        retorno.usuario_id = Integer.parseInt(jsonObject.getString("usuario_id"));
        retorno.nome = jsonObject.getString("nome");
        retorno.login = jsonObject.getString("login");
        retorno.senha = jsonObject.getString("senha");
        retorno.autenticacao = jsonObject.getString("autenticacao");

        return retorno;
    }

    public static interface ObterListener {
        void sucesso(Usuario usuario);

        void erro(VolleyError erro);
    }

    public static void listar(final ObterListener listener) {
        StringRequest request = new StringRequest(
                ApiRoutes.USUARIO.Get(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Usuario usuario = new Gson().fromJson(response, Usuario.class);
                        P.inserir(P.USUARIO_JSON,response);
                        P.inserir(P.USUARIO_ID, String.valueOf(usuario.usuario_id));
                        P.inserir(P.USUARIO_NOME, usuario.nome);
                        P.inserir(P.USUARIO_LOGIN, usuario.login);
                        P.inserir(P.USUARIO_AUTENTICACAO, usuario.autenticacao);
                        listener.sucesso(usuario);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error instanceof NoConnectionError) {
                    listener.sucesso(P.getUsuarioInstance());
                } else {
                    listener.erro(error);
                }
            }
        }
        );
        AppController.getInstance().addToRequestQueue(request);
    }
}
