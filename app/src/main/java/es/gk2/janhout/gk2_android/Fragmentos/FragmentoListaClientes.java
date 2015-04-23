package es.gk2.janhout.gk2_android.Fragmentos;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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

import es.gk2.janhout.gk2_android.Actividades.MostrarCliente;
import es.gk2.janhout.gk2_android.Adaptadores.AdaptadorListaClientes;
import es.gk2.janhout.gk2_android.Estaticas.Constantes;
import es.gk2.janhout.gk2_android.Estaticas.GetAsyncTask;
import es.gk2.janhout.gk2_android.R;
import es.gk2.janhout.gk2_android.ScrollInfinito;
import es.gk2.janhout.gk2_android.Util.Cliente;

public class FragmentoListaClientes extends Fragment implements GetAsyncTask.OnProcessCompleteListener {

    private AdaptadorListaClientes ad;
    private ArrayList<Cliente> listaClientes;
    private Context contexto;
    private GetAsyncTask asyncTask;

    private boolean favoritos;
    private String query;

    private int page;
    private static final int LIMITE_CONSULTA = 50;
    private static final int ITEMS_BAJO_LISTA = 5;

    private static final int CODIGO_CONSULTA_CLIENTES = 1;

    public FragmentoListaClientes() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        listaClientes = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_lista_clientes, container, false);
    }

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
        if(listaClientes != null) {
            ListView lv = (ListView) getActivity().findViewById(R.id.lvClientes);
            ad = new AdaptadorListaClientes(getActivity(), R.layout.detalle_lista_cliente, listaClientes);
            lv.setAdapter(ad);
            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent i = new Intent(contexto, MostrarCliente.class);
                    Bundle b = new Bundle();
                    b.putInt("cliente", listaClientes.get(position).getId());
                    i.putExtras(b);
                    contexto.startActivity(i);
                }
            });
            lv.setOnScrollListener(new ScrollInfinito(ITEMS_BAJO_LISTA){
                @Override
                public void cargaMas(int page, int totalItemsCount) {
                    FragmentoListaClientes.this.page = page;
                    cargarLista();
                }
            });
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("fav", favoritos);
    }

    private void cargarLista(){
        String url;
        if (favoritos){
            url = Constantes.CLIENTES_LISTAR_FAVORITOS + "?q=" + query + "&page=" + page + "&orderBy=&orderDir=&limit=" + LIMITE_CONSULTA;
        } else {
            url = Constantes.CLIENTES_LISTAR + "?q=" + query + "&page=" + page + "&limit=" + LIMITE_CONSULTA;
        }
        Hashtable<String, String> parametros = new Hashtable<>();
        parametros.put("q", query);
        parametros.put("page", page+"");
        asyncTask = new GetAsyncTask(contexto, this, url, false, CODIGO_CONSULTA_CLIENTES);
        asyncTask.execute(parametros);
    }

    @Override
    public void resultadoGet(String respuesta, int codigo) {
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
        }
    }
}