package br.com.chicobentojr.minhaeiro.utils;

import com.android.volley.DefaultRetryPolicy;

/**
 * Created by Francisco on 31/01/2016.
 */
public class MinhaeiroRetryPolicy extends DefaultRetryPolicy {

    private static final int MY_TIME_OUT = 60000;

    public static DefaultRetryPolicy getInstance(){
        return new DefaultRetryPolicy(MY_TIME_OUT,DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
    }
}
