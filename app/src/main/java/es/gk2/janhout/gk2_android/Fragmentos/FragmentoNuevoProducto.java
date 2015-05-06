package es.gk2.janhout.gk2_android.Fragmentos;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import es.gk2.janhout.gk2_android.R;

public class FragmentoNuevoProducto extends Fragment {


    public FragmentoNuevoProducto() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_fragmento_nuevo_producto, container, false);
    }

/*
                Fragment fragment = new FragmentoSeleccionarProducto();
                NuevaFactura.fragmentoActual = NuevaFactura.ListaFragmentosNuevaFactura.seleccionarProducto;
                Bundle bundle = new Bundle();
                bundle.putString("query", "");
                fragment.setArguments(bundle);
                FragmentoNuevaFactura.this.getActivity().invalidateOptionsMenu();
                FragmentTransaction ft = getFragmentManager().beginTransaction().replace(R.id.relativeLayoutFactura, fragment);
                ft.addToBackStack(null);
                ft.commit();
                getFragmentManager().executePendingTransactions();
 */
}
