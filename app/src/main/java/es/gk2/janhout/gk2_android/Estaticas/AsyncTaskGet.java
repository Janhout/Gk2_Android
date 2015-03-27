package es.gk2.janhout.gk2_android.Estaticas;

import android.os.AsyncTask;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;

public class AsyncTaskGet extends AsyncTask<String,String,String> {

    private String resp;

    @Override
    protected String doInBackground(String... params) {
            String response = null;
            try {
                response = SimpleHttpClient.executeHttpGet(params[0]);
                String res = response.toString();
                resp = res.replaceAll("\\s+", "");
            } catch (Exception e) {
                e.printStackTrace();
                resp = e.getMessage();
            }
        return resp;
    }
}
