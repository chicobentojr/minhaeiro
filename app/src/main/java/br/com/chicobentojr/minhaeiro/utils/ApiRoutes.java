package br.com.chicobentojr.minhaeiro.utils;

/**
 * Created by Francisco on 30/01/2016.
 */
public class ApiRoutes {
    public static final String URL_BASE = "http://minhaeiro.apphb.com/api/";

    public static String montar(String... params) {
        String url = ApiRoutes.URL_BASE;
        for (String parametro : params) {
            url += parametro + "/";
        }
        return url;
    }
    public static String montar(String autenticacao, String controller, Integer usuario_id) {
        return ApiRoutes.URL_BASE  + autenticacao + "/" + controller + "/" + usuario_id;
    }
    public static String montar(String autenticacao, String controller, Integer usuario_id, Integer... params) {
        String url = ApiRoutes.URL_BASE + autenticacao + "/" + controller + "/" + usuario_id;
        for (Integer parametro : params) {
            url += "/" + parametro;
        }
        return url;
    }
    public static String montar(String autenticacao, String controller, String usuario_id, String... params) {
        String url = ApiRoutes.URL_BASE + autenticacao + "/" + controller + "/" + usuario_id;
        for (String parametro : params) {
            url += "/" + parametro;
        }
        return url;
    }

    public interface Usuario {
        String ENTRAR = ApiRoutes.URL_BASE + "login";
        String CADASTRAR = ApiRoutes.URL_BASE + "usuario";
    }
}
