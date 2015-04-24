package es.gk2.janhout.gk2_android.Fragmentos;

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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;
import java.util.Hashtable;

import es.gk2.janhout.gk2_android.Actividades.LectorPDF;
import es.gk2.janhout.gk2_android.Actividades.MostrarCliente;
import es.gk2.janhout.gk2_android.Adaptadores.AdaptadorListaFacturas;
import es.gk2.janhout.gk2_android.Estaticas.AsyncTaskGet;
import es.gk2.janhout.gk2_android.Estaticas.Constantes;
import es.gk2.janhout.gk2_android.R;
import es.gk2.janhout.gk2_android.ScrollInfinito;
import es.gk2.janhout.gk2_android.Util.Factura;

public class FragmentoListaFacturas extends Fragment implements AsyncTaskGet.OnProcessCompleteListener {

    private AdaptadorListaFacturas ad;
    private ArrayList<Factura> listaFacturas;
    private Context contexto;
    private int page;
    private int idCliente;
    private boolean todas;

    private static final int ITEMS_BAJO_LISTA = 5;

    private static final int CODIGO_CONSULTA_FACTURAS = 1;
    private static final int CODIGO_PEDIR_PDF = 2;

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
        return inflater.inflate(R.layout.fragment_lista, container, false);
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
            ListView lv = (ListView) getActivity().findViewById(R.id.lvLista);
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

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("all", todas);
    }

    private void cargarLista() {
        AsyncTaskGet asyncTask;
        String url;
        Hashtable<String, String> parametros = new Hashtable<>();
        url = Constantes.FACTURAS;
        if (todas){
            parametros.put("q", "");
        } else {
            parametros.put("q", "cliente:"+idCliente);
        }
        parametros.put("page", page+"");
        parametros.put("orderBy", "");
        parametros.put("orderDir", "");
        parametros.put("formato", "json");
        asyncTask = new AsyncTaskGet(contexto, this, url, false, CODIGO_CONSULTA_FACTURAS);
        asyncTask.execute(parametros);
    }

    private void verFactura(int position){
        Hashtable<String, String> parametros = null;
        String url = Constantes.PDF_URL +String.valueOf(listaFacturas.get(position).getIdImpresion());
        AsyncTaskGet a = new AsyncTaskGet(contexto, this, url, true, CODIGO_PEDIR_PDF);
        ((MostrarCliente)contexto).mostrarDialogo();
        a.execute(parametros);
    }

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

    private void intentFactura(String respuesta){
        Intent intentCompartir = new Intent(contexto, LectorPDF.class);
        intentCompartir.putExtra("pdf", respuesta);
        contexto.startActivity(intentCompartir);
    }

    @Override
    public void resultadoGet(String respuesta, int codigo){
        if(respuesta != null) {
            switch (codigo) {
                case CODIGO_CONSULTA_FACTURAS:
                    cargarFacturas(respuesta);
                    break;
                case CODIGO_PEDIR_PDF:
                    ((MostrarCliente)contexto).cerrarDialogo();
                    intentFactura(respuesta);
                    break;
            }
        }
    }
}