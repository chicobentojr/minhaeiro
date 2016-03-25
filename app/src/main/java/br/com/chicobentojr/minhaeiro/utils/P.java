package br.com.chicobentojr.minhaeiro.utils;


import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import java.util.ArrayList;

import br.com.chicobentojr.minhaeiro.activity.LoginActivity;
import br.com.chicobentojr.minhaeiro.models.Pessoa;
import br.com.chicobentojr.minhaeiro.models.Usuario;

/**
 * Created by Francisco on 30/01/2016.
 */
public class P {

    public static final class REQUEST{
        public static final int MOVIMENTACAO_CADASTRO = 1;
        public static final int MOVIMENTACAO_ATUALIZACAO = 2;
    }

    public static Usuario usuario = null;

    public static final String PREF_KEY = "br.com.chicobentojr.minhaeiro.shared_preferences";

    public static final String USUARIO_JSON = "shared_preference_usuario_JSON";

    public static final String USUARIO_ID = "shared_preference_usuario_id";
    public static final String USUARIO_NOME = "shared_preference_usuario_nome";
    public static final String USUARIO_LOGIN = "shared_preference_usuario_login";
    public static final String USUARIO_AUTENTICACAO = "shared_preference_autenticacao";
    public static final String USUARIO_CONECTADO = "shared_preference_conectado";

    public static SharedPreferences prefs = AppController.getContext().getSharedPreferences(PREF_KEY, Context.MODE_PRIVATE);

    public static Usuario getUsuarioInstance(){
        if(usuario == null){
            usuario = P.getUsuario(P.obter(P.USUARIO_JSON));
        }
        return usuario;
    }

    public static Usuario getUsuario() {
        Usuario usuario = new Usuario();
        usuario.usuario_id = prefs.getInt(USUARIO_ID, 0);
        usuario.nome = prefs.getString(USUARIO_NOME, "");
        usuario.login = prefs.getString(USUARIO_LOGIN, "");
        usuario.autenticacao = prefs.getString(USUARIO_AUTENTICACAO,"");
        return usuario;
    }
    private static Usuario getUsuario(String json) {
        Usuario usuario = new Usuario();
        Gson gson = new Gson();
        usuario = gson.fromJson(json, Usuario.class);
        return usuario;
    }
    public static void setUsuario(Usuario usuario){
        prefs.edit()
                .putInt(USUARIO_ID, usuario.usuario_id)
                .putString(USUARIO_NOME, usuario.nome)
                .putString(USUARIO_LOGIN,usuario.login)
                .putString(USUARIO_AUTENTICACAO,usuario.autenticacao)
                .putString(USUARIO_JSON,new Gson().toJson(usuario))
                .apply();
    }

    public static void limpar() {
        prefs.edit().clear().apply();
    }

    public static void conectarUsuario(boolean conectar) {
        prefs.edit().putBoolean(USUARIO_CONECTADO, conectar).apply();
    }

    public static boolean usuarioConectado() {
        return prefs.getBoolean(USUARIO_CONECTADO, false);
    }

    public static int usuario_id() {
        return prefs.getInt(USUARIO_ID, 0);
    }

    public static void usuario_id(int usuario_id) {
        prefs.edit().putInt(USUARIO_ID, usuario_id).apply();
    }

    public static String nome() {
        return prefs.getString(USUARIO_NOME, "");
    }

    public static void nome(String nome) {
        prefs.edit().putString(USUARIO_NOME, nome).apply();
    }

    public static String login() {
        return prefs.getString(USUARIO_LOGIN, "");
    }

    public static void login(String login) {
        prefs.edit().putString(USUARIO_LOGIN, login).apply();
    }

    public static String autenticacao() {
        return prefs.getString(USUARIO_AUTENTICACAO, "");
    }

    public static void autenticacao(String autenticacao) {
        prefs.edit().putString(USUARIO_AUTENTICACAO, autenticacao).apply();
    }

    public static void inserir(String chave, String valor) {
        prefs.edit().putString(chave, valor).apply();
    }

    public static String obter(String chave) {
        return prefs.getString(chave, "");
    }

    public static void editarPessoa(Pessoa pessoa){
        usuario = P.getUsuarioInstance();
        Pessoa p;
        for(int i = 0, qtd = usuario.Pessoa.size(); i < qtd; i++){
            p = usuario.Pessoa.get(i);
            if(p.pessoa_id == pessoa.pessoa_id){
                usuario.Pessoa.get(i).nome = pessoa.nome;
                break;
            }
        }
        P.setUsuario(usuario);
    }
    public static void removerPessoa(Pessoa pessoa){
        usuario = P.getUsuarioInstance();
        Pessoa p;
        for(int i = 0, qtd = usuario.Pessoa.size(); i < qtd; i++){
            p = usuario.Pessoa.get(i);
            if(p.pessoa_id == pessoa.pessoa_id){
                usuario.Pessoa.remove(p);
                break;
            }
        }
        P.setUsuario(usuario);
    }

}
