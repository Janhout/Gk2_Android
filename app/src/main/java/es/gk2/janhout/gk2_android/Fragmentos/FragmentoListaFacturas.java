package es.gk2.janhout.gk2_android.Fragmentos;

import android.app.Fragment;
import android.content.Context;
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
import es.gk2.janhout.gk2_android.Estaticas.GetAsyncTask;
import es.gk2.janhout.gk2_android.R;
import es.gk2.janhout.gk2_android.ScrollInfinito;
import es.gk2.janhout.gk2_android.Util.Factura;

public class FragmentoListaFacturas extends Fragment implements GetAsyncTask.OnProcessCompleteListener {

    private AdaptadorListaFacturas ad;
    private ArrayList<Factura> listaFacturas;
    private Context contexto;
    private int page;
    private int idCliente;
    private boolean todas;

    private static final int ITEMS_BAJO_LISTA = 5;

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
        page = 0;
        if(savedInstanceState != null) {
            todas = savedInstanceState.getBoolean("all");
        } else {
            todas = getArguments().getBoolean("todo");
        }
        idCliente = getArguments().getInt("idCliente");
        cargarLista();
        if (listaFacturas != null) {
            ListView lv = (ListView) getActivity().findViewById(R.id.lvFacturas);
            ad = new AdaptadorListaFacturas(getActivity(), R.layout.detalle_lista_factura, listaFacturas);
            lv.setAdapter(ad);
            lv.setOnScrollListener(new ScrollInfinito(ITEMS_BAJO_LISTA) {
                @Override
                public void cargaMas(int page, int totalItemsCount) {
                    FragmentoListaFacturas.this.page = page;
                    cargarLista();
                }
            });
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("all", todas);
    }

    private void cargarLista() {
        GetAsyncTask asyncTask;
        String url;
        if (todas){
            url = Constantes.FACTURAS + "?q=&page=" + page + "&orderBy=&orderDir=&formato=json";
        } else {
            url = Constantes.FACTURAS + "?q=cliente:" + idCliente +
                    "&page=" + page + "&orderBy=&orderDir=&formato=json";
        }
        if(page == 0) {
            asyncTask = new GetAsyncTask(contexto, this, url, false, true);
        } else {
            asyncTask = new GetAsyncTask(contexto, this, url, false, false);
        }
        asyncTask.execute();
    }

    @Override
    public void resultadoGet(String respuesta){
        if(respuesta != null) {
            JSONTokener token = new JSONTokener(respuesta);
            JSONArray array;
            try {
                array = new JSONArray(token);
                for (int i = 0; i < array.length(); i++) {
                    JSONObject obj = array.getJSONObject(i);
                    listaFacturas.add(new Factura(obj));
                    if (ad != null) {
                        ad.notifyDataSetChanged();
                    }
                }
            } catch (JSONException e) {
                Log.e("error carga facturas", e.toString());
                listaFacturas = null;
            }
        }
    }
}