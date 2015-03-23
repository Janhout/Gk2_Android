package es.gk2.janhout.gk2_android;


import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
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


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentoListaFacturas extends Fragment {
    private ListView lv;
    private AdaptadorListaFacturas ad;
    private ArrayList<Factura> listaFacturas;

    public FragmentoListaFacturas() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_lista_facturas, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        cargarLista();
        if(listaFacturas != null) {
            lv = (ListView) getActivity().findViewById(R.id.lvClientes);
            ad = new AdaptadorListaFacturas(getActivity(), R.layout.detalle_lista_factura, listaFacturas);
            lv.setAdapter(ad);
        }
    }

    private void cargarLista(){
        listaFacturas = new ArrayList<>();
        HebraCargarListaFacturas h = new HebraCargarListaFacturas();
        h.execute();
    }

    private class HebraCargarListaFacturas extends AsyncTask<Void, Void, String> {

        private ProgressDialog progreso;

        @Override
        protected void onPreExecute() {
            cargarDialogoProgreso();
        }

        @Override
        protected String doInBackground(Void... params) {
            return Peticiones.peticionGetJSON(Constantes.facturasPrueba);
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
                Log.e("error carga clientes", e.toString());
                listaFacturas = null;
            }
        }

        private void cargarDialogoProgreso(){
            progreso = new ProgressDialog(FragmentoListaFacturas.getActivity());
            progreso.setMessage(getString(R.string.cargar_lista_clientes));
            progreso.setCancelable(false);
            progreso.show();
        }
    }
}
