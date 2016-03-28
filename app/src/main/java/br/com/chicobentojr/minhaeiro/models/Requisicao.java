package br.com.chicobentojr.minhaeiro.models;

import android.content.Intent;
import android.content.SharedPreferences;

import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

import br.com.chicobentojr.minhaeiro.activity.MainActivity;
import br.com.chicobentojr.minhaeiro.utils.AppController;
import br.com.chicobentojr.minhaeiro.utils.MinhaeiroErrorHelper;
import br.com.chicobentojr.minhaeiro.utils.P;

/**
 * Created by Francisco on 26/03/2016.
 */
public class Requisicao {

    public int id;
    public int requestMethod;
    public String url;
    public int modelo;
    public Object objeto;

    public static final int MOVIMENTACAO = 1;
    public static final int MOVIMENTACAO_ITEM = 2;
    public static final int CATEGORIA = 3;
    public static final int PESSOA = 4;

    public Requisicao(int requestMethod, String url, int modelo, Object objeto) {
        requisicoes = getRequisicoesInstance();

        this.id = requisicoes.size() > 0 ? requisicoes.get(requisicoes.size() - 1).id + 1 : 1;
        this.requestMethod = requestMethod;
        this.url = url;
        this.modelo = modelo;
        this.objeto = objeto;
    }

    public static ArrayList<Requisicao> requisicoes;

    private static SharedPreferences prefs = P.prefs;

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

        prefs.edit()
                .putString(REQUISICAO_JSON, requisicoesJson)
                .apply();
    }

    public static void remover(Requisicao requisicao) {
        requisicoes = getRequisicoesInstance();

        requisicoes.remove(requisicao);

        String requisicoesJson = new Gson().toJson(getRequisicoesInstance());

        prefs.edit()
                .putString(REQUISICAO_JSON, requisicoesJson)
                .apply();
    }

    public static ArrayList<Request> enviar() {
        ArrayList<Request> volleyRequests = new ArrayList<Request>();
        requisicoes = getRequisicoesInstance();

        if (requisicoes != null && requisicoes.size() > 0) {
            ArrayList<Requisicao> lista = requisicoes;

            for (final Requisicao requisicao : lista) {
                JSONObject objetoJson = new JSONObject();

                switch (requisicao.modelo){
                    case Requisicao.MOVIMENTACAO:
                        objetoJson = new JSONObject(((Movimentacao)requisicao.objeto).toParams());
                        break;
                }

                JsonObjectRequest request = new JsonObjectRequest(
                        requisicao.requestMethod,
                        requisicao.url,
                        objetoJson,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                Requisicao.remover(requisicao);
                            }
                        },null);

                volleyRequests.add(request);
            }
        }
        return volleyRequests;
    }
}
