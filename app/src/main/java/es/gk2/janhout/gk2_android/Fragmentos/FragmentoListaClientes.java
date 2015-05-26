package es.gk2.janhout.gk2_android.fragmentos;

import android.support.v4.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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

import es.gk2.janhout.gk2_android.actividades.MostrarCliente;
import es.gk2.janhout.gk2_android.adaptadores.AdaptadorListaProductos;
import es.gk2.janhout.gk2_android.util.AsyncTaskGet;
import es.gk2.janhout.gk2_android.util.Constantes;
import es.gk2.janhout.gk2_android.util.Metodos;
import es.gk2.janhout.gk2_android.R;
import es.gk2.janhout.gk2_android.util.ScrollInfinito;
import es.gk2.janhout.gk2_android.modelos.Cliente;

public class FragmentoListaClientes extends Fragment implements AsyncTaskGet.OnProcessCompleteListener {

    private AdaptadorListaProductos ad;
    private ArrayList<Cliente> listaClientes;
    private Context contexto;

    private ListView lv;
    private TextView textoVacio;

    private boolean favoritos;
    private String query;

    private int page;
    private static final int LIMITE_CONSULTA = 50;
    private static final int ITEMS_BAJO_LISTA = 5;

    private static final int CODIGO_CONSULTA_CLIENTES = 1;

    public FragmentoListaClientes() {
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
            favoritos = savedInstanceState.getBoolean("fav");
        } else {
            favoritos = getArguments().getBoolean("favorito");
        }
        query = getArguments().getString("query");
        cargarLista();
        inicializarListView();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        listaClientes = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_lista, container, false);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("fav", favoritos);
    }

    /* *************************************************************************
     ******************************* Auxiliares ********************************
     *************************************************************************** */

    private void cargarLista(){
        String url;
        if (favoritos){
            url = Constantes.CLIENTES_LISTAR_FAVORITOS;
        } else {
            url = Constantes.CLIENTES_LISTAR;
        }
        Hashtable<String, String> parametros = new Hashtable<>();
        parametros.put("q", query);
        parametros.put("page", page+"");
        parametros.put("orderBy", "");
        parametros.put("orderDir", "");
        parametros.put("limit", LIMITE_CONSULTA+"");
        AsyncTaskGet asyncTask = new AsyncTaskGet(contexto, this, url, false, CODIGO_CONSULTA_CLIENTES);
        asyncTask.execute(parametros);
    }

    private void inicializarListView() {
        if(listaClientes != null) {
            if (getView() != null) {
                lv = (ListView) getView().findViewById(R.id.lvLista);
                textoVacio = (TextView) getView().findViewById(R.id.empty);
                ad = new AdaptadorListaProductos(getActivity(), R.layout.detalle_lista_cliente, listaClientes);
                lv.setAdapter(ad);
                lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent i = new Intent(contexto, MostrarCliente.class);
                        Bundle b = new Bundle();
                        b.putParcelable("cliente", listaClientes.get(position));
                        i.putExtras(b);
                        contexto.startActivity(i);
                    }
                });
                lv.setOnScrollListener(new ScrollInfinito(ITEMS_BAJO_LISTA) {
                    @Override
                    public void cargaMas(int page, int totalItemsCount) {
                        FragmentoListaClientes.this.page = page;
                        cargarLista();
                    }
                });
            }
        }
    }

    /* *************************************************************************
    **************** Interfaz OnProcessCompleteListener ***********************
    *************************************************************************** */

    @Override
    public void resultadoGet(String respuesta, int codigo) {
        textoVacio.setText("lista vacia");
        lv.setEmptyView(textoVacio);
        if(respuesta != null) {
            JSONTokener token = new JSONTokener(respuesta);
            JSONArray array;
            try {
                array = new JSONArray(token);
                for (int i = 0; i < array.length(); i++) {
                    JSONObject obj = array.getJSONObject(i);
                    listaClientes.add(new Cliente(obj));
                }
                if (ad != null) {
                    ad.notifyDataSetChanged();
                }
            } catch (JSONException e) {
                listaClientes = null;
            }
        } else {
            Metodos.redireccionarLogin(contexto);
        }
    }
}