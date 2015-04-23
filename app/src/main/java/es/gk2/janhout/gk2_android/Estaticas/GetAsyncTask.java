package es.gk2.janhout.gk2_android.Estaticas;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import java.util.Hashtable;

import es.gk2.janhout.gk2_android.R;

public class GetAsyncTask extends AsyncTask<Hashtable<String, String>, Void, String>{

    private Context contexto;
    private String url;
    private boolean fichero;
    private OnProcessCompleteListener listener;
    private LinearLayout layoutProgreso;
    private int codigo_peticion;

    public GetAsyncTask(Context contexto, OnProcessCompleteListener listener, String url, boolean fichero, int codigo_peticion){
        this.contexto = contexto;
        this.url = url;
        this.fichero = fichero;
        this.listener = listener;
        this.layoutProgreso = ((LinearLayout)((ActionBarActivity)contexto).findViewById(R.id.dialogo_progreso));
        this.codigo_peticion = codigo_peticion;
    }

    @Override
    protected void onPreExecute() {
        layoutProgreso.bringToFront();
        layoutProgreso.setVisibility(View.VISIBLE);
    }

    @Override
    protected String doInBackground(Hashtable<String, String>[] params) {
        String resultado;
        if(fichero){
            resultado = Peticiones.peticionGetFichero(contexto, url, params[0]);
        } else {
            resultado = Peticiones.peticionGetJSON(contexto, url, params[0]);
        }
        return resultado;
    }

    @Override
    protected void onPostExecute(String s) {
        listener.resultadoGet(s, codigo_peticion);
        layoutProgreso.setVisibility(View.GONE);
    }

    public interface OnProcessCompleteListener{
        public void resultadoGet(String respuesta, int codigo_peticion);
    }
}
