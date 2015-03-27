package es.gk2.janhout.gk2_android.Estaticas;

import android.os.AsyncTask;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;

public class AsyncTaskPost extends AsyncTask<String,String,String> {

    private String resp;

    @Override
    protected String doInBackground(String... params) {
        int count = params.length;
        if(count==2){
            ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>();
            postParameters.add(new BasicNameValuePair("usuario",params[0]));
            postParameters.add(new BasicNameValuePair("pass",params[1]));
            String response = null;
            try {
                response = SimpleHttpClient.executeHttpPost(Constantes.urlLogin, postParameters);
                String res = response.toString();
                resp = res.replaceAll("\\s+", "");
            } catch (Exception e) {
                e.printStackTrace();
                resp = e.getMessage();
            }
        }else{
            resp="Invalid number of arguments-"+count;
        }
        return resp;
    }
}
