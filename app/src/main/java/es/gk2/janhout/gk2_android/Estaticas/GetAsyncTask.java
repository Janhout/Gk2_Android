package es.gk2.janhout.gk2_android.Estaticas;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.LinearLayout;

import es.gk2.janhout.gk2_android.R;

public class GetAsyncTask extends AsyncTask<Void, Void, String>{

    private Context contexto;
    private String url;
    private boolean fichero;
    private ProgressDialog progreso;
    private OnProcessCompleteListener listener;
    private boolean mostrarProgreso;
    private LinearLayout layoutProgreso;

    public GetAsyncTask(Context contexto, OnProcessCompleteListener listener, String url, boolean fichero, boolean mostrarProgreso){
        this.contexto = contexto;
        this.url = url;
        this.fichero = fichero;
        this.listener = listener;
        this.mostrarProgreso = mostrarProgreso;
        layoutProgreso = ((LinearLayout)((ActionBarActivity)contexto).findViewById(R.id.progressBar));
    }

    @Override
    protected void onPreExecute() {
        crearDialogo();
        if(mostrarProgreso) {
            mostrarDialogo();
        } else {
            layoutProgreso.bringToFront();
            layoutProgreso.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected String doInBackground(Void... params) {
        String resultado;
        if(fichero){
            resultado = Peticiones.peticionGetFichero(contexto, url);
        } else {
            resultado = Peticiones.peticionGetJSON(contexto, url);
        }
        return resultado;
    }

    @Override
    protected void onPostExecute(String s) {
        listener.resultado(s);
        layoutProgreso.setVisibility(View.GONE);
        cerrarDialogo();
    }

    public interface OnProcessCompleteListener{
        public void resultado(String respuesta);
    }

    public void mostrarDialogo(){
        progreso.show();
    }

    public void cerrarDialogo(){
        if(progreso != null && progreso.isShowing()){
            progreso.dismiss();
        }
    }

    private void crearDialogo(){
        progreso = new ProgressDialog(contexto);
        progreso.setMessage(contexto.getString(R.string.cargando_datos));
        progreso.setCancelable(false);
    }
}
