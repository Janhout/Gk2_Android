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
import java.util.Hashtable;

import es.gk2.janhout.gk2_android.Adaptadores.AdaptadorListaCompras;
import es.gk2.janhout.gk2_android.Estaticas.AsyncTaskGet;
import es.gk2.janhout.gk2_android.Estaticas.Constantes;
import es.gk2.janhout.gk2_android.R;
import es.gk2.janhout.gk2_android.Util.Compra;

public class FragmentoListaCompras extends Fragment implements AsyncTaskGet.OnProcessCompleteListener{

    private ListView lv;
    private AdaptadorListaCompras ad;
    private ArrayList<Compra> listaCompras;
    private Context contexto;

    private static final int CODIGO_CONSULTA_COMPRAS = 1;

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
        return inflater.inflate(R.layout.fragment_lista, container, false);
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.contexto = getActivity();
        cargarLista();
        if(listaCompras != null) {
            lv = (ListView) getActivity().findViewById(R.id.lvLista);
            ad = new AdaptadorListaCompras(getActivity(), R.layout.detalle_lista_compra, listaCompras);
            lv.setAdapter(ad);
        }
    }

    private void cargarLista(){
        Hashtable<String, String> parametros = null;
        AsyncTaskGet a = new AsyncTaskGet(contexto, this, Constantes.COMPRAS, false, CODIGO_CONSULTA_COMPRAS);
        a.execute(parametros);
    }

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
                Log.e("error carga facturas", e.toString());
                listaCompras = null;
            }
        }
    }

}
