package es.gk2.janhout.gk2_android.Fragmentos;


import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
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

import es.gk2.janhout.gk2_android.Actividades.NuevaFactura;
import es.gk2.janhout.gk2_android.Adaptadores.AdaptadorSeleccionarProducto;
import es.gk2.janhout.gk2_android.Estaticas.AsyncTaskGet;
import es.gk2.janhout.gk2_android.Estaticas.Constantes;
import es.gk2.janhout.gk2_android.Estaticas.Metodos;
import es.gk2.janhout.gk2_android.R;
import es.gk2.janhout.gk2_android.Util.Producto;

public class FragmentoSeleccionarProducto extends Fragment implements AsyncTaskGet.OnProcessCompleteListener{

    private ArrayList<Producto> listaProductos;
    private Context contexto;
    private AdaptadorSeleccionarProducto ad;
    private String query;
    private OnProductoSelectedListener listener;

    private ListView lv;
    private TextView textoVacio;

    private static final int CODIGO_CONSULTA_PRODUCTOS = 1;

    public FragmentoSeleccionarProducto() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        listaProductos = new ArrayList<>();
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
        query = getArguments().getString("query");
        listener = (NuevaFactura)getActivity();
        if(listaProductos != null) {
            if (getView() != null) {
                lv = (ListView) getView().findViewById(R.id.lvLista);
                textoVacio = (TextView) getView().findViewById(R.id.empty);
                ad = new AdaptadorSeleccionarProducto(getActivity(), R.layout.detalle_seleccionar_producto, listaProductos);
                lv.setAdapter(ad);
                lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        listener.devolverProducto(listaProductos.get(position));
                    }
                });
            }
        }
        if(query.length()>1) {
            cargarLista();
        } else {
            textoVacio.setText("lista vacia");
            lv.setEmptyView(textoVacio);
        }
    }

    private void cargarLista(){
        String url = Constantes.PRODUCTOS_LISTAR;
        Hashtable<String, String> parametros = new Hashtable<>();
        parametros.put("q", query);
        parametros.put("status", "1");
        AsyncTaskGet asyncTask = new AsyncTaskGet(contexto, this, url, false, CODIGO_CONSULTA_PRODUCTOS);
        asyncTask.execute(parametros);
    }

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
                    listaProductos.add(new Producto(obj));
                }
                if (ad != null) {
                    ad.notifyDataSetChanged();
                }
            } catch (JSONException e) {
                listaProductos = null;
            }
        } else {
            Metodos.redireccionarLogin(contexto);
        }
    }

    public interface OnProductoSelectedListener{
        public void devolverProducto(Producto producto);
    }
}