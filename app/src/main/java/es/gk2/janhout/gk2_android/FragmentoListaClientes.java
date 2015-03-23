package es.gk2.janhout.gk2_android;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;

public class FragmentoListaClientes extends Fragment {

    private ListView lv;
    private AdaptadorListaClientes ad;
    private ArrayList<Cliente> listaClientes;

    public FragmentoListaClientes() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_lista_clientes, container, false);
        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //listaClientes = (ArrayList<Cliente>)getActivity().getIntent().getBundleExtra("datos").getSerializable("clientes");
        listaClientes = new ArrayList<>();
        listaClientes.add(new Cliente("Empresa1", "78585452C", "985585858", "644887788", "rafa@fjamil.com1"));
        listaClientes.add(new Cliente("Empresa2", "78585452D", "985585859", "644887789", "rafa@fjamil.com2"));
        listaClientes.add(new Cliente("Empresa3", "78585452E", "985585850", "644887780", "rafa@fjamil.com3"));
        lv = (ListView)getActivity().findViewById(R.id.lvClientes);
        ad = new AdaptadorListaClientes(getActivity(), R.layout.detalle_lista_cliente, listaClientes);
        lv.setAdapter(ad);
    }



}
