package es.gk2.janhout.gk2_android.Estaticas;

import android.os.AsyncTask;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;

public class AsyncTaskGet extends AsyncTask<String,String,String> {

    @Override
    protected String doInBackground(String... params) {
            String response;
            try {
                response = SimpleHttpClient.executeHttpGet(params[0]);
                return response.toString();
            } catch (Exception e) {
                return null;
            }
    }
}
