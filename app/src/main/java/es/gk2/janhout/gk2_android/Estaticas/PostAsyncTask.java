package es.gk2.janhout.gk2_android.Estaticas;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import java.util.Hashtable;

import es.gk2.janhout.gk2_android.R;

public class PostAsyncTask extends AsyncTask<Hashtable<String, String>, Void, String>{

    private Context contexto;
    private String url;
    private ProgressDialog progreso;
    private OnProcessCompleteListener listener;

    public PostAsyncTask(Context contexto, OnProcessCompleteListener listener, String url){
        this.contexto = contexto;
        this.url = url;
        progreso = new ProgressDialog(contexto);
        progreso.setMessage(contexto.getString(R.string.cargando_datos));
        progreso.setCancelable(false);
        this.listener = listener;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progreso.show();
    }

    @Override
    protected String doInBackground(Hashtable<String, String>... params) {
        String resultado = Peticiones.peticionPostJSON(contexto, url, params[0]);
        return resultado;
    }

    @Override
    protected void onPostExecute(String s) {
        listener.resultado(s);

        if(progreso != null && progreso.isShowing()){
            progreso.dismiss();
        }
    }

    public interface OnProcessCompleteListener{
        public void resultado(String respuesta);
    }
}
