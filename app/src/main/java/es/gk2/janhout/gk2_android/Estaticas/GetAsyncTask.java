package es.gk2.janhout.gk2_android.Estaticas;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import es.gk2.janhout.gk2_android.R;

public class GetAsyncTask extends AsyncTask<Void, Void, String>{

    private Context contexto;
    private String url;
    private boolean fichero;
    private ProgressDialog progreso;
    private OnProcessCompleteListener listener;

    public GetAsyncTask(Context contexto, OnProcessCompleteListener listener, String url, boolean fichero){
        this.contexto = contexto;
        this.url = url;
        this.fichero = fichero;
        progreso = new ProgressDialog(contexto);
        progreso.setMessage(contexto.getString(R.string.cargando_datos));
        progreso.setCancelable(false);
        this.listener = listener;
    }

    @Override
    protected void onPreExecute() {
        progreso.show();
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

        if(progreso != null && progreso.isShowing()){
            progreso.dismiss();
        }
    }

    public interface OnProcessCompleteListener{
        public void resultado(String respuesta);
    }
}
