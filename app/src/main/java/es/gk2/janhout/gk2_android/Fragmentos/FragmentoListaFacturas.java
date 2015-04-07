package es.gk2.janhout.gk2_android.Fragmentos;

import android.app.Fragment;
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
import java.util.concurrent.ExecutionException;

import es.gk2.janhout.gk2_android.Adaptadores.AdaptadorListaFacturas;
import es.gk2.janhout.gk2_android.Estaticas.AsyncTaskGet;
import es.gk2.janhout.gk2_android.Estaticas.Constantes;
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
        listaFacturas = new ArrayList<>();
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
        cargarLista(27);
        if(listaFacturas != null) {
            lv = (ListView) getActivity().findViewById(R.id.lvFacturas);
            ad = new AdaptadorListaFacturas(getActivity(), R.layout.detalle_lista_factura, listaFacturas);
            lv.setAdapter(ad);
        }
    }

    private void cargarLista(int idCliente){
        AsyncTaskGet runner=new AsyncTaskGet();
        String response;
        Log.v("mio", Constantes.facturas + "q=cliente:" + idCliente + "&page=0&orderBy=&orderDir=&formato=json");
        AsyncTask<String, String, String> asyncTask = runner.execute(Constantes.facturas + "q=cliente:" + idCliente + "&page=0&orderBy=&orderDir=&formato=json");
        try {
            String asyncResultText = asyncTask.get();
            response = asyncResultText.trim();
        } catch (InterruptedException e1) {
            response = e1.getMessage();
        } catch (ExecutionException e1) {
            response = e1.getMessage();
        } catch (Exception e1) {
            response = e1.getMessage();
        }
        Log.v("mio response", response);
        JSONTokener token = new JSONTokener(response);
        JSONArray array = null;

        try {
            array = new JSONArray(token);
            Log.v("mio antes del for", array.length()+"");
            for (int i = 0; i < array.length(); i++) {
                JSONObject obj = array.getJSONObject(i);
                listaFacturas.add(new Factura(obj));
                if(ad != null) {
                    ad.notifyDataSetChanged();
                }
                Log.v("mio", listaFacturas.get(i).toString());
            }

        } catch (JSONException e) {
            Log.e("error carga facturas", e.toString());
            listaFacturas = null;
        }
    }
}

