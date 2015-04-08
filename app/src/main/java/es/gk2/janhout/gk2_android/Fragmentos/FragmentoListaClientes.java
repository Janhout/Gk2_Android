package es.gk2.janhout.gk2_android.Fragmentos;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
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
import java.util.concurrent.ExecutionException;

import es.gk2.janhout.gk2_android.Actividades.MostrarCliente;
import es.gk2.janhout.gk2_android.Adaptadores.AdaptadorListaClientes;
import es.gk2.janhout.gk2_android.Estaticas.AsyncTaskGet;
import es.gk2.janhout.gk2_android.Estaticas.Constantes;
import es.gk2.janhout.gk2_android.R;
import es.gk2.janhout.gk2_android.ScrollInfinito;
import es.gk2.janhout.gk2_android.Util.Cliente;

public class FragmentoListaClientes extends Fragment {

    private ListView lv;
    private AdaptadorListaClientes ad;
    private ArrayList<Cliente> listaClientes;
    private Context contexto;

    private int page;
    private static final int LIMITE_CONSULTA = 50;
    private static final int ITEMS_BAJO_LISTA = 5;

    public FragmentoListaClientes() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        listaClientes = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_lista_clientes, container, false);
        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.contexto = getActivity();
        page = 0;
        cargarLista();
        if(listaClientes != null) {
            lv = (ListView) getActivity().findViewById(R.id.lvClientes);
            ad = new AdaptadorListaClientes(getActivity(), R.layout.detalle_lista_cliente, listaClientes);
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
            /*lv.setOnScrollListener(new ScrollInfinito(ITEMS_BAJO_LISTA){
                @Override
                public void loadMore(int page, int totalItemsCount) {
                    FragmentoListaClientes.this.page = page;
                    cargarLista();
                }
            });*/
        }
    }

    private void cargarLista(){
        AsyncTaskGet runner=new AsyncTaskGet();
        String response;
        Log.v("mio", Constantes.clientes + "&page=" + page + "&limit=" + LIMITE_CONSULTA);
        AsyncTask<String, String, String> asyncTask = runner.execute(Constantes.clientes + "&page=" + page + "&limit=" + LIMITE_CONSULTA);
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
            Log.v("mio ", array.length()+"");
            for (int i = 0; i < array.length(); i++) {
                JSONObject obj = array.getJSONObject(i);
                listaClientes.add(new Cliente(obj));
            }
            if(ad != null) {
                ad.notifyDataSetChanged();
            }

        } catch (JSONException e) {
            listaClientes = null;
        }
    }
}