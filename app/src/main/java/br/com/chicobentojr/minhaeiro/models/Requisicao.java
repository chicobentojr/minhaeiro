package br.com.chicobentojr.minhaeiro.models;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import br.com.chicobentojr.minhaeiro.utils.ApiRoutes;
import br.com.chicobentojr.minhaeiro.utils.P;

/**
 * Created by Francisco on 26/03/2016.
 */
public class Requisicao {

    public int id;
    public int metodo;
    public int modelo;
    public String objeto;

    public static final int MOVIMENTACAO = 1;
    public static final int MOVIMENTACAO_ITEM = 2;
    public static final int CATEGORIA = 3;
    public static final int PESSOA = 4;

    public Requisicao(int metodo, int modelo, String objeto) {
        requisicoes = getRequisicoesInstance();

        this.id = requisicoes.size() > 0 ? requisicoes.get(requisicoes.size() - 1).id + 1 : 1;
        this.metodo = metodo;
        this.modelo = modelo;
        this.objeto = objeto;
    }

    public HashMap<String, String> toParams() {
        HashMap<String, String> params = new HashMap<>();

        params.put("id", String.valueOf(this.id));
        params.put("metodo", String.valueOf(this.metodo));
        params.put("modelo", String.valueOf(this.modelo));
        params.put("objeto", this.objeto);

        return params;
    }

    public static ArrayList<Requisicao> requisicoes;

    private static final String REQUISICAO_JSON = "shared_preference_requisicao_JSON";

    public static ArrayList<Requisicao> getRequisicoesInstance() {
        if (requisicoes == null) {
            String requisicoesJson = P.obter(REQUISICAO_JSON);
            if (!requisicoesJson.equals("")) {
                requisicoes = new ArrayList<Requisicao>(Arrays.asList(new Gson().fromJson(requisicoesJson, Requisicao[].class)));
            } else {
                requisicoes = new ArrayList<Requisicao>();
            }
        }
        return requisicoes;
    }

    public static void adicionar(Requisicao requisicao) {
        requisicoes = getRequisicoesInstance();

        requisicoes.add(requisicao);

        String requisicoesJson = new Gson().toJson(requisicoes);

        P.inserir(REQUISICAO_JSON, requisicoesJson);
    }

    public static void remover(Requisicao requisicao) {
        requisicoes = getRequisicoesInstance();

        requisicoes.remove(requisicao);

        String requisicoesJson = new Gson().toJson(getRequisicoesInstance());

        P.inserir(REQUISICAO_JSON, requisicoesJson);
    }

    public static void limpar(){
        requisicoes = null;
    }

    public static JSONObject obterObjetoJson() {
        JSONObject jsonObject = new JSONObject();
        requisicoes = getRequisicoesInstance();

        if(requisicoes.size() == 1){
            JSONObject objetoJson = new JSONObject(requisicoes.get(0).toParams());

            try {
                jsonObject.put("requisicoes",new JSONArray().put(objetoJson));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return jsonObject;
        }
        for (Requisicao requisicao : requisicoes) {
            JSONObject objetoJson = new JSONObject(requisicao.toParams());
            try {
                jsonObject = jsonObject.accumulate("requisicoes", objetoJson);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return jsonObject;
    }

    public static Request enviar() {
        requisicoes = getRequisicoesInstance();

        if (requisicoes != null && requisicoes.size() > 0) {
            JSONObject objetoJson = Requisicao.obterObjetoJson();

            JsonObjectRequest request = new JsonObjectRequest(
                    Request.Method.POST,
                    ApiRoutes.USUARIO.Sincronizar(),
                    objetoJson,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Usuario usuario = new Gson().fromJson(response.toString(), Usuario.class);
                            P.setUsuario(usuario);
                            P.inserir(REQUISICAO_JSON, "");
                            requisicoes = null;
                        }
                    }, null){
                @Override
                public Priority getPriority() {
                    return Priority.HIGH;
                }
            };
            return request;
        }

        return null;
    }
}
