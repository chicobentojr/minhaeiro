package br.com.chicobentojr.minhaeiro.utils;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;

/**
 * Created by Francisco on 31/01/2016.
 */
public class MinhaeiroErrorHelper {

    private static final String DIALOG_TITLE = "Operação falhou";

    private static final String TIME_OUT = "O tempo da requisição foi esgotado!";
    private static final String NO_CONNECTION = "Verifique sua conexão com a Internet";
    private static final String AUTH_FAILURE = "A autenticação falhou";
    private static final String SERVER = "Ocorreu um erro no Servidor";
    private static final String NETWORK = "Ocorreu um erro na sua conexão";
    private static final String PARSE = "Ocorreu um erro na conversão";
    private static final String DEFAULT = "Ocorreu um erro na operação";

    public static String getMessage(VolleyError error){
        if (error instanceof TimeoutError) {
            return TIME_OUT;
        } else if(error instanceof NoConnectionError) {
            return NO_CONNECTION;
        } else if (error instanceof AuthFailureError) {
            return AUTH_FAILURE;
        } else if (error instanceof ServerError) {
            return SERVER;
        } else if (error instanceof NetworkError) {
            return NETWORK;
        } else if (error instanceof ParseError) {
            return PARSE;
        } else{
            return DEFAULT;
        }
    }

    public static void alertar(VolleyError error,Context context){
        String mensagem = MinhaeiroErrorHelper.getMessage(error);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(DIALOG_TITLE)
                .setMessage(mensagem)
                .setPositiveButton("Ok",null)
                .create()
                .show();
    }
}
