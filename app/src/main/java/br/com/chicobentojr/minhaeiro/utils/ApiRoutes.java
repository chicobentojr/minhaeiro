package br.com.chicobentojr.minhaeiro.utils;

/**
 * Created by Francisco on 30/01/2016.
 */
public class ApiRoutes {
    public static final String URL_BASE = "http://minhaeiro.apphb.com/api/";


    public static class USUARIO {
        public static String Entrar() {
            return URL_BASE + "login";
        }

        public static String Post() {
            return ApiRoutes.URL_BASE + "usuario";
        }

        public static String Get() {
            return URL_BASE + P.autenticacao() + "/usuario/" + P.usuario_id();
        }
    }

    public static class CATEGORIA {
        public static String Post() {
            return URL_BASE + P.autenticacao() + "/categoria/" + P.usuario_id();
        }
    }

    public static class PESSOA {
        public static String Post() {
            return URL_BASE + P.autenticacao() + "/pessoa/" + P.usuario_id();
        }

        public static String Put(int pessoa_id) {
            return URL_BASE + P.autenticacao() + "/pessoa/" + P.usuario_id() + "/" + pessoa_id;
        }
    }

    public static class MOVIMENTACAO {
        public static String Post() {
            return "";
        }

        public static String Put(int movimentacao_id) {
            return URL_BASE + P.autenticacao() + "/movimentacao/" + P.usuario_id() + "/" + movimentacao_id;
        }

        public static String Delete(int movimentacao_id) {
            return URL_BASE + P.autenticacao() + "/movimentacao/" + P.usuario_id() + "/" + movimentacao_id;
        }
    }

    public static class MOVIMENTACAO_ITEM {
        public static String Post(int movimentacao_id) {
            return URL_BASE + P.autenticacao() + "/movimentacaoitem/" + P.usuario_id() + "/" + movimentacao_id;
        }

        public static String Put(int movimentacao_id, int item_id) {
            return URL_BASE + P.autenticacao() + "/movimentacaoitem/" + P.usuario_id() + "/" + movimentacao_id + "/" + item_id;
        }

        public static String Delete(int movimentacao_id, int item_id) {
            return URL_BASE + P.autenticacao() + "/movimentacaoitem/" + P.usuario_id() + "/" + movimentacao_id + "/" + item_id;
        }
    }

    public static String montar(String... params) {
        String url = ApiRoutes.URL_BASE;
        for (String parametro : params) {
            url += parametro + "/";
        }
        return url;
    }

    public static String montar(String autenticacao, String controller, Integer usuario_id) {
        return ApiRoutes.URL_BASE + autenticacao + "/" + controller + "/" + usuario_id;
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
}
