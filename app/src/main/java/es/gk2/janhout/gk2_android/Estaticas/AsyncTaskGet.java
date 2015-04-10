package es.gk2.janhout.gk2_android.Estaticas;

import android.os.AsyncTask;

public class AsyncTaskGet extends AsyncTask<String,String,String> {

    @Override
    protected String doInBackground(String... params) {
            String response;
            try {
                if(params[1].equals("fichero")) {
                    response = SimpleHttpClient.executeHttpGetb(params[0]);
                } else {
                    response = SimpleHttpClient.executeHttpGet(params[0]);
                }
                return response;
            } catch (Exception e) {
                return null;
            }
    }
}
