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

import es.gk2.janhout.gk2_android.Adaptadores.AdaptadorListaCompras;
import es.gk2.janhout.gk2_android.Estaticas.AsyncTaskGet;
import es.gk2.janhout.gk2_android.Estaticas.Constantes;
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
        listaCompras = new ArrayList<>();
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
        AsyncTaskGet runner = new AsyncTaskGet();
        String response;
        //Log.v("mio", Constantes.facturas + "q=cliente:" + idCliente + "&page=" + page + "&orderBy=&orderDir=&formato=json");
        AsyncTask<String, String, String> asyncTask = runner.execute(Constantes.comprasPruebas);
        //AsyncTask<String, String, String> asyncTask = runner.execute(Constantes.facturasPrueba);
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
        JSONTokener token = new JSONTokener(response);
        JSONArray array = null;

        try {
            array = new JSONArray(token);
            Log.v("mio antes del for", array.length() + "");
            for (int i = 0; i < array.length(); i++) {
                JSONObject obj = array.getJSONObject(i);
                listaCompras.add(new Compra(obj));
                if (ad != null) {
                    ad.notifyDataSetChanged();
                }
            }

        } catch (JSONException e) {
            Log.e("error carga facturas", e.toString());
            listaCompras = null;
        }
    }


}
