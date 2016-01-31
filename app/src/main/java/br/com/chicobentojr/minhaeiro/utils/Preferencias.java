package br.com.chicobentojr.minhaeiro.utils;


import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Francisco on 30/01/2016.
 */
public class Preferencias {

    public static final String PREF_KEY = "br.com.chicobentojr.minhaeiro.shared_preferences";

    public static final String USUARIO_ID = "shared_preference_usuario_id";
    public static final String USUARIO_NOME = "shared_preference_usuario_nome";
    public static final String AUTENTICACAO = "shared_preference_autenticacao";
    public static final String CONECTADO = "shared_preference_conectado";

    public static SharedPreferences prefs = AppController.getContext().getSharedPreferences(PREF_KEY, Context.MODE_PRIVATE);

    public static void inserir(String chave,String valor){
        prefs.edit().putString(chave,valor).apply();
    }

    public static String obter(String chave){
        return prefs.getString(chave,"");
    }

    public static void limpar(){
        prefs.edit().clear().apply();
    }

    public static void conectarUsuario(boolean conectar){
        prefs.edit().putBoolean(CONECTADO,conectar).apply();
    }

    public static boolean usuarioConectado(){
        return prefs.getBoolean(CONECTADO,false);
    }
}
