package es.gk2.janhout.gk2_android.fragmentos;

import android.support.v4.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;
import java.util.Hashtable;

import es.gk2.janhout.gk2_android.adaptadores.AdaptadorListaCompras;
import es.gk2.janhout.gk2_android.util.AsyncTaskGet;
import es.gk2.janhout.gk2_android.util.Constantes;
import es.gk2.janhout.gk2_android.util.Metodos;
import es.gk2.janhout.gk2_android.R;
import es.gk2.janhout.gk2_android.modelos.Compra;

public class FragmentoListaCompras extends Fragment implements AsyncTaskGet.OnProcessCompleteListener{

    private AdaptadorListaCompras ad;
    private ArrayList<Compra> listaCompras;
    private Context contexto;

    private static final int CODIGO_CONSULTA_COMPRAS = 1;

    public FragmentoListaCompras() {
    }

    /* *************************************************************************
     **************************** Métodos on... ********************************
     *************************************************************************** */

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.contexto = getActivity();
        cargarLista();
        if(listaCompras != null) {
            ListView lv = (ListView) getActivity().findViewById(R.id.lvLista);
            ad = new AdaptadorListaCompras(getActivity(), R.layout.detalle_lista_compra, listaCompras);
            lv.setAdapter(ad);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        listaCompras = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_lista, container, false);
    }

    /* *************************************************************************
     ****************************** Auxiliares *********************************
     *************************************************************************** */

    private void cargarLista(){
        Hashtable<String, String> parametros = null;
        AsyncTaskGet a = new AsyncTaskGet(contexto, this, Constantes.COMPRAS, false, CODIGO_CONSULTA_COMPRAS);
        a.execute(parametros);
    }

    /* *************************************************************************
     **************** Interfaz OnProcessCompleteListener ***********************
     *************************************************************************** */

    @Override
    public void resultadoGet(String respuesta, int codigo){
        if(respuesta != null){
            JSONTokener token = new JSONTokener(respuesta);
            JSONArray array;

            try {
                array = new JSONArray(token);
                for (int i = 0; i < array.length(); i++) {
                    JSONObject obj = array.getJSONObject(i);
                    listaCompras.add(new Compra(obj));
                    if (ad != null) {
                        ad.notifyDataSetChanged();
                    }
                }

            } catch (JSONException e) {
                listaCompras = null;
            }
        } else {
            Metodos.redireccionarLogin(contexto);
        }
    }

}
