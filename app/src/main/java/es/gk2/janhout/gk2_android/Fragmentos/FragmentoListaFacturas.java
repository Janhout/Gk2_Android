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

import es.gk2.janhout.gk2_android.Adaptadores.AdaptadorListaFacturas;
import es.gk2.janhout.gk2_android.Estaticas.Constantes;
import es.gk2.janhout.gk2_android.Estaticas.Peticiones;
import es.gk2.janhout.gk2_android.R;
import es.gk2.janhout.gk2_android.Util.Factura;

public class FragmentoListaFacturas extends Fragment {
    private ListView lv;
    private AdaptadorListaFacturas ad;
    private ArrayList<Factura> listaFacturas;
    private Context contexto;

    public FragmentoListaFacturas() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_lista_facturas, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.contexto = getActivity();
        //getArguments().getInt("idCliente");
        //cargarLista(getArguments().getInt("idCliente"));
        cargarLista(1);
        if(listaFacturas != null) {
            lv = (ListView) getActivity().findViewById(R.id.lvFacturas);
            ad = new AdaptadorListaFacturas(getActivity(), R.layout.detalle_lista_factura, listaFacturas);
            lv.setAdapter(ad);
        }
    }

    private void cargarLista(int idCliente){
        listaFacturas = new ArrayList<>();
        HebraCargarListaFacturas h = new HebraCargarListaFacturas();
        h.execute(new Integer[]{idCliente});
    }

    private class HebraCargarListaFacturas extends AsyncTask<Integer, Void, String> {

        private ProgressDialog progreso;

        @Override
        protected void onPreExecute() {
            cargarDialogoProgreso();
        }

        @Override
        protected String doInBackground(Integer... params) {
            int idCliente = params[0];
            Log.v("mio", idCliente+"");
            return Peticiones.peticionGetJSON(contexto, Constantes.facturasPrueba);
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
                    listaFacturas.add(new Factura(obj));
                    if(ad != null) {
                        ad.notifyDataSetChanged();
                    }
                }
            } catch (JSONException e) {
                Log.e("error carga facturas", e.toString());
                listaFacturas = null;
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

