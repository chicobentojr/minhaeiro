package br.com.chicobentojr.minhaeiro.models;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.HashMap;

public class Usuario implements Serializable {
    public int usuario_id;
    public String nome;
    public String login;
    public String senha;
    public String autenticacao;

    public HashMap<String,String> toParams(){
        HashMap<String,String> params = new HashMap<>();

        params.put("usuario_id",String.valueOf(this.usuario_id));
        params.put("nome",this.nome);
        params.put("login",this.login);
        params.put("senha",this.senha);
        params.put("autenticacao",this.autenticacao);

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
}
