package es.gk2.janhout.gk2_android.Estaticas;

import android.os.AsyncTask;

public class AsyncTaskGet extends AsyncTask<String,String,String> {

    @Override
    protected String doInBackground(String... params) {
            String response = null;
            try {
                response = SimpleHttpClient.executeHttpGet(params[0]);
                return response.toString();
            } catch (Exception e) {
                return null;
            }
    }
}
