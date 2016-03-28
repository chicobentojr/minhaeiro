package br.com.chicobentojr.minhaeiro.utils;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;

import com.google.gson.Gson;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import br.com.chicobentojr.minhaeiro.models.Categoria;
import br.com.chicobentojr.minhaeiro.models.Pessoa;
import br.com.chicobentojr.minhaeiro.models.Usuario;

/**
 * Created by Francisco on 30/01/2016.
 */
public class P {

    public static final class REQUEST {
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

    public static Usuario getUsuarioInstance() {
        if (usuario == null) {
            usuario = new Gson().fromJson(P.obter(USUARIO_JSON),Usuario.class);
        }
        return usuario;
    }

    public static void setUsuario(Usuario usuario) {
        prefs.edit()
                .putString(USUARIO_ID, String.valueOf( usuario.usuario_id))
                .putString(USUARIO_NOME, usuario.nome)
                .putString(USUARIO_LOGIN, usuario.login)
                .putString(USUARIO_AUTENTICACAO, usuario.autenticacao)
                .putString(USUARIO_JSON, new Gson().toJson(usuario))
                .apply();
    }

    public static void inserir(String chave, String valor) {
        prefs.edit().putString(chave, valor).apply();
    }
    public static String obter(String chave) {
        return prefs.getString(chave, "");
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
        return Integer.valueOf(prefs.getString(USUARIO_ID, "0"));
    }
    public static String nome() {
        return prefs.getString(USUARIO_NOME, "");
    }
    public static String login() {
        return prefs.getString(USUARIO_LOGIN, "");
    }
    public static String autenticacao() {
        return prefs.getString(USUARIO_AUTENTICACAO, "");
    }

    public static boolean exportarDados() {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            Usuario usuario = P.getUsuarioInstance();
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
            String filename = usuario.login + " " + dateFormat.format(new Date()) + ".txt";
            String outputString = "";
            File myDir = new File(Environment.getExternalStorageDirectory().toString() + "/PosteroCompany/Minhaeiro");

            myDir.mkdirs();

            outputString = new Gson().toJson(usuario);

            try {
                File file = new File(myDir, filename);
                if (file.exists()) {
                    file.delete();
                }

                FileOutputStream fos = new FileOutputStream(file);

                fos.write(outputString.getBytes());
                fos.flush();
                fos.close();
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
            return true;
        } else {
            return false;
        }
    }

}
