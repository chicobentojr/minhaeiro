package br.com.chicobentojr.minhaeiro.utils;

import android.app.Application;
import android.content.Context;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by Francisco on 30/01/2016.
 */
public class AppController extends Application {

    public static final String TAG = "volleyPatterns";

    private static AppController instance;

    private RequestQueue requestQueue;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }
    public static synchronized AppController getInstance(){
        return instance;
    }
    public RequestQueue getRequestQueue(){
        if(requestQueue == null){
            requestQueue = Volley.newRequestQueue(getApplicationContext());
        }
        return requestQueue;
    }
    public <T> void addToRequestQueue(Request<T> request, String tag){
        request.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(request);
    }
    public <T> void addToRequestQueue(Request<T> request){
        request.setTag(TAG);
        getRequestQueue().add(request);
    }
    public void cancelPendingRequests(Object tag){
        if (requestQueue != null){
            requestQueue.cancelAll(tag);
        }
    }

    public static Context getContext(){
        return AppController.getInstance().getApplicationContext();
    }
}
