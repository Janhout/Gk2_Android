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
    private boolean mostrarProgreso;

    public PostAsyncTask(Context contexto, OnProcessCompleteListener listener, String url, boolean mostrarProgreso){
        this.contexto = contexto;
        this.url = url;
        progreso = new ProgressDialog(contexto);
        progreso.setMessage(contexto.getString(R.string.cargando_datos));
        progreso.setCancelable(false);
        this.listener = listener;
        this.mostrarProgreso = mostrarProgreso;
    }

    @Override
    protected void onPreExecute() {
        if(mostrarProgreso) {
            progreso.show();
        }
    }

    @Override
    protected final String doInBackground(Hashtable<String, String>... params) {
        return Peticiones.peticionPostJSON(contexto, url, params[0]);
    }

    @Override
    protected void onPostExecute(String s) {
        listener.resultadoPost(s);
        if(progreso != null && progreso.isShowing()){
            progreso.dismiss();
        }
    }

    public interface OnProcessCompleteListener{
        public void resultadoPost(String respuesta);
    }
}
