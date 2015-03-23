package es.gk2.janhout.gk2_android;

import android.app.ProgressDialog;
import android.content.Context;
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

public class FragmentoListaClientes extends Fragment {

    private ListView lv;
    private AdaptadorListaClientes ad;
    private ArrayList<Cliente> listaClientes;
    private Context contexto;

    public FragmentoListaClientes() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_lista_clientes, container, false);
        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.contexto = getActivity();
        //listaClientes = (ArrayList<Cliente>)getActivity().getIntent().getBundleExtra("datos").getSerializable("clientes");
        /*listaClientes = new ArrayList<>();
        listaClientes.add(new Cliente("Empresa1", "78585452C", "985585858", "644887788", "rafa@fjamil.com1"));
        listaClientes.add(new Cliente("Empresa2", "78585452D", "985585859", "644887789", "rafa@fjamil.com2"));
        listaClientes.add(new Cliente("Empresa3", "78585452E", "985585850", "644887780", "rafa@fjamil.com3"));*/
        cargarLista();
        if(listaClientes != null) {
            lv = (ListView) getActivity().findViewById(R.id.lvClientes);
            ad = new AdaptadorListaClientes(getActivity(), R.layout.detalle_lista_cliente, listaClientes);
            lv.setAdapter(ad);
        }
    }

    private void cargarLista(){
        listaClientes = new ArrayList<>();
        HebraCargarLista h = new HebraCargarLista();
        h.execute();
    }

    private class HebraCargarLista extends AsyncTask<Void, Void, String> {

        private ProgressDialog progreso;

        @Override
        protected void onPreExecute() {
            cargarDialogoProgreso();
        }

        @Override
        protected String doInBackground(Void... params) {
            return Peticiones.peticionGetJSON(contexto, Constantes.clientesPrueba);
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
                    listaClientes.add(new Cliente(obj));
                    if(ad != null) {
                        ad.notifyDataSetChanged();
                    }
                }
            } catch (JSONException e) {
                Log.e("error carga clientes", e.toString());
                listaClientes = null;
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