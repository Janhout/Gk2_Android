package es.gk2.janhout.gk2_android.Fragmentos;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;

import es.gk2.janhout.gk2_android.Adaptadores.AdaptadorListaCompras;
import es.gk2.janhout.gk2_android.Estaticas.Constantes;
import es.gk2.janhout.gk2_android.Estaticas.Peticiones;
import es.gk2.janhout.gk2_android.R;
import es.gk2.janhout.gk2_android.Util.Compra;

public class FragmentoListaCompras extends Fragment {
    private ListView lv;
    private AdaptadorListaCompras ad;
    private ArrayList<Compra> listaCompras;
    private Context contexto;

    public FragmentoListaCompras() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_lista_compras, container, false);
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.contexto = getActivity();
        cargarLista();
        if(listaCompras != null) {
            lv = (ListView) getActivity().findViewById(R.id.lvCompras);
            ad = new AdaptadorListaCompras(getActivity(), R.layout.detalle_lista_compra, listaCompras);
            lv.setAdapter(ad);
        }
    }

    private void cargarLista(){
        listaCompras = new ArrayList<>();
        HebraCargarListaCompras h = new HebraCargarListaCompras();
        h.execute();
    }

    private class HebraCargarListaCompras extends AsyncTask<Void, Void, String> {

        private ProgressDialog progreso;

        @Override
        protected void onPreExecute() {
            cargarDialogoProgreso();
        }

        @Override
        protected String doInBackground(Void... params) {
            return Peticiones.peticionGetJSON(contexto, Constantes.gastosPrueba);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progreso.dismiss();
            JSONTokener token = new JSONTokener(s);
            JSONArray array = null;
            try {
                array = new JSONArray(token);
                for (int i = 0; i < array.length(); i++) {
                    JSONObject obj = array.getJSONObject(i);
                    listaCompras.add(new Compra(obj));
                    if(ad != null) {
                        ad.notifyDataSetChanged();
                    }
                }
            } catch (JSONException e) {
                Log.e("error carga compras", e.toString());
                listaCompras = null;
            }
        }

        private void cargarDialogoProgreso(){
            progreso = new ProgressDialog(contexto);
            progreso.setMessage(getString(R.string.cargar_lista_clientes));
            progreso.setCancelable(false);
            progreso.show();
        }
    }
}