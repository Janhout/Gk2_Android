package es.gk2.janhout.gk2_android.fragmentos;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;
import java.util.Hashtable;

import es.gk2.janhout.gk2_android.actividades.LectorPDF;
import es.gk2.janhout.gk2_android.actividades.MostrarCliente;
import es.gk2.janhout.gk2_android.actividades.Principal;
import es.gk2.janhout.gk2_android.adaptadores.AdaptadorListaFacturas;
import es.gk2.janhout.gk2_android.util.AsyncTaskGet;
import es.gk2.janhout.gk2_android.util.Constantes;
import es.gk2.janhout.gk2_android.util.Metodos;
import es.gk2.janhout.gk2_android.R;
import es.gk2.janhout.gk2_android.util.ScrollInfinito;
import es.gk2.janhout.gk2_android.modelos.Factura;

public class FragmentoListaFacturas extends Fragment implements AsyncTaskGet.OnProcessCompleteListener {

    private AdaptadorListaFacturas ad;
    private ArrayList<Factura> listaFacturas;
    private Context contexto;
    private int page;
    private int idCliente;
    private boolean todas;
    private String query;

    private TextView textoVacio;
    private ListView lv;

    private static final int ITEMS_BAJO_LISTA = 5;

    private static final int CODIGO_CONSULTA_FACTURAS = 1;
    private static final int CODIGO_PEDIR_PDF = 2;

    public FragmentoListaFacturas() {
    }

    /* *************************************************************************
     **************************** Métodos on... ********************************
     *************************************************************************** */

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
        query = getArguments().getString("query");
        idCliente = getArguments().getInt("idCliente");
        cargarLista();
        inicializarListView();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        listaFacturas = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_lista, container, false);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("all", todas);
    }

    /* *************************************************************************
     **************************** Auxialiares **********************************
     *************************************************************************** */

    private void cargarFacturas(String respuesta){
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

    private void cargarLista() {
        AsyncTaskGet asyncTask;
        String url;
        Hashtable<String, String> parametros = new Hashtable<>();
        url = Constantes.FACTURAS;
        if (todas){
            parametros.put("q", query);
        } else {
            parametros.put("q", "cliente:" + idCliente + "+" + query);
        }
        parametros.put("page", page+"");
        parametros.put("orderBy", "");
        parametros.put("orderDir", "");
        parametros.put("formato", "json");
        asyncTask = new AsyncTaskGet(contexto, this, url, false, CODIGO_CONSULTA_FACTURAS);
        asyncTask.execute(parametros);
    }

    private void inicializarListView(){
        if (listaFacturas != null) {
            if (getView() != null) {
                lv = (ListView) getView().findViewById(R.id.lvLista);
                textoVacio = (TextView) getView().findViewById(R.id.empty);
                ad = new AdaptadorListaFacturas(getActivity(), R.layout.detalle_lista_factura, listaFacturas);
                lv.setAdapter(ad);
                lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        verFactura(position);
                    }
                });
                lv.setOnScrollListener(new ScrollInfinito(ITEMS_BAJO_LISTA) {
                    @Override
                    public void cargaMas(int page, int totalItemsCount) {
                        FragmentoListaFacturas.this.page = page;
                        cargarLista();
                    }
                });
            }
        }
    }

    private void intentFactura(String respuesta){
        Intent intentCompartir = new Intent(contexto, LectorPDF.class);
        intentCompartir.putExtra("pdf", respuesta);
        contexto.startActivity(intentCompartir);
    }

    private void verFactura(int position){
        String url = Constantes.PDF_URL +String.valueOf(listaFacturas.get(position).getIdImpresion());
        AsyncTaskGet a = new AsyncTaskGet(contexto, this, url, true, CODIGO_PEDIR_PDF);
        if(contexto.getClass().getName().contains(MostrarCliente.class.getSimpleName())) {
            ((MostrarCliente) contexto).mostrarDialogo(contexto);
        } else if(contexto.getClass().getName().contains(Principal.class.getSimpleName())){
            ((Principal) contexto).mostrarDialogo(contexto);
        }
        a.execute(new Hashtable<String, String>());
    }

    /* *************************************************************************
     ******************** Interfaz OnProcessCompleteListener *******************
     *************************************************************************** */

    @Override
    public void resultadoGet(String respuesta, int codigo){
        textoVacio.setText("lista vacia");
        lv.setEmptyView(textoVacio);
        if(respuesta != null) {
            switch (codigo) {
                case CODIGO_CONSULTA_FACTURAS:
                    cargarFacturas(respuesta);
                    break;
                case CODIGO_PEDIR_PDF:
                    if(contexto.getClass().getName().contains(MostrarCliente.class.getSimpleName())) {
                        ((MostrarCliente) contexto).cerrarDialogo();
                    } else if(contexto.getClass().getName().contains(Principal.class.getSimpleName())){
                        ((Principal) contexto).cerrarDialogo();
                    }
                    intentFactura(respuesta);
                    break;
            }
        } else {
            Metodos.redireccionarLogin(contexto);
        }
    }
}